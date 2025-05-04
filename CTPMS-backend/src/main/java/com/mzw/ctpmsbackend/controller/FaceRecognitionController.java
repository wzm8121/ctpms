package com.mzw.ctpmsbackend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.dto.FaceRecordDTO;
import com.mzw.ctpmsbackend.dto.FaceVerificationDTO;
import com.mzw.ctpmsbackend.entity.FaceReview;
import com.mzw.ctpmsbackend.entity.User;
import com.mzw.ctpmsbackend.mapper.UserMapper;
import com.mzw.ctpmsbackend.service.FaceRecordService;
import com.mzw.ctpmsbackend.service.FaceVerificationService;
import javax.annotation.Resource;
import java.time.LocalDateTime;

import com.mzw.ctpmsbackend.service.ImageUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Api("人脸识别")
@RestController
@RequestMapping("/api/face")
@SaCheckLogin
public class FaceRecognitionController {
    private static final String FLASK_SERVER_URL = "http://10.8.0.10:5000";
    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionController.class);

    @Resource
    private FaceRecordService faceRecordService;
    
    @Resource
    private FaceVerificationService faceVerificationService;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private UserMapper userMapper;


    @ApiOperation("人脸注册")
    @PostMapping("/register")
    @OperationLog(type = "FACE_RECOGNITION", value = "人脸注册")
    public DataResult<String> registerFace(@RequestParam("file") MultipartFile file,
                                   @RequestParam("userId") Integer userId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(FLASK_SERVER_URL + "/register");

            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", file.getBytes(), ContentType.DEFAULT_BINARY, file.getOriginalFilename())
                    .addTextBody("userId", userId.toString(), ContentType.TEXT_PLAIN)
                    .build();

            httpPost.setEntity(multipartEntity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity());
                logger.info("人脸注册: " + responseString);
                
                // 创建人脸审核记录
                FaceVerificationDTO verificationDTO = new FaceVerificationDTO();
                verificationDTO.setUserId(userId);
                String url = imageUploadService.uploadImage(file);
                verificationDTO.setFaceImageUrl(url); // 使用返回的图片URL
                faceVerificationService.createVerification(verificationDTO);
                
                return DataResult.success(responseString);
            }
        } catch (Exception e) {
            logger.error("注册人脸失败: ", e);
            return DataResult.fail("注册人脸失败: " + e.getMessage());
        }
    }


    @ApiOperation("人脸验证")
    @PostMapping("/verify")
    @OperationLog(type = "FACE_RECOGNITION", value = "人脸验证")
    public DataResult<FaceReview> verifyFace(@RequestParam("file") MultipartFile file) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            Integer loginId = StpUtil.getLoginIdAsInt();
            User user = userMapper.selectById(loginId);
            Integer faceVerified = user.getFaceVerified();
            if (faceVerified == 0) {
                return DataResult.success("验证人脸失败：未通过人脸认证");
            }
            HttpPost httpPost = new HttpPost(FLASK_SERVER_URL + "/verify");

            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", file.getBytes(), ContentType.DEFAULT_BINARY, file.getOriginalFilename())
                    .build();

            httpPost.setEntity(multipartEntity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseString = EntityUtils.toString(response.getEntity());
                logger.info("人脸验证: " + responseString);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseString);

                // **检查 JSON 是否包含对应字段**
                boolean matched = jsonNode.has("matched") && jsonNode.get("matched").asBoolean(false);
                double confidence = jsonNode.has("confidence") ? jsonNode.get("confidence").asDouble(0.0) : 0.0;
                int userId = jsonNode.has("userId") ? jsonNode.get("userId").asInt(0) : 0;
                String status = jsonNode.has("status") ? jsonNode.get("status").asText("error") : "error";
                String message = jsonNode.has("message") ? jsonNode.get("message").asText("未知错误") : "未知错误";

                // **如果 Flask 返回 error，直接返回错误信息**
                if ("error".equals(status)) {
                    return DataResult.fail("人脸验证失败: " + message);
                }

                int loginUserId = StpUtil.getLoginIdAsInt();
                if (userId != loginUserId){
                    return DataResult.fail("验证人脸与登录用户不匹配: " + message);
                }
                // 添加人脸识别记录


                String url = imageUploadService.uploadImage(file);
                FaceRecordDTO faceRecordDTO = new FaceRecordDTO();
                faceRecordDTO.setUserId(loginUserId);
                faceRecordDTO.setImageUrl(url);
                faceRecordDTO.setResult(matched ? 1 : 2);
                faceRecordDTO.setConfidence((float) confidence);
                faceRecordDTO.setDeviceType(1);
                faceRecordDTO.setCreatedAt(LocalDateTime.now());
                faceRecordService.addFaceRecord(faceRecordDTO);

                if (matched) {
                    return DataResult.success("验证人脸成功✅ ");
                } else {
                    return DataResult.success("验证人脸失败❌");
                }
            } catch (IOException e) {
                logger.error("人脸验证失败", e);
                return DataResult.error("人脸验证失败: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error in verifyFace: ", e);
            return DataResult.fail("验证人脸失败: " + e.getMessage());
        }
    }

}
