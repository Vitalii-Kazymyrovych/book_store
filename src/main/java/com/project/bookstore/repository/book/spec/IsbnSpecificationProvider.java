package com.project.bookstore.repository.book.spec;

import com.project.bookstore.model.Book;
import com.project.bookstore.repository.SpecificationProvider;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.domain.Specification;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "isbn";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) -> root
                .get(KEY).in((Object[]) params));
    }
}
