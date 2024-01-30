package com.example.shopappbackend.services.category;


import com.example.shopappbackend.dtos.CategoryDTO;
import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.exceptions.DataNotFoundException;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(long id, CategoryDTO categoryDTO) throws DataNotFoundException;
    Category deleteCategoryById(long id) throws DataNotFoundException;
    Category getCategoryById(long id) throws DataNotFoundException;
    List<Category> getAllCategories();
}
