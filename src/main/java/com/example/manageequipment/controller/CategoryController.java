package com.example.manageequipment.controller;

import com.example.manageequipment.dto.CategoryDto;
import com.example.manageequipment.dto.EquipmentDto;
import com.example.manageequipment.model.Category;
import com.example.manageequipment.model.Equipment;
import com.example.manageequipment.service.CategoryService;
import com.example.manageequipment.type.IntegerArrayRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category){
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/get-category-name")
    public ResponseEntity<List<?>> getCategory(){
        return new ResponseEntity<>(categoryService.getCategoryName(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/get-category")
    public ResponseEntity<List<Category>> getCategoryWithEquipment(){
        return new ResponseEntity<>(categoryService.getCategory(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update-category")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.updateCategory(category), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete-category")
    public ResponseEntity<String> deleteCategory(@RequestBody IntegerArrayRequest categoryIds) {
        categoryService.deleteCategory(categoryIds.getIds());
        return new ResponseEntity<>("Delete category success!!", HttpStatus.OK);
    }
}

