package com.librarymicroservices.userservice.repository;

import com.librarymicroservices.userservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Modifying
    @Query(value = "DELETE FROM UserEntity u WHERE u.id = ?1")
    Integer customDeleteById(Long id);
}
