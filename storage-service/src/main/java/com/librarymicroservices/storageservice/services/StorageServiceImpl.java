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
    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookById(Long id) {
        return storageRepository.findById(id).map(BookDTO::fromModel);
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

}
