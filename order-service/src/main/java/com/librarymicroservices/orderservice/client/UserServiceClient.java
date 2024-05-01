package com.librarymicroservices.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", path = "api/users")
public interface UserServiceClient {
    @PostMapping("/taken/{id}/reset")
    ResponseEntity<Void> resetBookTaken(@PathVariable Long id);

    @PostMapping("/taken/{id}/set")
    ResponseEntity<Void> setBookTaken(@PathVariable Long id);

    @GetMapping("/taken/{id}")
    Boolean checkBookTaken(@PathVariable Long id);
}
