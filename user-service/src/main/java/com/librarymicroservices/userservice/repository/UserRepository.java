package com.librarymicroservices.userservice.repository;

import com.librarymicroservices.userservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Transactional
    long deleteByName(String name);

    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findById(Long id);


}
