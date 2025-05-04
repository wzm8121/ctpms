package com.mzw.ctpmsbackend.rabbitmq;


import com.mzw.ctpmsbackend.dto.ProductReviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendProductForReview(ProductReviewMessage message) {
        rabbitTemplate.convertAndSend("product.review.exchange", "product.review.routingKey", message);
    }
}
