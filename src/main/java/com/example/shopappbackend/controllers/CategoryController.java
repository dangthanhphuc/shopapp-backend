package com.example.shopappbackend.controllers;


import com.example.shopappbackend.dtos.CategoryDTO;
import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.category.CategoryResponse;
import com.example.shopappbackend.services.category.CategoryService;
import com.example.shopappbackend.services.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createCategory(
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
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Category created failed")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(categoryResponse)
                            .build()
            );
        }
        Category category = categoryService.createCategory(categoryDTO);
        categoryResponse.setCategory(category);
        categoryResponse.setMessage("Create category is successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Category created successfully")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(categoryResponse)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ) throws DataNotFoundException {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Category information updated successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteCategory(
            @PathVariable Long id
    ) throws DataNotFoundException {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Category information deleted successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long categoryId) throws DataNotFoundException {
        Category existingCategory = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Get category information by id is successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllCategories(
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("All categories information get successfully !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(categories)
                        .build()
        );
    }


}
