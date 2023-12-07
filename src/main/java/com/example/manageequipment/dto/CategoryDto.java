package com.example.manageequipment.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
