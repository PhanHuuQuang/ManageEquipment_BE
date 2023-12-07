package com.example.manageequipment.repository.impl;

import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.dto.UserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestRepoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RequestDto> getRequestByUserId(Long userId) {
        String sqlQuery = "SELECT r.id, r.description, r.state, r.category_id, r.user_id FROM request r WHERE r.user_id = :user_id";
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("user_id", userId);


        List<Object[]> result = query.getResultList();
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Object[] row : result) {
            RequestDto requestDto = new RequestDto();
            requestDto.setId(Long.valueOf(row[0].toString()));
            if (row[1] != null) {
                requestDto.setDescription(row[1].toString());
            }
            if (row[2] != null) {
                requestDto.setState(row[2].toString());
            }
            if (row[3] != null) {
                requestDto.setRequestEquipmentTypeId(Long.valueOf(row[3].toString()));
            }
            if (row[4] != null) {
                requestDto.setUserId(Long.valueOf(row[4].toString()));
            }

            requestDtos.add(requestDto);
        }

        return requestDtos;
    }

    public List<RequestDto> getAllRequest() {
        String sqlQuery = "SELECT r.id, r.description, r.state, r.category_id, r.user_id FROM request r";
        Query query = entityManager.createNativeQuery(sqlQuery);

        List<Object[]> result = query.getResultList();
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Object[] row : result) {
            RequestDto requestDto = new RequestDto();
            requestDto.setId(Long.valueOf(row[0].toString()));
            if (row[1] != null) {
                requestDto.setDescription(row[1].toString());
            }
            if (row[2] != null) {
                requestDto.setState(row[2].toString());
            }
            if (row[3] != null) {
                requestDto.setRequestEquipmentTypeId(Long.valueOf(row[3].toString()));
            }
            if (row[4] != null) {
                requestDto.setUserId(Long.valueOf(row[4].toString()));
            }

            requestDtos.add(requestDto);
        }

        return requestDtos;
    }
}
