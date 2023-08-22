package com.book.library.integration.service.repositoryService.impl;

import com.book.library.dto.BookDto;
import com.book.library.exception.EntityIdNotZeroValueException;
import com.book.library.exception.EntityNotFoundException;
import com.book.library.integration.IntegrationBaseTest;
import com.book.library.model.Author;
import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import com.book.library.service.repositoryService.impl.BookServiceImpl;
import com.book.library.util.TestDataGeneratorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceImplIT extends IntegrationBaseTest
{
    @Autowired
    private BookServiceImpl service;
    @Autowired
    private BookRepository repo;

    @Test
    void saveNewEntityPositiveCase() {
        Book bookForSave = TestDataGeneratorUtil.fetchBookWithoutId();
        Author author = repo.findById(1L).get().getAuthor();
        bookForSave.setAuthor(author);
        assertEquals(0, bookForSave.getId());

        BookDto actualBookDto = service.save(bookForSave);

        long count = repo.count();
        assertNotNull(actualBookDto);
        assertEquals(count, actualBookDto.id());
    }

    @Test
    void saveNewEntityNegativeCase() {
        Book bookForSave = TestDataGeneratorUtil.fetchBookWithoutId();
        Author author = repo.findById(1L).get().getAuthor();
        bookForSave.setAuthor(author);
        bookForSave.setId(1);
        assertEquals(1, bookForSave.getId());

        Throwable exception = assertThrows(EntityIdNotZeroValueException.class, () -> service.save(bookForSave));

        assertNotNull(exception);
        assertEquals("Id value for new entity is 1 but should be 0.", exception.getMessage());
    }

    @Test
    void updateExistEntityPositiveCase() {
        String titleNew = "Qwerty";
        Book book = repo.findById(1L).orElse(null);
        assertNotNull(book);
        assertEquals(1, book.getId());
        assertNotEquals(titleNew, book.getTitle());

        book.setTitle(titleNew);
        BookDto actualBookDto = service.update(book);

        assertNotNull(actualBookDto);
        assertEquals(1, actualBookDto.id());
        assertEquals(titleNew, actualBookDto.title());
    }

    @Test
    void updateExistEntityNegativeCase() {
        Book book = TestDataGeneratorUtil.fetchBook();
        assertEquals(23, book.getId());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.update(book));

        assertNotNull(exception);
        assertEquals("Absent Book entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void deleteEntityByIdPositiveCase() {
        Book bookToDelete = repo.findById(1L).orElse(null);
        assertNotNull(bookToDelete);
        assertEquals(1, bookToDelete.getId());

        boolean actual = service.deleteById(bookToDelete.getId());

        assertTrue(actual);
        Book bookAfterDeleting = repo.findById(1L).orElse(null);
        assertNull(bookAfterDeleting);
    }

    @Test
    void deleteEntityByIdNegativeCase() {
        Book bookToDelete = TestDataGeneratorUtil.fetchBook();
        Book bookFromDatabase = repo.findById(bookToDelete.getId()).orElse(null);
        assertNull(bookFromDatabase);

        boolean actual = service.deleteById(bookToDelete.getId());

        assertFalse(actual);
    }

    @Test
    void getEntityByIdPositiveCase() {
        Book actualBook = service.getEntityById(1);

        assertNotNull(actualBook);
        assertEquals(1, actualBook.getId());
    }

    @Test
    void getEntityByIdNegativeCase() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(23));

        assertNotNull(exception);
        assertEquals("Absent Book entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void getDtoByIdPositiveCase() {
        BookDto actualBookDto = service.getDtoById(1);

        assertNotNull(actualBookDto);
        assertEquals(1, actualBookDto.id());
    }

    @Test
    void getDtoByIdNegativeCase() {
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> service.getEntityById(23));

        assertNotNull(exception);
        assertEquals("Absent Book entity with id - 23 in DB.", exception.getMessage());
    }

    @Test
    void deleteAllEntitiesPositiveCase() {
        service.deleteAll();

        long count = repo.count();
        assertEquals(0, count);
    }

    @Test
    void findAllEntitiesPositiveCase() {
        List<Book> actualBooks = service.findAllEntities();

        long count = repo.count();
        assertFalse(actualBooks.isEmpty());
        assertEquals(count, actualBooks.size());
    }

    @Test
    void findAllDtosPositiveCase() {
        List<BookDto> actualBookDtos = service.findAllDtos();

        long count = repo.count();
        assertFalse(actualBookDtos.isEmpty());
        assertEquals(count, actualBookDtos.size());
    }
}
