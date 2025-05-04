package com.mzw.ctpmsbackend.rabbitmq;

import com.mzw.ctpmsbackend.config.RabbitMQConfig;
import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import com.mzw.ctpmsbackend.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductAuditListener {

    private final ProductReviewService productReviewService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleReview(ProductReviewMessage message) {
        try {
            log.info("收到商品审核消息: {}", message);

            // 调用审核服务，执行 DeepSeek 模型逻辑
            productReviewService.autoReview(message);

        } catch (Exception e) {
            log.error("审核商品失败", e);
        }
    }
}

