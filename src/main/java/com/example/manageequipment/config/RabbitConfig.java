package com.example.manageequipment.config;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitConfig {
    @Bean
    public static ConnectionFactory getConnection(){
        System.out.println("connection from RabbitMQConfig");

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory ("armadillo.rmq.cloudamqp.com");
        connectionFactory.setUsername("rguvxjrp");
        connectionFactory.setPassword("rxey4RJ4y_gF0Eto8QEE6h5s65joWYzu");
        connectionFactory.setVirtualHost("rguvxjrp");

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue queue = new Queue("push_notification_queue");
        admin.declareQueue(queue);

        TopicExchange exchange = new TopicExchange("equipment_exchange");
        admin.declareExchange(exchange);

        admin.declareBinding(
                BindingBuilder.bind(queue).to(exchange).with("push_notification_key"));

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(getConnection());
    }
}
