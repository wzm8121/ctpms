package com.mzw.ctpmsbackend.dto;

import com.mzw.ctpmsbackend.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册信息DTO
 */
@Data
public  class RegisterDTO {
    @NotNull(message = "用户信息不能为空")
    private User user;

    @NotBlank(message = "验证码不能为空")
    private String code;
}