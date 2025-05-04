package com.mzw.ctpmsbackend.service;

import com.mzw.ctpmsbackend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    /**
     * 上传图片到图床
     * @param file 上传的图片文件
     * @return 返回图片 URL
     */
    String uploadImage(MultipartFile file) throws ServiceException;
}

