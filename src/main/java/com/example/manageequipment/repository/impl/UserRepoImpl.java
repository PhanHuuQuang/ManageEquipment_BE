package com.example.manageequipment.repository.impl;

import com.example.manageequipment.dto.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDto> getAllUser() {
        String sqlQuery = "SELECT u.id, u.address, u.email, u.first_name, u.last_name, u.device_token, u.role_id FROM user u ";
        Query query = entityManager.createNativeQuery(sqlQuery);

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
            if (row[5] != null) {
                userDto.setDeviceToken(row[5].toString());
            }
            if (row[6] != null) {
                if (Long.valueOf(row[0].toString()) == 1) {
                    userDto.setRole("ADMIN");
                }
                if (Long.valueOf(row[0].toString()) == 2) {
                    userDto.setRole("USER");
                }
            }
            userDtos.add(userDto);
        }

        return userDtos;
    }

}
