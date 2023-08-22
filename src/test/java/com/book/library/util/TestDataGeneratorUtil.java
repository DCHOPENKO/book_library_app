package com.book.library.util;


import com.book.library.dto.AuthorDto;
import com.book.library.dto.AuthorPostDto;
import com.book.library.dto.BookDto;
import com.book.library.model.Author;
import com.book.library.model.Book;

import java.util.List;

public class TestDataGeneratorUtil {

    private TestDataGeneratorUtil() {
    }

    public static Author fetchAuthorWithoutBooksAndId() {
        return Author.builder()
                .firstname("John")
                .lastname("Smith")
                .build();
    }
    public static Author fetchAuthorWithoutBooks() {
        return Author.builder()
                .id(23L)
                .firstname("John")
                .lastname("Smith")
                .build();
    }

    public static AuthorDto fetchAuthorDtoWithoutBooks() {
        return new AuthorDto(23L, "John", "Smith", null);
    }

    public static AuthorPostDto fetchAuthorPostDto() {
        return new AuthorPostDto(23L, "John", "Smith");
    }

    public static AuthorPostDto fetchAuthorPostDtoWithoutId() {
        return new AuthorPostDto(0L, "John", "Smith");
    }

    public static AuthorPostDto fetchAuthorPostDtoWithExistId() {
        return new AuthorPostDto(2L, "Ernest", "Hemingway");
    }

    public static List<Author> fetchAuthors()
    {
        return List.of(
                Author.builder()
                        .id(23L)
                        .firstname("John")
                        .lastname("Smith")
                        .build(),
                Author.builder()
                        .id(32L)
                        .firstname("Jack")
                        .lastname("London")
                        .build()
        );
    }

        public static List<AuthorDto> fetchAuthorDtos()
        {
            return List.of(
                    new AuthorDto(23L, "John", "Smith", null),
                    new AuthorDto(32L, "Jack","London",null)
            );
        }

    public static Book fetchBookWithoutId() {
        return Book.builder()
                .title("Test Book Name")
                .author(fetchAuthorWithoutBooks())
                .build();
    }
    public static Book fetchBook() {
        return Book.builder()
                .id(23L)
                .title("Test Book Name")
                .author(fetchAuthorWithoutBooks())
                .build();
    }

    public static BookDto fetchBookDtoWithoutId() {
        return new BookDto(0L, "Test Book Name",  fetchAuthorPostDtoWithExistId());
    }

    public static BookDto fetchBookDto() {
        return new BookDto(23L, "Test Book Name",  fetchAuthorPostDto());
    }

    public static List<Book> fetchBooks()
    {
        return List.of(
                Book.builder()
                    .id(23L)
                    .title("Test Book Name 01")
                    .author(fetchAuthorWithoutBooks())
                    .build(),
                Book.builder()
                    .id(32L)
                    .title("Test Book Name 02")
                    .author(fetchAuthorWithoutBooks())
                    .build()
        );
    }

    public static List<BookDto> fetchBookDtos()
    {
        return List.of(
                new BookDto(23L, "Test Book Name 01", fetchAuthorPostDto()),
                new BookDto(32L, "Test Book Name 02",fetchAuthorPostDto())
        );
    }
}
