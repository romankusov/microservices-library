package com.librarymicroservices.storageservice.dto;

import com.librarymicroservices.storageservice.model.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String name;
    private Integer quantity;

    public static BookDTO fromModel(BookEntity bookEntity) {
        return new BookDTO(bookEntity.getId(), bookEntity.getName(), bookEntity.getQuantity());
    }

    public static BookEntity toEntity(BookDTO bookDTO) {
        return new BookEntity(bookDTO.getId(), bookDTO.getName(), bookDTO.getQuantity());
    }
}
