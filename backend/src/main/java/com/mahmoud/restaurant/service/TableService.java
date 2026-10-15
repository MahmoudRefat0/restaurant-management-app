package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.TableRequest;
import com.mahmoud.restaurant.dto.TableResponse;
import com.mahmoud.restaurant.entity.RestaurantTable;
import com.mahmoud.restaurant.entity.TableStatus;
import com.mahmoud.restaurant.exception.BadRequestException;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.OrderRepository;
import com.mahmoud.restaurant.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository tableRepository;
    private final OrderRepository orderRepository;

    public List<TableResponse> findAll() {
        return tableRepository.findAllByOrderByNumberAsc().stream().map(TableResponse::from).toList();
    }

    public TableResponse findById(Long id) {
        return TableResponse.from(getTable(id));
    }

    public TableResponse create(TableRequest request) {
        if (tableRepository.existsByNumber(request.number())) {
            throw new BadRequestException("Table number already exists");
        }

        RestaurantTable table = new RestaurantTable();
        table.setNumber(request.number());
        table.setCapacity(request.capacity());
        return TableResponse.from(tableRepository.save(table));
    }

    public TableResponse update(Long id, TableRequest request) {
        RestaurantTable table = getTable(id);
        if (tableRepository.existsByNumberAndIdNot(request.number(), id)) {
            throw new BadRequestException("Table number already exists");
        }

        table.setNumber(request.number());
        table.setCapacity(request.capacity());
        return TableResponse.from(tableRepository.save(table));
    }

    public TableResponse updateStatus(Long id, TableStatus status) {
        RestaurantTable table = getTable(id);
        table.setStatus(status);
        return TableResponse.from(tableRepository.save(table));
    }

    public void delete(Long id) {
        RestaurantTable table = getTable(id);
        if (orderRepository.existsByTableId(id)) {
            throw new BadRequestException("Table has orders and can't be deleted");
        }
        tableRepository.delete(table);
    }

    private RestaurantTable getTable(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
    }
}
