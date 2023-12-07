package com.example.manageequipment.repository.impl;

import com.example.manageequipment.dto.NotificationDto;
import com.example.manageequipment.dto.RequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class NotificationRepoImpl {

    @PersistenceContext
    private EntityManager entityManager;



    public List<NotificationDto> getAllNotificationDesc(Long userId) {
        String sqlQuery = "SELECT r.id, r.created_at, r.description, r.is_read, r.type, r.user_id FROM notification r WHERE r.user_id = :user_id ORDER BY r.created_at DESC";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("user_id", userId);

        List<Object[]> result = query.getResultList();
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Object[] row : result) {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setId(Long.valueOf(row[0].toString()));
            if (row[1] != null) {
                notificationDto.setCreatedAt((Date) row[1]);
            }
            if (row[2] != null) {
                notificationDto.setDescription(row[2].toString());
            }
            if (row[3] != null) {
                notificationDto.setRead((Boolean) row[3]);
            }
            if (row[4] != null) {
                notificationDto.setType(row[4].toString());
            }
            if (row[5] != null) {
                notificationDto.setUserOwnerId(Long.valueOf(row[5].toString()));
            }

            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }
}
