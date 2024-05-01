package com.librarymicroservices.orderservice.dto;

import com.librarymicroservices.orderservice.model.Order;
import com.librarymicroservices.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    private Long userId;

    private Long bookId;

    private OrderStatus orderStatus;

    public static OrderDto fromModel(Order order) {
        return new OrderDto(order.getId(), order.getUserId(), order.getBookId(), order.getOrderStatus());
    }

    public static Order toModel(OrderDto orderDto) {
        return new Order(orderDto.getId(), orderDto.getUserId(), orderDto.getBookId(), orderDto.getOrderStatus());
    }
}
