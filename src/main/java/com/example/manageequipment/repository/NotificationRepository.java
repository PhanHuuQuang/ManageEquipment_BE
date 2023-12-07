package com.example.manageequipment.repository;

import com.example.manageequipment.model.Notification;
import com.example.manageequipment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOwner(User user);

    List<Notification> findByUserOwnerOrderByCreatedAtDesc(User user);

}
