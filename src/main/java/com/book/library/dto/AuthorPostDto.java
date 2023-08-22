package com.book.library.dto;

public record AuthorPostDto(long id,
                            String firstname,
                            String lastname) implements DtoClass {}