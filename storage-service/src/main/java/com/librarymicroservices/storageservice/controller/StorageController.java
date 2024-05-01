package com.librarymicroservices.storageservice.controller;


import com.librarymicroservices.storageservice.dto.BookDTO;
import com.librarymicroservices.storageservice.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        System.out.println("Вызван getAllBooks");
        List<BookDTO> bookDTOList = storageService.getAllBooks();
        if (!bookDTOList.isEmpty()) {
            return ResponseEntity.ok(bookDTOList);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        System.out.println("Вызван getBookById");
        Optional<BookDTO> optionalBookDTO = storageService.getBookById(id);
        if (optionalBookDTO.isPresent()) {
            BookDTO bookDTO = optionalBookDTO.get();
            if (storageService.decrementBookQuantity(id)) {
                return ResponseEntity.ok(bookDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        System.out.println("Вызван createBook");
        BookDTO book = storageService.createBook(bookDTO);
        return ResponseEntity.created(URI.create("/api/books/" + book.getId())).body(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        System.out.println("Вызван deleteBook");
        return storageService.deleteBook(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBook(@RequestBody BookDTO bookDTO) {
        System.out.println("Вызван updateBook");
        return storageService.updateBook(bookDTO) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }
}
