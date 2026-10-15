package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.CategoryRequest;
import com.mahmoud.restaurant.dto.CategoryResponse;
import com.mahmoud.restaurant.entity.Category;
import com.mahmoud.restaurant.exception.BadRequestException;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.CategoryRepository;
import com.mahmoud.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(CategoryResponse::from).toList();
    }

    public CategoryResponse findById(Long id) {
        return CategoryResponse.from(getCategory(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new BadRequestException("Category name already exists");
        }

        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategory(id);
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new BadRequestException("Category name already exists");
        }

        category.setName(request.name());
        category.setDescription(request.description());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getCategory(id);
        if (menuItemRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Category has menu items, delete or move them first");
        }
        categoryRepository.delete(category);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
}
