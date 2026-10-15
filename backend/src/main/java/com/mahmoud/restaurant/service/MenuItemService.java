package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.MenuItemRequest;
import com.mahmoud.restaurant.dto.MenuItemResponse;
import com.mahmoud.restaurant.entity.Category;
import com.mahmoud.restaurant.entity.MenuItem;
import com.mahmoud.restaurant.exception.BadRequestException;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.CategoryRepository;
import com.mahmoud.restaurant.repository.MenuItemRepository;
import com.mahmoud.restaurant.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll(Long categoryId) {
        List<MenuItem> items = categoryId == null
                ? menuItemRepository.findAll()
                : menuItemRepository.findByCategoryId(categoryId);
        return items.stream().map(MenuItemResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public MenuItemResponse findById(Long id) {
        return MenuItemResponse.from(getMenuItem(id));
    }

    @Transactional
    public MenuItemResponse create(MenuItemRequest request) {
        MenuItem item = new MenuItem();
        fillMenuItem(item, request);
        return MenuItemResponse.from(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemResponse update(Long id, MenuItemRequest request) {
        MenuItem item = getMenuItem(id);
        fillMenuItem(item, request);
        return MenuItemResponse.from(item);
    }

    @Transactional
    public void delete(Long id) {
        MenuItem item = getMenuItem(id);
        if (orderItemRepository.existsByMenuItemId(id)) {
            throw new BadRequestException("Item is used in orders, mark it as unavailable instead");
        }
        menuItemRepository.delete(item);
    }

    private void fillMenuItem(MenuItem item, MenuItemRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setAvailable(request.available());
        item.setCategory(category);
    }

    private MenuItem getMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
    }
}
