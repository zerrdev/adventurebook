package com.pictet.adventurebookapi.application.mapper;

import com.pictet.adventurebookapi.application.dto.BookDTO;
import com.pictet.adventurebookapi.application.dto.BookDetailsDTO;
import com.pictet.adventurebookapi.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final ModelMapper modelMapper;

    public BookDetailsDTO toDetailsDTO(final Book book) {
        return modelMapper.map(book, BookDetailsDTO.class);
    }

    private BookDTO toDTO(final Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public List<BookDTO> toDTO(final List<Book> books) {
        return books.stream().map(this::toDTO).toList();
    }
}
