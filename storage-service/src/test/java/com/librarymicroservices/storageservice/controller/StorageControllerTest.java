package com.librarymicroservices.storageservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymicroservices.storageservice.dto.BookDTO;
import com.librarymicroservices.storageservice.services.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StorageController.class)
public class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;

    @Test
    void shouldGetAllBooks() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setName("Test Book");
        book.setQuantity(5);
        when(storageService.getAllBooks()).thenReturn(Collections.singletonList(book));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Book"))
                .andExpect(jsonPath("$[0].quantity").value(5));
    }

    @Test
    void shouldGetBookById() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setName("Test Book");
        book.setQuantity(5);
        when(storageService.getBookById(1L)).thenReturn(Optional.of(book));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldPickUpBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/pick_up/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(storageService, times(1)).pickUp(1L);
    }

    @Test
    void shouldReturnBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage/return/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(storageService, times(1)).returnBook(1L);
    }

    @Test
    void shouldGetBookQuantity() throws Exception {
        when(storageService.getBookQuantity(1L)).thenReturn(5);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/storage/quantity/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookDTO book = new BookDTO();
        book.setId(1L);
        book.setName("Test Book");
        book.setQuantity(5);
        when(storageService.createBook(any(BookDTO.class))).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/storage")
                        .content(asJsonString(book))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookDTO updatedBook = new BookDTO();
        updatedBook.setId(1L);
        updatedBook.setName("Updated Book");
        updatedBook.setQuantity(10);
        when(storageService.updateBook(any(BookDTO.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/storage")
                        .content(asJsonString(updatedBook))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        doReturn(true).when(storageService).deleteBook(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/storage/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        verify(storageService, times(1)).deleteBook(1L);
    }
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}