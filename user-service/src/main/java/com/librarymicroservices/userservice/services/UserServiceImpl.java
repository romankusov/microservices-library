package com.librarymicroservices.userservice.services;

import com.librarymicroservices.userservice.dto.UserDTO;
import com.librarymicroservices.userservice.model.UserEntity;
import com.librarymicroservices.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::fromModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromModel);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        if (userRepository.findById(userDTO.getId()).isEmpty()) {
            return false;
        }
        userRepository.save(UserDTO.toEntity(userDTO));
        return true;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = UserDTO.toEntity(userDTO);
        user.setIsBookTaken(false);
        return UserDTO.fromModel(userRepository.save(user));
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.customDeleteById(id) > 0;
    }

    @Override
    public boolean checkIsBookTaken(UserDTO userDTO) {
        Optional<UserEntity> user = userRepository.findById(userDTO.getId());
        return user.isPresent() ? user.get().getIsBookTaken() : false;
    }
}
