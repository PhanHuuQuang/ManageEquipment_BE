package com.example.manageequipment.service.impl;


import com.example.manageequipment.service.AuthenticationService;
import com.example.manageequipment.service.FCMService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


@Service
public class FCMServiceImpl implements FCMService  {

    @Override
    public String sendFCMNotification(String tokenDevice, String title, String body) throws FirebaseMessagingException {
        // Tạo một thông báo
        Message message = Message.builder()
                .setNotification(new Notification(title, body))
                .setToken(tokenDevice)
                .build();


        // Gửi thông báo
        String response = FirebaseMessaging.getInstance().send(message);
        return "FCM notification sent: " + response;
    }
}
