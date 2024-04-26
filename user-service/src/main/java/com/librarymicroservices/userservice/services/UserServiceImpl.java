package com.librarymicroservices.userservice.services;

import com.librarymicroservices.userservice.dto.UserDTO;
import com.librarymicroservices.userservice.model.UserEntity;
import com.librarymicroservices.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO createUser(String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setIsBookTaken(false);
        return UserDTO.fromModel(userRepository.save(userEntity));
    }
    @Override
    public boolean deleteUser(String name) {
        return userRepository.deleteByName(name) > 0;
    }

    @Override
    public boolean checkIsBookTaken(UserDTO userDTO) {
        Optional<UserEntity> userEntityOptional = userRepository.findByName(userDTO.getName());
        if (userEntityOptional.isPresent())
        {
            return userEntityOptional.get().getIsBookTaken();
        }
        return false;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<UserEntity> userEntityList = userRepository.findAll();
        userEntityList.forEach(userE -> userDTOList.add(UserDTO.fromModel(userE)));
        return userDTOList;
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        return userEntityOptional.map(UserDTO::fromModel).orElse(null);
    }
}
