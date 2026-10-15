package com.mahmoud.restaurant.controller;

import com.mahmoud.restaurant.dto.TableRequest;
import com.mahmoud.restaurant.dto.TableResponse;
import com.mahmoud.restaurant.dto.TableStatusRequest;
import com.mahmoud.restaurant.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public List<TableResponse> findAll() {
        return tableService.findAll();
    }

    @GetMapping("/{id}")
    public TableResponse findById(@PathVariable Long id) {
        return tableService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(@Valid @RequestBody TableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.create(request));
    }

    @PutMapping("/{id}")
    public TableResponse update(@PathVariable Long id, @Valid @RequestBody TableRequest request) {
        return tableService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public TableResponse updateStatus(@PathVariable Long id, @Valid @RequestBody TableStatusRequest request) {
        return tableService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
