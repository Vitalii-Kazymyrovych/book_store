package com.project.bookstore.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record BookSearchParameters
        (String[] titles,
         String[] authors,
         String[] prices,
         String[] isbns) {
}
