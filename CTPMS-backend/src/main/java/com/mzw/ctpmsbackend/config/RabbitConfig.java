package com.mzw.ctpmsbackend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "product.audit.exchange";
    public static final String QUEUE = "product.audit.queue";
    public static final String ROUTING_KEY = "product.audit.key";

    @Bean
    public DirectExchange auditExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, DirectExchange auditExchange) {
        return BindingBuilder.bind(auditQueue).to(auditExchange).with(ROUTING_KEY);
    }
}
