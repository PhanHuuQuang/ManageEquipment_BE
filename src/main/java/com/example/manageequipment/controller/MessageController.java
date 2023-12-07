package com.example.manageequipment.controller;

import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.publisher.RabbitMQProducer;
import com.example.manageequipment.service.FCMService;
import com.example.manageequipment.service.impl.FCMServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private FCMService fcmService;

    private RabbitMQProducer producer;

    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message, @RequestBody RequestDto requestDto) throws JsonProcessingException {
//        producer.sendMessage(requestDto);
        return ResponseEntity.ok("Message send to RabbitMQ");
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/firebase-test")
    public ResponseEntity<String> firebase(@RequestParam("tokenDevice") String tokenDevice ) throws FirebaseMessagingException {
//        fcmService.sendFCMNotification(tokenDevice);
        return  ResponseEntity.ok("Message send to RabbitMQ");
    }
}
