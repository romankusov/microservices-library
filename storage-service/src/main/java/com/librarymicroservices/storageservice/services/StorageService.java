package com.librarymicroservices.storageservice.services;




import com.librarymicroservices.storageservice.dto.BookDTO;

import java.util.List;
import java.util.Optional;

public interface StorageService {
    BookDTO createBook(BookDTO bookDTO);

    boolean deleteBook(Long id);

    List<BookDTO> getAllBooks();

    Optional<BookDTO> getBookById(Long id);

    boolean updateBook(BookDTO bookDTO);

    boolean decrementBookQuantity(Long id);
}
