package com.pictet.adventurebookapi.infrastructure.configuration;

import com.pictet.adventurebookapi.infrastructure.FileCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BooksConfiguration {

    @Value("${adventurebook.books-matcher}")
    private String booksMatcher;

    @Bean(name="bookCatalog")
    public FileCatalog bookCatalog() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:".concat(booksMatcher));
        return new FileCatalog(List.of(resources));
    }

}
