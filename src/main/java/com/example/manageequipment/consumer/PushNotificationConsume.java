package com.example.manageequipment.consumer;

import com.example.manageequipment.config.RabbitConfig;
import com.example.manageequipment.dto.NotificationDto;
import com.example.manageequipment.dto.TypeNotification;
import com.example.manageequipment.model.Request;
import com.example.manageequipment.model.Role;
import com.example.manageequipment.model.User;
import com.example.manageequipment.repository.RequestRepository;
import com.example.manageequipment.repository.RoleCustomRepo;
import com.example.manageequipment.repository.UserRepository;
import com.example.manageequipment.service.FCMService;
import com.example.manageequipment.service.NotificationService;
import com.example.manageequipment.service.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.tomcat.util.json.JSONParserConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Component
public class PushNotificationConsume implements MessageListener {

    @Autowired
    private RequestService requestService;

    @Autowired
    private RoleCustomRepo roleCustomRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private RequestRepository requestRepository;

    private RabbitTemplate rabbitTemplate;

    public PushNotificationConsume(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void onMessage(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] body = message.getBody();
        String messageContent = new String(body);
        TypeNotification typeNotification;

        try {
            typeNotification = objectMapper.readValue(messageContent, TypeNotification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Receive message: "+ typeNotification.getType());

        if (typeNotification.getType().equals("REQUEST")) {
            Role role = roleCustomRepo.findByName("ADMIN").get();

            List<User> listUserAdmin = userRepository.findByRole(role);

            listUserAdmin.forEach(user -> {
                NotificationDto notificationDto = new NotificationDto();
                notificationDto.setDescription("You have new request from user!");
                notificationDto.setUserOwnerId(user.getId());
                notificationDto.setRead(false);
                notificationDto.setType("REQUEST");
                notificationDto.setCreatedAt(new Date());

                notificationService.createNotification(notificationDto);
                try {
                    fcmService.sendFCMNotification(user.getDeviceToken(), "REQUEST", "You have new request from user!");
                } catch (FirebaseMessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (typeNotification.getType().equals("CONFIRM")) {

            String requestId = new String(String.valueOf(typeNotification.getContent()));

            Request request = requestRepository.findById((Long.valueOf(requestId))).get();

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setDescription("Admin confirmed your request for "+request.getRequestEquipmentType().getName());
            notificationDto.setUserOwnerId(request.getUserOwner().getId());
            notificationDto.setRead(false);
            notificationDto.setType("REQUEST");
            notificationDto.setCreatedAt(new Date());

            notificationService.createNotification(notificationDto);
            try {
                if (request.getUserOwner().getDeviceToken() != null) {
                    fcmService.sendFCMNotification(request.getUserOwner().getDeviceToken(), "REQUEST", "Admin confirmed your request for "+request.getRequestEquipmentType().getName());
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }

        if (typeNotification.getType().equals("REJECT")) {

            String requestId = new String(String.valueOf(typeNotification.getContent()));

            Request request = requestRepository.findById((Long.valueOf(requestId))).get();

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setDescription("Admin rejected your request for "+request.getRequestEquipmentType().getName());
            notificationDto.setUserOwnerId(request.getUserOwner().getId());
            notificationDto.setRead(false);
            notificationDto.setType("REQUEST");
            notificationDto.setCreatedAt(new Date());

            notificationService.createNotification(notificationDto);
            try {
                if (request.getUserOwner().getDeviceToken() != null) {
                    fcmService.sendFCMNotification(request.getUserOwner().getDeviceToken(), "REQUEST", "Admin rejected your request for "+request.getRequestEquipmentType().getName());
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }

        if (typeNotification.getType().equals("TRANSFER")) {

            String userId = new String(String.valueOf(typeNotification.getContent()));

            User user = userRepository.findById((Long.valueOf(userId))).get();

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setDescription("Admin trust transfer equipment for you ");
            notificationDto.setUserOwnerId(user.getId());
            notificationDto.setRead(false);
            notificationDto.setType("EQUIPMENT");
            notificationDto.setCreatedAt(new Date());

            notificationService.createNotification(notificationDto);
            try {
                if (user.getDeviceToken() != null) {
                    fcmService.sendFCMNotification(user.getDeviceToken(), "NEW EQUIPMENT", "Admin trust transfer equipment for you");
                }
            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
