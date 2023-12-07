package com.example.manageequipment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "type")
    @JsonIgnore
    private Set<Equipment> equipments = new HashSet<>();

    @OneToMany(mappedBy = "requestEquipmentType")
    @JsonIgnore
    private Set<Request> requests = new HashSet<>();

    public int getTotalEquipment() {
        return equipments.size();
    }


}
