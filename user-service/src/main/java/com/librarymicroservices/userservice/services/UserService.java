package com.librarymicroservices.userservice.services;


import com.librarymicroservices.userservice.dto.UserDTO;
import com.librarymicroservices.userservice.model.UserEntity;

import java.util.List;

public interface UserService {

    UserDTO createUser(String name);
    boolean deleteUser(String name);
    boolean checkIsBookTaken(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);

}
