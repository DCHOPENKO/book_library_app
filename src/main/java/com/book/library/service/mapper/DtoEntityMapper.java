package com.book.library.service.mapper;

import com.book.library.dto.AuthorDto;
import com.book.library.dto.AuthorPostDto;
import com.book.library.dto.BookDto;
import com.book.library.model.Author;
import com.book.library.model.Book;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DtoEntityMapper {
    Author authorPostDtoToAuthor (AuthorPostDto authorPostDto);
    AuthorPostDto authorToAuthorPostDto (Author author);
    Author authorDtoToAuthor(AuthorDto authorDto);
    AuthorDto authorDtoToAuthor(Author author);
    Book bookDtoToBook (BookDto bookDto);
    BookDto bookToBookDto (Book book);
}
