package com.example.manageequipment.dto;

import com.example.manageequipment.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long userOwnerId;
    private String type;
    private String description;
    private Date createdAt;
    private boolean isRead;
}
