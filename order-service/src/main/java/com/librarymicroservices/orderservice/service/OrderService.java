package com.librarymicroservices.orderservice.service;

import com.librarymicroservices.orderservice.client.StorageServiceClient;
import com.librarymicroservices.orderservice.client.UserServiceClient;
import com.librarymicroservices.orderservice.dto.OrderDto;
import com.librarymicroservices.orderservice.model.OrderStatus;
import com.librarymicroservices.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserServiceClient userServiceClient;

    private final StorageServiceClient storageServiceClient;

    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDto::fromModel).toList();
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderDto::fromModel);
    }

    public boolean updateOrder(OrderDto orderDto) {
        if (orderRepository.findById(orderDto.getId()).isEmpty()) {
            return false;
        } else {
            if (orderDto.getOrderStatus().equals(OrderStatus.RETURNED)) {
                storageServiceClient.returnBook(orderDto.getBookId());
                userServiceClient.resetBookTaken(orderDto.getBookId());
            }
            orderRepository.save(OrderDto.toModel(orderDto));
            return true;
        }
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Long bookId = orderDto.getBookId();
        Long userId = orderDto.getUserId();
        Boolean bookTaken = userServiceClient.checkBookTaken(userId);
        Long bookQuantity = storageServiceClient.getBookQuantity(bookId);
        if (bookTaken && bookQuantity < 1) {
            orderDto.setOrderStatus(OrderStatus.DENIED);
        } else {
            storageServiceClient.pickUp(bookId);
            userServiceClient.setBookTaken(userId);
            orderDto.setOrderStatus(OrderStatus.CREATED);
        }
        return OrderDto.fromModel(orderRepository.save(OrderDto.toModel(orderDto)));
    }
}
