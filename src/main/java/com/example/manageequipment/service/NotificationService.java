package com.example.manageequipment.service;

import com.example.manageequipment.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    void createNotification(NotificationDto notificationDto);

    List<NotificationDto> getNotification(Long userId);

    NotificationDto readNotification(Long notificationId);

    void readAllNotification(Long userId);

    void clearAllNotification(Long userId);
}
