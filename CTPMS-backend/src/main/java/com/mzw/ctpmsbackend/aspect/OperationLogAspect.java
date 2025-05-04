package com.mzw.ctpmsbackend.aspect;

import cn.dev33.satoken.session.SaSession;
import com.mzw.ctpmsbackend.annotation.OperationLog;
import com.mzw.ctpmsbackend.entity.UserOperationLog;
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
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;


@Aspect
@Component
public class OperationLogAspect {
    private final LogService logService;

    public OperationLogAspect(LogService logService) {
        this.logService = logService;
    }

    @Around("@annotation(com.mzw.ctpmsbackend.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 执行目标方法
        long time = System.currentTimeMillis() - beginTime;

        // 记录日志
        recordOperationLog(joinPoint, time, result);
        return result;
    }

    private void recordOperationLog(ProceedingJoinPoint joinPoint, long time, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 1. 获取注解信息
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        UserOperationLog log = new UserOperationLog();
        if (operationLog != null) {
            log.setOperation(operationLog.value()); // 操作描述
            log.setType(operationLog.type());      // 操作类型
        }

        // 2. 方法信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        log.setMethod(className + "." + methodName + "()");

        // 3. 请求参数（JSON格式）
        try {
            String params = JSON.toJSONString(joinPoint.getArgs());
            log.setParams(params.length() > 2000 ? "参数过长" : params); // 防止超长字段
        } catch (Exception e) {
            log.setParams("参数解析失败");
        }

        // 4. 返回结果（JSON格式）
        try {
            String jsonResult = JSON.toJSONString(result);
            log.setResult(jsonResult.length() > 2000 ? "结果过长" : jsonResult);
        } catch (Exception e) {
            log.setResult("结果解析失败");
        }

        // 5. 设置IP地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.setIp(getClientIp(request));

        // 6. 设置操作用户（Sa-Token方式）
        if (StpUtil.isLogin()) { // 检查是否登录

            Integer userId = StpUtil.getLoginIdAsInt();
            // 设置 session
            SaSession session = StpUtil.getSession();
            String username = (String) session.get("username");
            log.setUserId(userId); // 获取当前用户ID
            log.setUsername(username); // 获取用户名（或自定义）
        }

        // 7. 执行耗时 & 时间
        log.setTime(time);
        log.setCreateTime(LocalDateTime.now());

        // 8. 保存到数据库
        logService.saveUserOperationLog(log);
    }

    /**
     * 获取客户端IP（兼容Nginx反向代理）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}