package com.mzw.ctpmsbackend.aspect;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.mzw.ctpmsbackend.annotation.TransactionLog;
import com.mzw.ctpmsbackend.common.utils.DataResult;
import com.mzw.ctpmsbackend.common.utils.IpUtils;
import com.mzw.ctpmsbackend.dto.TransactionDTO;

import com.mzw.ctpmsbackend.entity.TransactionLogEntity;
import com.mzw.ctpmsbackend.service.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionLogAspect {
    private final LogService transactionLogService;

    @Around("@annotation(com.mzw.ctpmsbackend.annotation.TransactionLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - beginTime;

        recordTransactionLog(joinPoint, time, result);
        return result;
    }

    private void recordTransactionLog(ProceedingJoinPoint joinPoint, long time, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TransactionLogEntity log = new TransactionLogEntity();

        // 1. 获取注解信息
        TransactionLog transactionLog = method.getAnnotation(TransactionLog.class);
        if (transactionLog != null) {
            log.setOperation(transactionLog.value());
            log.setType(transactionLog.type());
        }

        // 2. 方法信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();

        // 3. 请求参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSON.toJSONString(args);
            log.setParams(params.length() > 2000 ? "参数过长" : params);
        } catch (Exception e) {
            log.setParams("参数解析失败");
        }

        // 4. 返回结果
        try {
            String jsonResult = JSON.toJSONString(result);
            log.setResult(jsonResult.length() > 2000 ? "结果过长" : jsonResult);

            // 从返回结果中提取交易信息
            if (result instanceof DataResult) {
                Object data = ((DataResult<?>) result).getData();
                if (data instanceof TransactionDTO) {
                    TransactionDTO dto = (TransactionDTO) data;
                    log.setTransactionNo(dto.getTransactionNo());
                    log.setBuyerId(dto.getBuyerId());
                    log.setSellerId(dto.getSellerId());
                    log.setItemId(dto.getItemId());
                    log.setAmount(dto.getAmount());
                    log.setStatus(dto.getStatus());
                }
            }
        } catch (Exception e) {
            log.setResult("结果解析失败");
        }

        // 5. 用户信息(Sa-Token)
        if (StpUtil.isLogin()) {
            Integer userId = StpUtil.getLoginIdAsInt();
            // 设置 session
            SaSession session = StpUtil.getSession();
            String username = (String) session.get("username");


            // 根据交易类型确定买卖方
            if ("REFUND".equals(log.getType())) {
                log.setSellerId(userId);
                log.setSellerName(username);
            } else {
                log.setBuyerId(userId);
                log.setBuyerName(username);
            }
        }

        // 6. IP地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.setIp(IpUtils.getIpAddress(request));

        // 7. 执行时间
        log.setTime(time);
        log.setCreateTime(LocalDateTime.now());

        transactionLogService.saveTransactionLog(log);
    }
}