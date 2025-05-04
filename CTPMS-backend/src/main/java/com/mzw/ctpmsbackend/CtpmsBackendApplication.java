package com.mzw.ctpmsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling  // 启用定时任务（服务器不能启动，需要本地服务）
@EnableAspectJAutoProxy
@MapperScan("com.mzw.ctpmsbackend.mapper")
public class CtpmsBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CtpmsBackendApplication.class, args);

    }
}
