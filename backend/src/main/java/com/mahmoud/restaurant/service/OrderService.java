package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.OrderItemRequest;
import com.mahmoud.restaurant.dto.OrderRequest;
import com.mahmoud.restaurant.dto.OrderResponse;
import com.mahmoud.restaurant.entity.MenuItem;
import com.mahmoud.restaurant.entity.Order;
import com.mahmoud.restaurant.entity.OrderItem;
import com.mahmoud.restaurant.entity.OrderStatus;
import com.mahmoud.restaurant.entity.RestaurantTable;
import com.mahmoud.restaurant.entity.TableStatus;
import com.mahmoud.restaurant.entity.User;
import com.mahmoud.restaurant.exception.BadRequestException;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.MenuItemRepository;
import com.mahmoud.restaurant.repository.OrderRepository;
import com.mahmoud.restaurant.repository.RestaurantTableRepository;
import com.mahmoud.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(OrderStatus status) {
        List<Order> orders = status == null
                ? orderRepository.findAllByOrderByCreatedAtDesc()
                : orderRepository.findByStatusOrderByCreatedAtDesc(status);
        return orders.stream().map(OrderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return OrderResponse.from(getOrder(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest request, String username) {
        RestaurantTable table = tableRepository.findById(request.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new BadRequestException("Table " + table.getNumber() + " is already occupied");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setTable(table);
        order.setCreatedBy(user);

        for (OrderItemRequest itemRequest : request.items()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.menuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
            if (!menuItem.isAvailable()) {
                throw new BadRequestException(menuItem.getName() + " is not available now");
            }
            order.addItem(new OrderItem(menuItem, itemRequest.quantity()));
        }

        table.setStatus(TableStatus.OCCUPIED);
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        Order order = getOrder(id);
        if (order.getStatus().isClosed()) {
            throw new BadRequestException("Order is already " + order.getStatus());
        }
        if (newStatus != OrderStatus.CANCELLED && newStatus.ordinal() <= order.getStatus().ordinal()) {
            throw new BadRequestException("Order can't go back to " + newStatus);
        }

        order.setStatus(newStatus);
        if (newStatus.isClosed()) {
            order.getTable().setStatus(TableStatus.AVAILABLE);
        }
        return OrderResponse.from(order);
    }

    @Transactional
    public void delete(Long id) {
        Order order = getOrder(id);
        if (!order.getStatus().isClosed()) {
            order.getTable().setStatus(TableStatus.AVAILABLE);
        }
        orderRepository.delete(order);
    }

    private Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
}
