package com.example.manageequipment.service.impl;

import com.example.manageequipment.dto.NotificationDto;
import com.example.manageequipment.model.Notification;
import com.example.manageequipment.model.User;
import com.example.manageequipment.repository.NotificationRepository;
import com.example.manageequipment.repository.UserRepository;
import com.example.manageequipment.repository.impl.NotificationRepoImpl;
import com.example.manageequipment.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepoImpl notificationRepo;

    public NotificationDto mapToDto(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();

        notificationDto.setId(notification.getId());
        notificationDto.setUserOwnerId(notification.getUserOwner().getId());
        notificationDto.setCreatedAt(notification.getCreatedAt());
        notificationDto.setDescription(notification.getDescription());
        notificationDto.setRead(notification.isRead());
        notificationDto.setType(notification.getType());

        return notificationDto;
    }


    @Override
    public void createNotification(NotificationDto notificationDto) {
        Notification notification = new Notification();

        notification.setType(notificationDto.getType());
        notification.setRead(notificationDto.isRead());
        notification.setCreatedAt(notificationDto.getCreatedAt());
        notification.setDescription(notificationDto.getDescription());

        User userOwner = userRepository.findById(notificationDto.getUserOwnerId()).get();

        notification.setUserOwner(userOwner);

        Notification newNotification = notificationRepository.save(notification);

        mapToDto(newNotification);
    }

    @Override
    public List<NotificationDto> getNotification(Long userId) {

        List<NotificationDto> notificationList = notificationRepo.getAllNotificationDesc(userId);

        return notificationList;
    }

    @Override
    public NotificationDto readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).get();

        notification.setRead(true);

        Notification newNotification = notificationRepository.save(notification);

        return mapToDto(newNotification);
    }

    @Override
    public void readAllNotification(Long userId) {
        User owner = userRepository.findById(userId).get();
        List<Notification> notificationList = notificationRepository.findByUserOwner(owner);

        notificationList.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public void clearAllNotification(Long userId) {
        User owner = userRepository.findById(userId).get();
        List<Notification> notificationList = notificationRepository.findByUserOwnerOrderByCreatedAtDesc(owner);

        notificationList.forEach(notification -> {
            owner.getNotifications().remove(notification);
            notificationRepository.delete(notification);
        });
    }
}
