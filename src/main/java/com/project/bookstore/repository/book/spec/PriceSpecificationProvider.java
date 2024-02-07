package com.project.bookstore.repository.book.spec;

import com.project.bookstore.model.Book;
import com.project.bookstore.repository.SpecificationProvider;

import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "price";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (((root, query, criteriaBuilder) -> root
                .get("price").in(Arrays.stream(params)
                        .map(BigDecimal::new)
                        .toArray())));
    }
}
