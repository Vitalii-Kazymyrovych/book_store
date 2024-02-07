package com.project.bookstore.repository.book.spec;

import com.project.bookstore.model.Book;
import com.project.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (((root, query, criteriaBuilder) -> root
                .get("title").in(Arrays.stream(params)
                        .toArray())));
    }
}
