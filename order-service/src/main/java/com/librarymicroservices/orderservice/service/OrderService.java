package com.librarymicroservices.orderservice.service;

import com.librarymicroservices.orderservice.dto.OrderDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> getAllOrders();

    Optional<OrderDto> getOrderById(Long id);

    boolean returnBook(Long orderId);

    Optional<Long> createOrder(OrderDto orderDto);
}
