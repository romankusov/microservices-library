package com.librarymicroservices.orderservice.service;

import com.librarymicroservices.orderservice.client.StorageServiceClient;
import com.librarymicroservices.orderservice.client.UserServiceClient;
import com.librarymicroservices.orderservice.dto.OrderDto;
import com.librarymicroservices.orderservice.model.Order;
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
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final UserServiceClient userServiceClient;

    private final StorageServiceClient storageServiceClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDto::fromModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderDto::fromModel);
    }

    @Override
    public boolean returnBook(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getOrderStatus().equals(OrderStatus.RETURNED)) {
                return false;
            } else {
                storageServiceClient.returnBook(order.getBookId());
                userServiceClient.resetBookTaken(order.getUserId());
                order.setOrderStatus(OrderStatus.RETURNED);
                orderRepository.save(order);
                return true;
            }
        }).orElse(false);
    }

    @Override
    public Optional<Long> createOrder(OrderDto orderDto) {
        Long bookId = orderDto.getBookId();
        Long userId = orderDto.getUserId();
        Boolean bookTaken = userServiceClient.checkBookTaken(userId);
        Long bookQuantity = storageServiceClient.getBookQuantity(bookId);
        if (bookTaken && bookQuantity < 1) {
            return Optional.empty();
        } else {
            storageServiceClient.pickUp(bookId);
            userServiceClient.setBookTaken(userId);
            orderDto.setOrderStatus(OrderStatus.CREATED);
            Order order = orderRepository.save(OrderDto.toModel(orderDto));
            return Optional.of(order.getId());
        }
    }
}
