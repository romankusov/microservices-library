package com.librarymicroservices.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "storage-service", path = "api/storage")
public interface StorageServiceClient {
    @PostMapping("/pick_up/{id}")
    void pickUp(@PathVariable("id") long bookId);

    @PostMapping("/return/{id}")
    void returnBook(@PathVariable("id") long bookId);

    @GetMapping("/quantity/{id}")
    Long getBookQuantity(@PathVariable("id") long bookId);
}
