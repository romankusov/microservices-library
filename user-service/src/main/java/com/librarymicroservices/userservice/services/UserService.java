package com.librarymicroservices.userservice.services;


import com.librarymicroservices.userservice.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    boolean deleteUser(Long id);

    boolean checkIsBookTaken(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long id);

    boolean updateUser(UserDTO userDTO);
}
