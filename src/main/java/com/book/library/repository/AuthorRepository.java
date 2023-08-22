package com.book.library.repository;

import com.book.library.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    @EntityGraph(attributePaths = {"books"})
    Optional<Author> findById(Long aLong);

    @Override
    @EntityGraph(attributePaths = {"books"})
    List<Author> findAll();
}
