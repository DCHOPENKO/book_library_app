package com.book.library.dto;

public record BookDto(
        long id,
        String title,
        AuthorPostDto author) implements DtoClass {}