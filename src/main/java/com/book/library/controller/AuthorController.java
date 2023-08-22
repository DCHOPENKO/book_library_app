package com.book.library.controller;

import com.book.library.dto.AuthorDto;
import com.book.library.dto.AuthorPostDto;
import com.book.library.model.Author;
import com.book.library.service.mapper.DtoEntityMapper;
import com.book.library.service.repositoryService.impl.AuthorServiceImpl;
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
@RequestMapping(value = "/author")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthorController
{
    AuthorServiceImpl service;
    DtoEntityMapper mapper;

    @PostMapping()
    public AuthorDto save(@RequestBody AuthorPostDto authorPostDto) {
        log.info("Start save method. Object for saving is -> " + authorPostDto.toString());
        Author author = mapper.authorPostDtoToAuthor(authorPostDto);
        AuthorDto savedInstance = service.save(author);
        log.info("Finished save method. Saved object is -> " + savedInstance.toString());
        return savedInstance;
    }

    @PutMapping()
    public AuthorDto update(@RequestBody  AuthorDto authorDto) {
        log.info("Start update method. Object for updating is -> " + authorDto.toString());
        Author author = mapper.authorDtoToAuthor(authorDto);
        AuthorDto updatedInstance = service.update(author);
        log.info("Finished update method. Updated object is -> " + updatedInstance.toString());
        return updatedInstance;
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
    public AuthorDto find(@PathVariable(name = "id") long id) {
        log.info("Start find by Id method. Trying find object by id -> " + id);
        AuthorDto authorDtoById = service.getDtoById(id);
        log.info("Finished find by Id method. find object - " + authorDtoById.toString());
        return authorDtoById;
    }

    @GetMapping("/all")
    public List<AuthorDto> findAll() {
        log.info("Start findAll method.");
        List<AuthorDto> authorDtos = service.findAllDtos();
        log.info("Finished findAll method. Was found " + authorDtos.size() + " objects");
        return authorDtos;
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        log.info("Start deleteAll method.");
        service.deleteAll();
        log.info("Finished deleteAll method.");
    }
}