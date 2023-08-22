package com.book.library.service.repositoryService.impl;

import com.book.library.dto.BookDto;
import com.book.library.exception.EntityIdNotZeroValueException;
import com.book.library.exception.EntityNotFoundException;
import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import com.book.library.service.mapper.DtoEntityMapper;
import com.book.library.service.repositoryService.CRUDService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BookServiceImpl implements CRUDService<Book, BookDto> {
    BookRepository repo;
    DtoEntityMapper mapper;

    @Override
    public BookDto save(Book book) {
        log.info("Start save method.");
        long startTime = System.currentTimeMillis();
        if (book.getId() > 0) {
            String errorMessage = String.format("Id value for new entity is %d but should be 0.", book.getId());
            log.error(errorMessage + " Terminate execution process.");
            log.warn("End save method with ERROR.");
            throw new EntityIdNotZeroValueException(errorMessage);
        }
        BookDto bookDto = mapper.bookToBookDto(repo.save(book));
        log.info("End save method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return bookDto;
    }

    @Override
    public BookDto update(Book book) {
        log.info("Start update method.");
        long startTime = System.currentTimeMillis();
        BookDto bookDtoUpdated;
        Optional<Book> possibleBook = repo.findById(book.getId());
        if(possibleBook.isPresent()) {
            bookDtoUpdated = mapper.bookToBookDto(repo.save(book));
            log.info("End update method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        } else
        {
            String errorMessage = String.format("Absent Book entity with id - %d in DB.", book.getId());
            log.error(errorMessage + " Terminate execution process.");
            log.warn("End update method for with ERROR.");
            throw new EntityNotFoundException(errorMessage);
        }
        return bookDtoUpdated;
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Start deleteById method.");
        long startTime = System.currentTimeMillis();
        Optional<Book> possibleAuthor = repo.findById(id);
        if(possibleAuthor.isPresent()) {
            repo.deleteById(id);
            log.info("End deleteById method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
            return true;
        }
        log.warn("End deleteById. Deleting data was unsuccessfully for id " + id);
        return false;
    }

    @Override
    public Book getEntityById(long id){
        log.info("Start getEntityById method.");
        long startTime = System.currentTimeMillis();
        Book book = repo.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Absent Book entity with id - %d in DB.", id);
                    log.error(errorMessage + " Terminate execution process");
                    log.warn("End getEntityById method with ERROR");
                    return new EntityNotFoundException(errorMessage);
                });
        log.info("End getEntityById method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return book;
    }

    @Override
    public BookDto getDtoById(long id) {
        log.info("Start getDtoById method.");
        Book book = getEntityById(id);
        BookDto bookDto = mapper.bookToBookDto(book);
        log.info("End getDtoById method.");
        return bookDto;
    }

    @Override
    public void deleteAll() {
        log.info("Start deleteAll method.");
        long startTime = System.currentTimeMillis();
        repo.deleteAll();
        log.info("End deleteAll method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    @Override
    public List<Book> findAllEntities() {
        log.info("Start findAllEntities method.");
        long startTime = System.currentTimeMillis();
        List<Book> books = repo.findAll();
        log.info("End findAllEntities method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return books;
    }

    @Override
    public List<BookDto> findAllDtos() {
        log.info("Start findAllDtos method.");
        List<Book> allEntities = findAllEntities();
        List<BookDto> bookDtos =  allEntities.stream()
                .map(mapper::bookToBookDto)
                .toList();
        log.info("End findAllDtos method.");
        return bookDtos;
    }
}