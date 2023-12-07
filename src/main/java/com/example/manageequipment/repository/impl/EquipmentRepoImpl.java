package com.example.manageequipment.repository.impl;

import com.example.manageequipment.dto.EquipmentDto;
import com.example.manageequipment.dto.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EquipmentRepoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<EquipmentDto> getEquipmentForUser(int ownerId) {
        String sqlQuery = "SELECT e.id, e.name, e.image_url, e.owner_id, e.category_id, e.description FROM equipment e WHERE e.owner_id = :owner_id";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("owner_id", ownerId);

        List<Object[]> result = query.getResultList();
        List<EquipmentDto> equipmentDtos = new ArrayList<>();
        for (Object[] row : result) {
            EquipmentDto equipmentDto = new EquipmentDto();
            equipmentDto.setId(Long.valueOf(row[0].toString()));
            equipmentDto.setName(row[1].toString());
            if (row[2] != null) {
                equipmentDto.setImageUrl(row[2].toString());
            }
            if (row[3] != null) {
                equipmentDto.setOwnerId(Long.valueOf(row[3].toString()));
            }
            if (row[4] != null) {
                equipmentDto.setType(Long.valueOf(row[4].toString()));
            }
            if (row[5] != null) {
                equipmentDto.setDescription(row[5].toString());
            }

            equipmentDtos.add(equipmentDto);
        }

        return equipmentDtos;
    }

    public List<EquipmentDto> getAllEquipment() {
        String sqlQuery = "SELECT e.id, e.name, e.image_url, e.owner_id, e.category_id, e.description FROM equipment e";
        Query query = entityManager.createNativeQuery(sqlQuery);

        List<Object[]> result = query.getResultList();
        List<EquipmentDto> equipmentDtos = new ArrayList<>();
        for (Object[] row : result) {
            EquipmentDto equipmentDto = new EquipmentDto();
            equipmentDto.setId(Long.valueOf(row[0].toString()));
            equipmentDto.setName(row[1].toString());
            if (row[2] != null) {
                equipmentDto.setImageUrl(row[2].toString());
            }
            if (row[3] != null) {
                equipmentDto.setOwnerId(Long.valueOf(row[3].toString()));
            }
            if (row[4] != null) {
                equipmentDto.setType(Long.valueOf(row[4].toString()));
            }
            if (row[5] != null) {
                equipmentDto.setDescription(row[5].toString());
            }
            equipmentDtos.add(equipmentDto);
        }

        return equipmentDtos;
    }

    public List<UserDto> getTransferedUser(int equipmentId) {
        String sqlQuery = "SELECT u.id, u.address, u.email, u.first_name, u.last_name FROM user u " +
                "LEFT JOIN transfer_history t ON u.id = t.user_id WHERE t.equipment_id = :equipment_id";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("equipment_id", equipmentId);

        List<Object[]> result = query.getResultList();
        List<UserDto> userDtos = new ArrayList<>();
        for (Object[] row : result) {
            UserDto userDto = new UserDto();    
            userDto.setId(Long.valueOf(row[0].toString()));
            if (row[1] != null) {
                userDto.setAddress(row[1].toString());
            }
            if (row[2] != null) {
                userDto.setEmail(row[2].toString());
            }
            if (row[3] != null) {
                userDto.setFirstName(row[3].toString());
            }
            if (row[4] != null) {
                userDto.setLastName(row[4].toString());
            }
            userDtos.add(userDto);
        }

        return userDtos;
    }
}
