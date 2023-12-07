package com.example.manageequipment.publisher;

import com.example.manageequipment.config.RabbitConfig;
import com.example.manageequipment.dto.EquipmentDto;
import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.dto.TypeNotification;
import com.example.manageequipment.dto.UserDto;
import com.example.manageequipment.service.EquipmentService;
import com.example.manageequipment.service.RequestService;
import com.example.manageequipment.type.IntegerArrayRequest;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.ConnectableFlux;

import java.io.IOException;
import java.util.List;

@Service
public class RabbitMQProducer {
    
    @Autowired
    private RequestService requestService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EquipmentService equipmentService;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createRequestAndSendNotification(RequestDto message) {
        System.out.println("connection from RabbitMQProducer");
        requestService.createRequestEquipment(message);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        TypeNotification typeNotification = new TypeNotification().builder().type("REQUEST").content(message).build();

        rabbitTemplate.convertAndSend("equipment_exchange", "push_notification_key", typeNotification);
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void confirmRequestAndSendNotification(Long requestId) {
        requestService.confirmRequestEquipment(requestId);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        TypeNotification typeNotification = new TypeNotification().builder().type("CONFIRM").content(requestId).build();

        rabbitTemplate.convertAndSend("equipment_exchange", "push_notification_key", typeNotification);
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void rejectRequestAndSendNotification(Long requestId) {
        requestService.rejectRequestEquipment(requestId);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        TypeNotification typeNotification = new TypeNotification().builder().type("REJECT").content(requestId).build();

        rabbitTemplate.convertAndSend("equipment_exchange", "push_notification_key", typeNotification);
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public UserDto transferEquipmentAndSendNotification(List<Long> equipmentIds, Long userId) {
        UserDto userDto = equipmentService.transferEquipment(equipmentIds, userId);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        TypeNotification typeNotification = new TypeNotification().builder().type("TRANSFER").content(userId).build();

        rabbitTemplate.convertAndSend("equipment_exchange", "push_notification_key", typeNotification);
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return userDto;
    }
}
