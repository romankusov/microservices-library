package com.librarymicroservices.userservice.controller;

import com.librarymicroservices.userservice.dto.UserDTO;
import com.librarymicroservices.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        if (!userDTOList.isEmpty()) {
            return ResponseEntity.ok(userDTOList);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.createUser(userDTO);
        return ResponseEntity.created(URI.create("/api/users/" + user.getId())).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/taken/{id}/set")
    public ResponseEntity<Void> setBookTaken(@PathVariable Long id) {
        return userService.setBookTaken(id, true) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/taken/{id}/reset")
    public ResponseEntity<Void> resetBookTaken(@PathVariable Long id) {
        return userService.setBookTaken(id, false) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/taken/{id}")
    public Boolean checkBookTaken(@PathVariable Long id) {
        return userService.checkIsBookTaken(id);
    }
}
