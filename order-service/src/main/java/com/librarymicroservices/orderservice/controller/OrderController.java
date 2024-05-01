package com.librarymicroservices.orderservice.controller;

import com.librarymicroservices.orderservice.dto.OrderDto;
import com.librarymicroservices.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtoList = orderService.getAllOrders();
        if (orderDtoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orderDtoList);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.of(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto order = orderService.createOrder(orderDto);
        return ResponseEntity.created(URI.create("api/orders/" + order.getId())).body(order);
    }

    @PutMapping
    public ResponseEntity<Void> updateOrder(@RequestBody OrderDto orderDto) {
        return orderService.updateOrder(orderDto) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }
}
