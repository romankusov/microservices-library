package com.librarymicroservices.storageservice.repository;


import com.librarymicroservices.storageservice.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<BookEntity, Long> {
    @Modifying
    @Query(value = "DELETE FROM BookEntity u where u.id = ?1")
    Integer customDeleteById(Long id);
}
