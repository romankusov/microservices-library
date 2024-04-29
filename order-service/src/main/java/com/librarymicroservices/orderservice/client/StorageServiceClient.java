package com.librarymicroservices.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "storage-service")
public interface StorageServiceClient {
    @GetMapping("/pick_up/{id}")
    void pickUp(@PathVariable("id") int bookId);

    @GetMapping("/return/{id}")
    void returnBook(@PathVariable("id") int bookId);
}
