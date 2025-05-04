package com.mzw.ctpmsbackend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String vcode;
}
