package com.librarymicroservices.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "storage-service", path = "api/storage")
public interface StorageServiceClient {
    @GetMapping("/pick_up/{id}")
    void pickUp(@PathVariable("id") long bookId);

    @GetMapping("/return/{id}")
    void returnBook(@PathVariable("id") long bookId);

    @GetMapping("/quantity/{id}")
    Long getBookQuantity(@PathVariable("id") long bookId);
}
