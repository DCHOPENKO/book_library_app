package com.book.library.dto;

import java.util.List;

public record AuthorDto(
        long id,
        String firstname,
        String lastname,
        List<BookDto> books) implements DtoClass {}