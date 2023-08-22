package com.book.library;

import com.book.library.model.Author;
import com.book.library.model.Book;
import com.book.library.repository.AuthorRepository;
import com.book.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class BookLibraryApplication
{

    public static void main(String[] args) {
        SpringApplication.run(BookLibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDemoDataIfNeeded(BookRepository repoBook, AuthorRepository repoAuthor) {
        return args -> {
            log.info("Start createDemoDataIfNeeded method");
            log.info("Check if need to renew data");
            if (repoAuthor.count() == 0 || repoBook.count() == 0) {
                log.info("Starting renew data");
                long startTime = System.currentTimeMillis();
                log.info("Double check data in tables and Truncating if not empty");
                if(repoAuthor.count() > 0) {
                    repoAuthor.deleteAll();
                }
                if(repoBook.count() > 0) {
                    repoBook.deleteAll();
                }
                Faker faker = new Faker();
                Set<Book> books = new HashSet<>();
                Set<Author> authors = new HashSet<>();
                for (int i = 0; i < 120; i++) {
                    authors.add(Author.builder()
                            .firstname(faker.name().firstName())
                            .lastname(faker.name().lastName())
                            .build());
                }
                List<Author> persistAuthors = repoAuthor.saveAll(authors);
                log.info("Generate and add to DB authors. Total count is - " + persistAuthors.size());
                for (int i = 0; i < 500; i++) {
                    books.add(Book.builder()
                            .title(faker.book().title())
                            .build());
                }
                books.forEach(it -> {
                    Author author = persistAuthors.get(new Random().nextInt(120));
                    it.setAuthor(author);
                });
                repoBook.saveAll(books);
                log.info("Generate and add to DB books. Total count is - " + books.size());
                List<Author> unusedAuthors = repoAuthor.findAll().stream()
                        .filter(it -> it.getBooks().isEmpty())
                        .collect(Collectors.toList());
                repoAuthor.deleteAll(unusedAuthors);
                log.info("Collect and remove authors which was not assigned to any book. Removed " + unusedAuthors.size() +
                        ". Total author count after reducing is - " + (persistAuthors.size() - unusedAuthors.size()));
                log.info("End createDemoDataIfNeeded method. Timecost is - " + (System.currentTimeMillis() - startTime) + "ms.");
            } else {
                log.info("No need renew data");
                log.info("Finished createDemoDataIfNeeded method");
            }
        };
    }
}
