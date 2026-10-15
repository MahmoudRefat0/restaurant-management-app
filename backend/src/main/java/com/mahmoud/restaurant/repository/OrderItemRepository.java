package com.mahmoud.restaurant.repository;

import com.mahmoud.restaurant.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByMenuItemId(Long menuItemId);
}
