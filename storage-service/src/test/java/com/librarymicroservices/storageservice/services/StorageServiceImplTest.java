package com.librarymicroservices.storageservice.services;
import com.librarymicroservices.storageservice.dto.BookDTO;
import com.librarymicroservices.storageservice.model.BookEntity;
import com.librarymicroservices.storageservice.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StorageServiceImplTest {

    @Mock
    private StorageRepository storageRepository;

    @InjectMocks
    private StorageService storageService = new StorageServiceImpl(storageRepository);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storageRepository = mock(StorageRepository.class);
        storageService = new StorageServiceImpl(storageRepository);
    }

    @Test
    void testGetAllBooks() {
        List<BookDTO> expectedBooks = new ArrayList<>();
        when(storageRepository.findAll()).thenReturn(new ArrayList<>());
        List<BookDTO> actualBooks = storageService.getAllBooks();
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void testCreateBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Test Book");
        bookDTO.setQuantity(5);
        when(storageRepository.save(any())).thenReturn(BookDTO.toEntity(bookDTO));
        BookDTO savedBook = storageService.createBook(bookDTO);
        assertEquals(bookDTO, savedBook);
    }

    @Test
    void testDeleteBook() {
        long bookId = 1L;
        when(storageRepository.customDeleteById(bookId)).thenReturn(1);
        boolean result = storageService.deleteBook(bookId);
        assertEquals(true, result);
    }

    @Test
    void testPickUp_QuantityGreaterThanZero() {
        long bookId = 1L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        bookEntity.setQuantity(5); // Set quantity greater than zero
        when(storageRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        storageService.pickUp(bookId);
        assertEquals(4, bookEntity.getQuantity());
        verify(storageRepository, times(1)).save(bookEntity);
    }

    @Test
    void testPickUp_QuantityZero() {
        long bookId = 1L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        bookEntity.setQuantity(0); // Set quantity to zero
        when(storageRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        storageService.pickUp(bookId);
        assertEquals(0, bookEntity.getQuantity());
        verify(storageRepository, never()).save(bookEntity); // Ensure save is not called
    }

    @Test
    void testUpdateBook_ExistingBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Book Name");
        bookDTO.setQuantity(5);
        when(storageRepository.findById(bookDTO.getId())).thenReturn(Optional.of(new BookEntity()));
        boolean result = storageService.updateBook(bookDTO);
        assertTrue(result);
        verify(storageRepository, times(1)).save(any(BookEntity.class));
    }

    @Test
    void testUpdateBook_NonExistingBook() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Book Name");
        bookDTO.setQuantity(5);
        when(storageRepository.findById(bookDTO.getId())).thenReturn(Optional.empty());
        boolean result = storageService.updateBook(bookDTO);
        assertFalse(result);
        verify(storageRepository, never()).save(any());
    }

    @Test
    void testGetBookById_ExistingBook() {
        long bookId = 1L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        when(storageRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Optional<BookDTO> result = storageService.getBookById(bookId);
        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getId());
    }

    @Test
    void testGetBookById_NonExistingBook() {
        long bookId = 1L;
        when(storageRepository.findById(bookId)).thenReturn(Optional.empty());
        Optional<BookDTO> result = storageService.getBookById(bookId);
        assertFalse(result.isPresent());
    }

    @Test
    void testReturnBook() {
        long bookId = 1L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        bookEntity.setQuantity(5);
        when(storageRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        storageService.returnBook(bookId);
        assertEquals(6, bookEntity.getQuantity());
        verify(storageRepository, times(1)).save(bookEntity);
    }

    @Test
    void testGetBookQuantity_ExistingBook() {
        long bookId = 1L;
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookId);
        bookEntity.setQuantity(5);
        when(storageRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        int result = storageService.getBookQuantity(bookId);
        assertEquals(5, result);
    }

    @Test
    void testGetBookQuantity_NonExistingBook() {
        long bookId = 1L;
        when(storageRepository.findById(bookId)).thenReturn(Optional.empty());
        int result = storageService.getBookQuantity(bookId);
        assertEquals(0, result);
    }
}