package com.pictet.adventurebookapi.domain.validators.validations;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.model.BookSectionType;
import com.pictet.adventurebookapi.domain.validators.BookValidation;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookSectionValidations implements BookValidation {
    @Override
    public List<String> validate(Book book) {
        ArrayList<String> errors = new ArrayList<>();
        Map<Integer, BookSection> sections = new HashMap<>();
        int beginCount = 0;
        int endCount = 0;

        if (book.getSections().isEmpty()) {
            errors.add("Sections cannot be empty");
        }

        for (BookSection bookSection : book.getSections()) {
            if (sections.containsKey(bookSection.getId())) {
                errors.add("Duplicate section ID: %d".formatted(bookSection.getId()));
            } else {
                sections.put(bookSection.getId(), bookSection);
            }

            if (BookSectionType.BEGIN.equals(bookSection.getType())) {
                beginCount++;
            } else if (BookSectionType.END.equals(bookSection.getType())) {
                endCount++;
            }
        }

        if (beginCount < 1) {
            errors.add("A book cannot have less than one begin");
        }

        if (beginCount > 1) {
            errors.add("A book cannot have more than one begin");
        }

        if (endCount < 1) {
            errors.add("A book cannot have less than one end");
        }

        book.getSections().stream()
                .filter(section -> Objects.nonNull(section.getOptions()))
                .flatMap(s -> s.getOptions().stream())
                .map(BookSectionOption::getGotoId)
                .filter(gotoId -> !sections.containsKey(gotoId))
                .forEach(gotoId -> errors.add("Invalid section gotoId: %d".formatted(gotoId)));

        book.getSections().stream()
                .filter(section -> !BookSectionType.END.equals(section.getType()))
                .filter(section -> section.getOptions() == null || section.getOptions().isEmpty())
                .forEach(section -> errors.add("Non-ending section without options: %d".formatted(section.getId())));

        return errors;
    }
}
