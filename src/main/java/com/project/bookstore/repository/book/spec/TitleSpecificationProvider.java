package com.project.bookstore.repository.book.spec;

import com.project.bookstore.model.Book;
import com.project.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "title";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (((root, query, criteriaBuilder) -> root
                .get(KEY).in(Arrays.stream(params)
                        .toArray())));
    }
}
