package com.mzw.ctpmsbackend.dto;

import lombok.Data;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO {
    private Integer userId;
    private String studentId;
    private String phone;
    private String email;
    private String realName;
    private String avatarUrl;
    private Integer isSeller;
    private String role;
}
