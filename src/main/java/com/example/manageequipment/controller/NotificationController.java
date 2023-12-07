package com.example.manageequipment.controller;

import com.example.manageequipment.dto.NotificationDto;
import com.example.manageequipment.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasAnyAuthority('ADMIN') or #userId == authentication.credentials")
    @PostMapping("/get-notification/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotification(@PathVariable Long userId) {
        return new ResponseEntity<>(notificationService.getNotification(userId), HttpStatus.OK);
    }    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/read-notification/{notificationId}")
    public ResponseEntity<NotificationDto> readNotification(@PathVariable Long notificationId) {
        return new ResponseEntity<>(notificationService.readNotification(notificationId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') or #userId == authentication.credentials")
    @PostMapping("/read-all-notification/{userId}")
    public ResponseEntity<String> readAllNotification(@PathVariable Long userId) {
        notificationService.readAllNotification(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') or #userId == authentication.credentials")
    @DeleteMapping("/clear-all-notification/{userId}")
    public ResponseEntity<String> clearAllNotification(@PathVariable Long userId) {
        notificationService.clearAllNotification(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
