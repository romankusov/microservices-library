package com.librarymicroservices.userservice.controller;

import com.librarymicroservices.userservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.librarymicroservices.userservice.services.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getusers")
    public ResponseEntity<?> getAllUsers()
    {
        List<UserDTO> userDTOList = userService.getAllUsers();
        if (userDTOList.size() > 0)
        {
            return ResponseEntity.ok(userDTOList);
        }

        return ResponseEntity.internalServerError().body("No users in DB");
    }

    @GetMapping("/getuserbyid")
    public ResponseEntity<?> getUserById(@RequestParam Long id)
    {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO != null)
        {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.badRequest().body("No such user in DB");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestParam String name)
    {
        UserDTO userDTO = userService.createUser(name);
        if (userDTO != null)
        {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.internalServerError().body("Impossible to create user");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String name)
    {
        if (!userService.deleteUser(name))
        {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("No such user in DB");
    }
}
