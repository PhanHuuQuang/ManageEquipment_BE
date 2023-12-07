package com.example.manageequipment.config;

import com.example.manageequipment.consumer.PushNotificationConsume;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListenerConfig {
    private ConnectionFactory connectionFactory;
    private PushNotificationConsume pushNotificationConsume;

    public RabbitMQListenerConfig(ConnectionFactory connectionFactory,PushNotificationConsume pushNotificationConsume) {
        this.connectionFactory = connectionFactory;
        this.pushNotificationConsume = pushNotificationConsume;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        System.out.println("connection from ListenerConfig");
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.setQueueNames("push_notification_queue"); // Queue(s) to listen to
        container.setMessageListener(pushNotificationConsume);

        return container;
    }
}
