package com.book.library.service.repositoryService.impl;

import com.book.library.dto.AuthorDto;
import com.book.library.exception.EntityNotFoundException;
import com.book.library.model.Author;
import com.book.library.repository.AuthorRepository;
import com.book.library.service.mapper.DtoEntityMapper;
import com.book.library.util.TestDataGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest
{
    @Mock
    private AuthorRepository repo;
    @Mock
    private DtoEntityMapper mapper;
    @InjectMocks
    private AuthorServiceImpl service;

    @Test

    void saveNewEntityPositiveCase() {
        Author authorForSave = TestDataGeneratorUtil.fetchAuthorWithoutBooksAndId();
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        AuthorDto expectedAuthorDto = TestDataGeneratorUtil.fetchAuthorDtoWithoutBooks();
        doReturn(author).when(repo).save(authorForSave);
        doReturn(expectedAuthorDto).when(mapper).authorDtoToAuthor(author);

        AuthorDto actualAuthorDto = service.save(authorForSave);

        assertEquals(expectedAuthorDto, actualAuthorDto);
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void updateExistEntityPositiveCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        AuthorDto expectedAuthorDto = TestDataGeneratorUtil.fetchAuthorDtoWithoutBooks();
        doReturn(Optional.ofNullable(author)).when(repo).findById(author.getId());
        doReturn(author).when(repo).save(author);
        doReturn(expectedAuthorDto).when(mapper).authorDtoToAuthor(author);

        AuthorDto actualAuthorDto = service.update(author);

        assertEquals(expectedAuthorDto, actualAuthorDto);
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void updateExistEntityNegativeCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        doReturn(Optional.ofNullable(null)).when(repo).findById(author.getId());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.update(author));

        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void deleteEntityByIdPositiveCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        doReturn(Optional.ofNullable(author)).when(repo).findById(author.getId());
        doNothing().when(repo).deleteById(author.getId());

        boolean actual = service.deleteById(author.getId());

        assertTrue(actual);
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void deleteEntityByIdNegativeCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        doReturn(Optional.ofNullable(null)).when(repo).findById(author.getId());

        boolean actual = service.deleteById(author.getId());

        assertFalse(actual);
        verifyNoMoreInteractions(repo, mapper);
    }


    @Test
    void getEntityByIdPositiveCase() {
        Author expectedAuthor = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        doReturn(Optional.ofNullable(expectedAuthor)).when(repo).findById(expectedAuthor.getId());

        Author actualedAuthor = service.getEntityById(expectedAuthor.getId());

        assertEquals(expectedAuthor, actualedAuthor);
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void getEntityByIdNegativeCase() {
        Author expectedAuthor = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        long id = expectedAuthor.getId();
        doReturn(Optional.ofNullable(null)).when(repo).findById(id);

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(id));

        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void getDtoByIdPositiveCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        long id = author.getId();
        AuthorDto expectedAuthorDto = TestDataGeneratorUtil.fetchAuthorDtoWithoutBooks();
        doReturn(Optional.ofNullable(author)).when(repo).findById(id);
        doReturn(expectedAuthorDto).when(mapper).authorDtoToAuthor(author);

        AuthorDto actualedAuthorDto = service.getDtoById(id);

        assertEquals(expectedAuthorDto, actualedAuthorDto);
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void getDtoByIdNegativeCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        long id = author.getId();
        AuthorDto expectedAuthorDto = TestDataGeneratorUtil.fetchAuthorDtoWithoutBooks();
        doReturn(Optional.ofNullable(null)).when(repo).findById(id);

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(id));

        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void deleteAllEntitiesPositiveCase() {
        doNothing().when(repo).deleteAll();

        service.deleteAll();

        verifyNoMoreInteractions(repo, mapper);
    }

    @Test
    void findAllEntitiesPositiveCase() {
        List<Author> expectedAuthors = TestDataGeneratorUtil.fetchAuthors();
        doReturn(expectedAuthors).when(repo).findAll();

        List<Author> actualAuthors = service.findAllEntities();

        assertEquals(expectedAuthors, actualAuthors);
        verifyNoMoreInteractions(repo, mapper);
    }


}