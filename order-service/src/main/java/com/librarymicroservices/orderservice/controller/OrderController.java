package com.librarymicroservices.orderservice.controller;

import com.librarymicroservices.orderservice.dto.OrderDto;
import com.librarymicroservices.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto) {
        Optional<Long> createdOrder = orderService.createOrder(orderDto);
        return createdOrder.isPresent() ?
                ResponseEntity.created(URI.create("api/orders/" + createdOrder.get())).build() :
                ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()),
                        "Book is taken or book quantity is not enough")).build();
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        return orderService.returnBook(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }
}
