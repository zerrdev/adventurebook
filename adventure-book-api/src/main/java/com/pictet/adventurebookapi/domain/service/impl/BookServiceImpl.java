package com.pictet.adventurebookapi.domain.service.impl;

import com.pictet.adventurebookapi.domain.model.Book;
import com.pictet.adventurebookapi.domain.model.BookSection;
import com.pictet.adventurebookapi.domain.model.BookSectionOption;
import com.pictet.adventurebookapi.domain.model.BookSectionType;
import com.pictet.adventurebookapi.domain.repository.BookRepository;
import com.pictet.adventurebookapi.domain.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<Book> getBooks() {
        return bookRepository.getBooks();
    }

    @Override
    public Optional<Book> findById(Integer bookId) {
        return bookRepository.getBookById(bookId);
    }

    @Override
    public BookSection getBookBeginSection(Book book) {
        return book.getSections().stream()
                .filter(s -> BookSectionType.BEGIN.equals(s.getType())).findFirst()
                .orElseThrow(() -> new ServerErrorException("Misconfigured book: has no begin section", null));
    }

    @Override
    public List<Integer> getUnreachableSections(Book book) {
        final Map<Integer, BookSection> sectionsById = book.getSections().stream()
                .collect(Collectors.toMap(BookSection::getId, Function.identity(), (first, duplicate) -> first));
        final Set<Integer> reached = new HashSet<>();
        final Deque<Integer> toVisit = new ArrayDeque<>();

        book.getSections().stream()
                .filter(s -> BookSectionType.BEGIN.equals(s.getType()))
                .map(BookSection::getId)
                .forEach(toVisit::push);

        while (!toVisit.isEmpty()) {
            final Integer sectionId = toVisit.pop();
            final BookSection section = sectionsById.get(sectionId);
            if (section == null || !reached.add(sectionId)) {
                continue;
            }
            Optional.ofNullable(section.getOptions()).orElse(List.of())
                    .forEach(option -> toVisit.push(option.getGotoId()));
        }

        return book.getSections().stream()
                .map(BookSection::getId)
                .filter(id -> !reached.contains(id))
                .distinct()
                .toList();
    }
}
