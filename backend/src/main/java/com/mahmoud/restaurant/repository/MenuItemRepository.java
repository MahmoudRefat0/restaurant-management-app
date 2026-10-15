package com.mahmoud.restaurant.repository;

import com.mahmoud.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategoryId(Long categoryId);

    boolean existsByCategoryId(Long categoryId);
}
