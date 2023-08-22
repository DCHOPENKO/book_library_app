package com.book.library.controller;

import com.book.library.dto.BookDto;
import com.book.library.model.Book;
import com.book.library.service.mapper.DtoEntityMapper;
import com.book.library.service.repositoryService.impl.BookServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/book")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookController {
    BookServiceImpl service;
    DtoEntityMapper mapper;

    @PostMapping()
    public BookDto save(@RequestBody BookDto bookDto) {
        log.info("Start save method. Object for saving is -> " + bookDto.toString());
        Book book = mapper.bookDtoToBook(bookDto);
        BookDto savedBookDto = service.save(book);
        log.info("Finished save method. Saved object is -> " + savedBookDto.toString());
        return savedBookDto;
    }

    @PutMapping()
    public BookDto update(@RequestBody  BookDto bookDto) {
        log.info("Start update method. Object for updating is -> " + bookDto.toString());
        Book book = mapper.bookDtoToBook(bookDto);
        BookDto updatedBookDto = service.update(book);
        log.info("Finished update method. Updated object is -> " + updatedBookDto.toString());
        return updatedBookDto;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable(name = "id") long id) {
        log.info("Start delete by Id method. Trying delete object by id -> " + id);
        boolean isDeleted = service.deleteById(id);
        if(isDeleted) {
            log.info("Finished delete by Id method. Deleted entity with id -> " + id);
        } else {
            log.info("Finished delete by Id method. Nothing to delete, Entity with id " + id + " absent in database");
        }
        return isDeleted;
    }

    @GetMapping("/{id}")
    public BookDto find(@PathVariable(name = "id") long id) {
        log.info("Start find by Id method. Trying find object by id -> " + id);
        BookDto bookDtoById = service.getDtoById(id);
        log.info("Finished find by Id method. find object - " + bookDtoById.toString());
        return bookDtoById;
    }

    @GetMapping("/all")
    public List<BookDto> findAll() {
        log.info("Start findAll method.");
        List<BookDto> bookDtos = service.findAllDtos();
        log.info("Finished findAll method. Was found " + bookDtos.size() + " objects");
        return bookDtos;
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        log.info("Start deleteAll method.");
        service.deleteAll();
        log.info("Finished deleteAll method.");
    }
}