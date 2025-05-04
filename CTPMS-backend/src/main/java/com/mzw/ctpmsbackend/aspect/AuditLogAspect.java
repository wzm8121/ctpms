package com.mzw.ctpmsbackend.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.mzw.ctpmsbackend.annotation.AuditLog;
import com.mzw.ctpmsbackend.common.utils.IpUtils;
import com.mzw.ctpmsbackend.context.AuditLogContext;
import com.mzw.ctpmsbackend.entity.AdminAuditLog;
import com.mzw.ctpmsbackend.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLogAspect {
    private final LogService logService;

    public AuditLogAspect(LogService logService) {
        this.logService = logService;
    }

    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        // 每次初始化新的上下文，避免复用上一次的 AdminAuditLog
        AuditLogContext.init();
        long beginTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long time = System.currentTimeMillis() - beginTime;
            // 不管成功失败，都要记录一条新的日志
            recordAuditLog(joinPoint, time);
            AuditLogContext.clear(); // 释放 ThreadLocal
        }
    }

    private void recordAuditLog(ProceedingJoinPoint joinPoint, long time) {
        AdminAuditLog log = AuditLogContext.get();
        if (log == null) {
            log = new AdminAuditLog();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuditLog auditLog = method.getAnnotation(AuditLog.class);

        // 设置基础信息
        log.setOperation(auditLog.value());
        log.setAuditType(auditLog.type());
        log.setTime(time);
        log.setCreateTime(LocalDateTime.now());

        // ✅ 设置 targetId、reason、result，从 AuditLogContext 中获取
        log.setTargetId(AuditLogContext.get().getTargetId());
        log.setReason(AuditLogContext.get().getReason());
        log.setResult(AuditLogContext.get().getResult());

        // 审核人信息
        if (StpUtil.isLogin() && StpUtil.hasRole("admin")) {
            log.setAuditorId(StpUtil.getLoginIdAsInt());
            log.setAuditorName((String) StpUtil.getSession().get("username"));
        } else {
            log.setAuditorId(100000); // 系统Id
            log.setAuditorName("系统");
        }

        // IP 地址
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            log.setIp(IpUtils.getIpAddress(request));
        } catch (Exception e) {
            log.setIp("获取失败");
        }

        // 参数
        try {
            Object[] args = joinPoint.getArgs();
            String params = JSON.toJSONString(args);
            log.setParams(params.length() > 2000 ? "参数过长" : params);
        } catch (Exception e) {
            log.setParams("参数解析失败");
        }

        // 保存日志
        logService.saveAdminAuditLog(log);
    }
}


