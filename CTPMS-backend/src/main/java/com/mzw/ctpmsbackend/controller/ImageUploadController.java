package com.mzw.ctpmsbackend.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.service.ImageUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 地址控制器
 */
@RestController
@RequestMapping("/api/image")
@Api(tags = "图片管理")
@SaCheckLogin
public class ImageUploadController {

    @Resource
    private ImageUploadService imageUploadService;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public DataResult<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageUploadService.uploadImage(file);
            return DataResult.success(imageUrl);
        } catch (Exception e) {
            return DataResult.fail("图片上传失败：" + e.getMessage());
        }
    }
}
