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
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOList = storageService.getAllBooks();
        if (!bookDTOList.isEmpty()) {
            return ResponseEntity.ok(bookDTOList);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<BookDTO> optionalBookDTO = storageService.getBookById(id);
        if (optionalBookDTO.isPresent()) {
            return ResponseEntity.ok(optionalBookDTO.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pick_up/{id}")
    public void pickUp(@PathVariable long id) {
        storageService.pickUp(id);
    }

    @GetMapping("/quantity/{id}")
    public Long getBookQuantity(@PathVariable long id) {
        return Long.valueOf(storageService.getBookQuantity(id));
    }

    @GetMapping("/return/{id}")
    public void returnBook(@PathVariable long id) {
        storageService.returnBook(id);
    }
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO book = storageService.createBook(bookDTO);
        return ResponseEntity.created(URI.create("/api/storage/" + book.getId())).body(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return storageService.deleteBook(id) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBook(@RequestBody BookDTO bookDTO) {
        return storageService.updateBook(bookDTO) ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }
}
