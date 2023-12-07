package com.example.manageequipment.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

@Service
public interface FCMService {
    String sendFCMNotification(String tokenDevice, String title, String body) throws FirebaseMessagingException;

}
