package com.mzw.ctpmsbackend.service.impl;

import com.mzw.ctpmsbackend.exception.ServiceException;
import com.mzw.ctpmsbackend.service.ImageUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

    @Resource
    private RestTemplate restTemplate;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 最大5MB
    private static final String UPLOAD_URL = "http://123.60.139.233:40027/api/v1/upload"; // 图床API地址

    @Override
    public String uploadImage(MultipartFile file) throws ServiceException {
        // 1️⃣ 验证文件类型
        if (!file.getContentType().startsWith("image/")) {
            log.warn("文件类型不合法：{}", file.getContentType());
            throw new ServiceException("仅支持图片文件上传");
        }

        // 2️⃣ 验证文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("文件过大：{} 字节", file.getSize());
            throw new ServiceException("文件大小不能超过5MB");
        }

        try {
            // 3️⃣ 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 4️⃣ 封装文件内容
            ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 5️⃣ 发送上传请求
            ResponseEntity<Map> response = restTemplate.postForEntity(UPLOAD_URL, requestEntity, Map.class);

            // 6️⃣ 解析响应结果
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Object dataObj = responseBody.get("data");

                if (dataObj instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) dataObj;
                    Object linksObj = data.get("links");

                    if (linksObj instanceof Map) {
                        Map<String, Object> links = (Map<String, Object>) linksObj;
                        Object url = links.get("url");
                        if (url instanceof String) {
                            return (String) url;
                        }
                    }
                }
            }

            log.error("上传失败，返回格式异常，响应体: {}", response.getBody());
            throw new ServiceException("上传失败，服务器返回数据格式不正确");

        } catch (IOException | RestClientException e) {
            log.error("图片上传异常: {}", e.getMessage(), e);
            throw new ServiceException("图片上传失败，请稍后重试");
        }
    }
}


