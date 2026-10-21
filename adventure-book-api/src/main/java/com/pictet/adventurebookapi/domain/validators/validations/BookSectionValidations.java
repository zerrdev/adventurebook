package com.pictet.adventurebookapi.domain.validators.validations;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.validators.BookValidation;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookSectionValidations implements BookValidation {
    @Override
    public List<String> validate(Book book) {
        ArrayList<String> errors = new ArrayList<>();
        Map<Integer, BookSection> sections = new HashMap<>();

        if (book.getSections().isEmpty()) {
            errors.add("Sections cannot be empty");
        }

        for (BookSection bookSection : book.getSections()) {
            if (sections.containsKey(bookSection.getId())) {
                errors.add("Duplicate section ID: %d".formatted(bookSection.getId()));
            } else {
                sections.put(bookSection.getId(), bookSection);
            }
        }

        book.getSections().stream()
                .filter(section -> Objects.nonNull(section.getOptions()))
                .flatMap(s -> s.getOptions().stream())
                .map(BookSectionOption::getGotoId)
                .filter(gotoId -> !sections.containsKey(gotoId))
                .forEach(gotoId -> errors.add("Invalid section gotoId: %d".formatted(gotoId)));

        return errors;
    }
}
