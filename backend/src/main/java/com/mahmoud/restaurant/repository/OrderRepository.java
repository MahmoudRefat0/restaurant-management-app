package com.mahmoud.restaurant.repository;

import com.mahmoud.restaurant.entity.Order;
import com.mahmoud.restaurant.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    boolean existsByTableId(Long tableId);
}
