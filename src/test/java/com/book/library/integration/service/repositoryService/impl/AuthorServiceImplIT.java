package com.book.library.integration.service.repositoryService.impl;

import com.book.library.dto.AuthorDto;
import com.book.library.exception.EntityIdNotZeroValueException;
import com.book.library.exception.EntityNotFoundException;
import com.book.library.integration.IntegrationBaseTest;
import com.book.library.model.Author;
import com.book.library.repository.AuthorRepository;
import com.book.library.service.repositoryService.impl.AuthorServiceImpl;
import com.book.library.util.TestDataGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorServiceImplIT extends IntegrationBaseTest
{
    @Autowired
    private AuthorServiceImpl service;
    @Autowired
    private AuthorRepository repo;

    @Test
    void saveNewEntityPositiveCase() {
        Author authorForSave = TestDataGeneratorUtil.fetchAuthorWithoutBooksAndId();
        assertEquals(0, authorForSave.getId());

        AuthorDto actualAuthorDto = service.save(authorForSave);

        long count = repo.count();
        assertNotNull(actualAuthorDto);
        assertEquals(count, actualAuthorDto.id());
    }

    @Test
    void saveNewEntityNegativeCase() {
        Author authorForSave = TestDataGeneratorUtil.fetchAuthorWithoutBooksAndId();
        authorForSave.setId(1);
        assertEquals(1, authorForSave.getId());

        Throwable exception = assertThrows(EntityIdNotZeroValueException.class, () -> service.save(authorForSave));

        assertNotNull(exception);
        assertEquals("Id value for new entity is 1 but should be 0.", exception.getMessage());
    }

    @Test
    void updateExistEntityPositiveCase() {
        String firstNameNew = "Qwerty";
        Author author = repo.findById(1L).orElse(null);
        assertNotNull(author);
        assertEquals(1, author.getId());
        assertNotEquals(firstNameNew, author.getFirstname());

        author.setFirstname(firstNameNew);
        AuthorDto actualAuthorDto = service.update(author);

        assertNotNull(actualAuthorDto);
        assertEquals(1, actualAuthorDto.id());
        assertEquals(firstNameNew, actualAuthorDto.firstname());
    }

    @Test
    void updateExistEntityNegativeCase() {
        Author author = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        assertEquals(23, author.getId());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.update(author));

        assertNotNull(exception);
        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void deleteEntityByIdPositiveCase() {
        Author authorToDelete = repo.findById(1L).orElse(null);
        assertNotNull(authorToDelete);
        assertEquals(1, authorToDelete.getId());

        boolean actual = service.deleteById(authorToDelete.getId());

        assertTrue(actual);
        Author authorAfterDeleting = repo.findById(1L).orElse(null);
        assertNull(authorAfterDeleting);
    }

    @Test
    void deleteEntityByIdNegativeCase() {
        Author authorToDelete = TestDataGeneratorUtil.fetchAuthorWithoutBooks();
        Author authorFromDatabase = repo.findById(authorToDelete.getId()).orElse(null);
        assertNull(authorFromDatabase);

        boolean actual = service.deleteById(authorToDelete.getId());

        assertFalse(actual);
    }

    @Test
    void getEntityByIdPositiveCase() {
        Author actualAuthor = service.getEntityById(1);

        assertNotNull(actualAuthor);
        assertEquals(1, actualAuthor.getId());
    }

    @Test
    void getEntityByIdNegativeCase() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(23));

        assertNotNull(exception);
        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void getDtoByIdPositiveCase() {
        AuthorDto actualAuthorDto = service.getDtoById(1);

        assertNotNull(actualAuthorDto);
        assertEquals(1, actualAuthorDto.id());
    }

    @Test
    void getDtoByIdNegativeCase() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(23));

        assertNotNull(exception);
        assertEquals("Absent Author entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void deleteAllEntitiesPositiveCase() {
        service.deleteAll();

        long count = repo.count();
        assertEquals(0, count);
    }

    @Test
    void findAllEntitiesPositiveCase() {
        List<Author> actualAuthors = service.findAllEntities();

        long count = repo.count();
        assertFalse(actualAuthors.isEmpty());
        assertEquals(count, actualAuthors.size());
    }

    @Test
    void findAllDtosPositiveCase() {
        List<AuthorDto> actualAuthorDtos = service.findAllDtos();

        long count = repo.count();
        assertFalse(actualAuthorDtos.isEmpty());
        assertEquals(count, actualAuthorDtos.size());
    }
}
