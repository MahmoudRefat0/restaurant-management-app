package com.mahmoud.restaurant.repository;

import com.mahmoud.restaurant.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findAllByOrderByNumberAsc();

    boolean existsByNumber(Integer number);

    boolean existsByNumberAndIdNot(Integer number, Long id);
}
