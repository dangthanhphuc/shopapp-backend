package com.example.shopappbackend.controllers;


import com.example.shopappbackend.dtos.CategoryDTO;
import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.category.CategoryResponse;
import com.example.shopappbackend.services.category.CategoryService;
import com.example.shopappbackend.services.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ){
        CategoryResponse categoryResponse = new CategoryResponse();
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            categoryResponse.setErrors(errors);
            categoryResponse.setMessage("Create category is failed");
            return ResponseEntity.badRequest().body(categoryResponse);
        }
        Category category = categoryService.createCategory(categoryDTO);
        categoryResponse.setCategory(category);
        categoryResponse.setMessage("Create category is successfully");
        return ResponseEntity.ok(categoryResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ){
        try {
            categoryService.updateCategory(id, categoryDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully updated category");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id
    ){
        try{
            categoryService.deleteCategoryById(id);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully updated category");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long categoryId){
        try {
            Category existingCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(existingCategory);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


}
