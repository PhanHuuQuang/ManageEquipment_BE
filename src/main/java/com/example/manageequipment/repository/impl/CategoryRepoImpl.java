package com.example.manageequipment.repository.impl;

import com.example.manageequipment.dto.CategoryDto;
import com.example.manageequipment.dto.EquipmentDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryDto> getNameOfCategory() {
        String sqlQuery = "SELECT id, name FROM category";

        Query query = entityManager.createNativeQuery(sqlQuery);

        List<Object[]> result = query.getResultList();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Object[] row : result) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(Long.valueOf(row[0].toString()));
            categoryDto.setName(row[1].toString());
            categoryDtos.add(categoryDto);
        }


        return categoryDtos;
    }


}
