package com.book.library.service.repositoryService.impl;

import com.book.library.dto.AuthorDto;
import com.book.library.exception.EntityIdNotZeroValueException;
import com.book.library.exception.EntityNotFoundException;
import com.book.library.model.Author;
import com.book.library.repository.AuthorRepository;
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
public class AuthorServiceImpl implements CRUDService<Author, AuthorDto> {
    AuthorRepository repo;
    DtoEntityMapper mapper;

    @Override
    public AuthorDto save(Author author) {
        log.info("Start save method.");
        long startTime = System.currentTimeMillis();
        if (author.getId() > 0) {
            String errorMessage = String.format("Id value for new entity is %d but should be 0.", author.getId());
            log.error(errorMessage + " Terminate execution process.");
            log.warn("End save method with ERROR.");
            throw new EntityIdNotZeroValueException(errorMessage);
        }
        AuthorDto authorDto = mapper.authorDtoToAuthor(repo.save(author));
        log.info("End save method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return authorDto;
    }

    @Override
    public AuthorDto update(Author author) {
        log.info("Start update method.");
        long startTime = System.currentTimeMillis();
        AuthorDto authorDtoUpdated;
        Optional<Author> possibleAuthor = repo.findById(author.getId());
        if(possibleAuthor.isPresent()) {
            authorDtoUpdated = mapper.authorDtoToAuthor(repo.save(author));
            log.info("End update method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        } else
        {
            String errorMessage = String.format("Absent Author entity with id - %d in DB.", author.getId());
            log.error(errorMessage + " Terminate execution process.");
            log.warn("End update method with ERROR.");
            throw new EntityNotFoundException(errorMessage);
        }
        return authorDtoUpdated;
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Start deleteById method.");
        long startTime = System.currentTimeMillis();
        Optional<Author> possibleAuthor = repo.findById(id);
        if(possibleAuthor.isPresent()) {
            repo.deleteById(id);
            log.info("End deleteById method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
            return true;
        }
        log.warn("End deleteById. Deleting data was unsuccessfully for id " + id);
        return false;
    }

    @Override
    public Author getEntityById(long id) {
        log.info("Start getEntityById method.");
        long startTime = System.currentTimeMillis();
        Author author = repo.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Absent Author entity with id - %d in DB.", id);
                    log.error(errorMessage + " Terminate execution process");
                    log.warn("End getEntityById method with ERROR");
                    return new EntityNotFoundException(errorMessage);
                });
        log.info("End getEntityById method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return author;
    }

    @Override
    public AuthorDto getDtoById(long id) throws EntityNotFoundException {
        log.info("Start getDtoById method.");
        Author author = getEntityById(id);
        AuthorDto authorDto = mapper.authorDtoToAuthor(author);
        log.info("End getDtoById method.");
        return authorDto;
    }

    @Override
    public void deleteAll() {
        log.info("Start deleteAll method.");
        long startTime = System.currentTimeMillis();
        repo.deleteAll();
        log.info("End deleteAll method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    @Override
    public List<Author> findAllEntities() {
        log.info("Start findAllEntities method.");
        long startTime = System.currentTimeMillis();
        List<Author> authors = repo.findAll();
        log.info("End findAllEntities method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
        return authors;
    }

    @Override
    public List<AuthorDto> findAllDtos() {
        log.info("Start findAllDtos method.");
        List<Author> allEntities = findAllEntities();
        List<AuthorDto> authorDtos =  allEntities.stream()
                .map(mapper::authorDtoToAuthor)
                .toList();
        log.info("End findAllDtos method.");
        return authorDtos;
    }
}