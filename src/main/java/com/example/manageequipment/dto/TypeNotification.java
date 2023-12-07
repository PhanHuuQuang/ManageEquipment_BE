package com.example.manageequipment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeNotification {

    public String type;
    public Object content;
}
