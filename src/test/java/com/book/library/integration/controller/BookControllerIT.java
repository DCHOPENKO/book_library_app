package com.book.library.integration.controller;

import com.book.library.dto.BookDto;
import com.book.library.integration.IntegrationBaseTest;
import com.book.library.model.Book;
import com.book.library.repository.BookRepository;
import com.book.library.service.mapper.DtoEntityMapper;
import com.book.library.util.TestDataGeneratorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class BookControllerIT extends IntegrationBaseTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository repo;
    @Autowired
    private DtoEntityMapper mapper;

    @Test
    void saveBookPositiveCase() throws Exception {
        BookDto bookDtoToAdd = TestDataGeneratorUtil.fetchBookDtoWithoutId();

        String json = objectMapper.writeValueAsString(bookDtoToAdd);
        String response = mockMvc.perform(post("/book")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        BookDto bookDtoFromResponse = objectMapper.readValue(response, BookDto.class);

        assertEquals(bookDtoToAdd.title(), bookDtoFromResponse.title());
        assertNotEquals(bookDtoToAdd.id(), bookDtoFromResponse.id());
        assertEquals(10, bookDtoFromResponse.id());
    }

    @Test
    void saveBookNegativeCase() throws Exception {
        Book requestBook = repo.findById(1L).orElse(null);
        assertNotNull(requestBook);
        BookDto requestBookDto = mapper.bookToBookDto(requestBook);

        String json = objectMapper.writeValueAsString(requestBookDto);
        mockMvc.perform(post("/book")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void updateBookPositiveCase() throws Exception {
        String updatedTitle = "Qwerty";
        Book existBook = repo.findById(1L).orElse(null);
        assertNotNull(existBook);
        assertNotEquals(updatedTitle, existBook.getTitle());
        existBook.setTitle(updatedTitle);
        BookDto bookDtoForRequest = mapper.bookToBookDto(existBook);

        String json = objectMapper.writeValueAsString(bookDtoForRequest);
        String response = mockMvc.perform(put("/book")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        BookDto bookDtoFromResponse = objectMapper.readValue(response, BookDto.class);

        assertEquals(bookDtoForRequest.title(), bookDtoFromResponse.title());
        assertEquals(bookDtoForRequest.id(), bookDtoFromResponse.id());
    }

    @Test
    void updateBookNegativeCase() throws Exception {
        BookDto bookDto = TestDataGeneratorUtil.fetchBookDto();
        assertTrue(bookDto.id() > repo.count());

        String json = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(put("/book")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void deleteBookPositiveCase() throws Exception {
        Book existBook = repo.findById(1L).orElse(null);
        assertNotNull(existBook);

        String response = mockMvc.perform(delete("/author/{id}", existBook.getId()))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        boolean actualResult = Boolean.parseBoolean(response);

        assertTrue(actualResult);
    }

    @Test
    void deleteBookNegativeCase() throws Exception {
        long fakeId = 23L;
        Optional<Book> possibleBook = repo.findById(fakeId);
        assertFalse(possibleBook.isPresent());

        String response = mockMvc.perform(delete("/book/{id}", fakeId))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        boolean actualResult = Boolean.parseBoolean(response);

        assertFalse(actualResult);
    }

    @Test
    void getBookDtoPositiveCase() throws Exception {
        Book existBook = repo.findById(1L).orElse(null);
        assertNotNull(existBook);

        String response = mockMvc.perform(get("/book/{id}", existBook.getId()))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        BookDto bookDtoFromResponse = objectMapper.readValue(response, BookDto.class);

        assertNotNull(bookDtoFromResponse);
        assertEquals(existBook.getId(), bookDtoFromResponse.id());
        assertEquals(existBook.getTitle(), bookDtoFromResponse.title());
    }

    @Test
    void getBookDtoNegativeCase() throws Exception {
        long fakeId = 23L;
        Optional<Book> possibleBook = repo.findById(fakeId);
        assertFalse(possibleBook.isPresent());

        mockMvc.perform(get("/book/{id}", fakeId))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void getAllBookDtosPositiveCase() throws Exception {
        String response = mockMvc.perform(get("/book/all"))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        BookDto[] bookDtos = objectMapper.readValue(response, BookDto[].class);

        assertTrue(bookDtos.length > 0);
        assertEquals(repo.count(), bookDtos.length);
    }

    @Test
    void deleteAllBooksPositiveCase() throws Exception {
        long countBeforeDeleting = repo.count();
        assertTrue(countBeforeDeleting > 0);

        mockMvc.perform(delete("/book/all"))
                .andExpectAll(status().is2xxSuccessful());
        long countAfterDeleting = repo.count();

        assertEquals(0, countAfterDeleting);
    }
}
