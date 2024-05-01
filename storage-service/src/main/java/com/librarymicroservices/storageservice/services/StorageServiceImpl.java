package com.librarymicroservices.storageservice.services;

import com.librarymicroservices.storageservice.dto.BookDTO;
import com.librarymicroservices.storageservice.model.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.librarymicroservices.storageservice.repository.StorageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        return storageRepository.findAll().stream().map(BookDTO::fromModel).toList();
    }

    @Override
    public void pickUp(Long id) {
        Optional<BookEntity> optionalBookEntity = storageRepository.findById(id);
        optionalBookEntity.ifPresent(bookEntity -> {
            int quantity = bookEntity.getQuantity();
            if (quantity > 0) {
                bookEntity.setQuantity(quantity - 1);
                storageRepository.save(bookEntity);
            }
        });
    }

    @Override
    public boolean updateBook(BookDTO bookDTO) {
        if (storageRepository.findById(bookDTO.getId()).isEmpty()) {
            return false;
        }
        storageRepository.save(BookDTO.toEntity(bookDTO));
        return true;
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        BookEntity book = BookDTO.toEntity(bookDTO);
        return BookDTO.fromModel(storageRepository.save(book));
    }

    @Override
    public boolean deleteBook(Long id) {
        return storageRepository.customDeleteById(id) > 0;
    }


    @Override
    public boolean decrementBookQuantity(Long id) {
        Optional<BookEntity> optionalBookEntity = storageRepository.findById(id);
        if (optionalBookEntity.isPresent()) {
            BookEntity bookEntity = optionalBookEntity.get();
            int currentQuantity = bookEntity.getQuantity();
            if (currentQuantity > 0) {
                bookEntity.setQuantity(currentQuantity - 1);
                storageRepository.save(bookEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<BookDTO> getBookById(Long id) {
        return storageRepository.findById(id).map(BookDTO::fromModel);
    }

    @Override
    public void returnBook(Long id) {
        Optional<BookEntity> optionalBookEntity = storageRepository.findById(id);
        optionalBookEntity.ifPresent(bookEntity -> {
            int quantity = bookEntity.getQuantity();
            bookEntity.setQuantity(quantity + 1);
            storageRepository.save(bookEntity);
        });
    }


    @Override
    public Integer getBookQuantity(Long id) {
        return storageRepository.findById(id).map(BookEntity::getQuantity).orElse(0);
    }

}
