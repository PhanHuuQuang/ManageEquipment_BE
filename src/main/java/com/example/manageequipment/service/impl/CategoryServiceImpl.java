package com.example.manageequipment.service.impl;

import com.example.manageequipment.model.Category;
import com.example.manageequipment.repository.CategoryRepository;
import com.example.manageequipment.repository.EquipmentRepository;
import com.example.manageequipment.repository.RequestRepository;
import com.example.manageequipment.repository.impl.CategoryRepoImpl;
import com.example.manageequipment.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CategoryRepoImpl categoryRepo;

    @Override
    public Category createCategory(Category cate) {
        Category category = new Category();
        category.setName(cate.getName());

        Category newCategory = categoryRepository.save(category);

        return newCategory;
    }

    @Override
    public List<?> getCategoryName() {
        List<?> listCategory = categoryRepo.getNameOfCategory();

        return listCategory;
    }

    @Override
    public Category updateCategory(Category newCategory) {

        categoryRepository.findById(newCategory.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid category id " + newCategory.getId()));

        return categoryRepository.save(newCategory);
    }

    @Override
    public void deleteCategory(List<Long> categoryIds) {
        categoryIds.forEach(categoryId -> {
            Category category = categoryRepository.findById(categoryId).get();
            category.getEquipments().forEach(equipment -> {
                equipment.setType(null);
                equipmentRepository.save(equipment);
            });
            category.getRequests().forEach(request -> {
                request.setRequestEquipmentType(null);
                requestRepository.save(request);

            });
            categoryRepository.deleteById(categoryId);
        });
    }

    @Override
    public List<Category> getCategory() {
        List<Category> listCategory = categoryRepository.findAll();

        System.out.println();

        return listCategory;
    }

}
