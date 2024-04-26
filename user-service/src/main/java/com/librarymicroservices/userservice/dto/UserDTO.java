package com.librarymicroservices.userservice.dto;

import com.librarymicroservices.userservice.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private Boolean isTaken;


    public static UserDTO fromModel(UserEntity userEntity)
    {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getIsBookTaken());
    }

    public static UserEntity fromDTO(UserDTO userDTO)
    {
        return new UserEntity(userDTO.getId(), userDTO.getName(), userDTO.getIsTaken());
    }
}
