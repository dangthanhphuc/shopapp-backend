package com.example.shopappbackend.services.category;

import com.example.shopappbackend.dtos.CategoryDTO;
import com.example.shopappbackend.entities.Category;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(long id, CategoryDTO categoryDTO) throws DataNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Category not found")
        );
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategoryById(long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Category not found")
        );
        category.setDeleted(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) throws DataNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new DataNotFoundException("Category not found");
        }
        return category.get();
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
