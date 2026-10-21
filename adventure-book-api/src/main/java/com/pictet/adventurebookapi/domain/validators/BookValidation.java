package com.pictet.adventurebookapi.domain.validators;

import com.pictet.adventurebookapi.domain.model.Book;

import java.util.List;

public interface BookValidation {
    List<String> validate(Book book);
}
