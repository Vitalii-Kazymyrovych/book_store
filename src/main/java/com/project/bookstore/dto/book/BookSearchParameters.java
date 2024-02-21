package com.project.bookstore.dto.book;

public record BookSearchParameters
        (String[] titles,
         String[] authors,
         String[] prices,
         String[] isbns) {
}
