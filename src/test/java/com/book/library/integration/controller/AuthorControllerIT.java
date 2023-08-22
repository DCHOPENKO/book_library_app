package com.book.library.integration.controller;

import com.book.library.dto.AuthorDto;
import com.book.library.dto.AuthorPostDto;
import com.book.library.integration.IntegrationBaseTest;
import com.book.library.model.Author;
import com.book.library.repository.AuthorRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthorControllerIT extends IntegrationBaseTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorRepository repo;
    @Autowired
    private DtoEntityMapper mapper;

    @Test
    void saveAuthorPositiveCase() throws Exception {
        AuthorPostDto authorPostDto = TestDataGeneratorUtil.fetchAuthorPostDtoWithoutId();

        String json = objectMapper.writeValueAsString(authorPostDto);
        String response = mockMvc.perform(post("/author")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        AuthorDto authorDto = objectMapper.readValue(response, AuthorDto.class);

        assertEquals(authorPostDto.firstname(), authorDto.firstname());
        assertEquals(authorPostDto.lastname(), authorDto.lastname());
        assertNotEquals(authorPostDto.id(), authorDto.id());
        assertEquals(4, authorDto.id());
    }

    @Test
    void saveAuthorNegativeCase() throws Exception {
        Author requestAuthor = repo.findById(1L).orElse(null);
        assertNotNull(requestAuthor);
        AuthorPostDto authorPostDto = mapper.authorToAuthorPostDto(requestAuthor);

        String json = objectMapper.writeValueAsString(authorPostDto);
        mockMvc.perform(post("/author")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void updateAuthorPositiveCase() throws Exception {
        String firstNameNew = "Qwerty";
        Author requestAuthor = repo.findById(1L).orElse(null);
        assertNotNull(requestAuthor);
        assertNotEquals(firstNameNew, requestAuthor.getFirstname());
        requestAuthor.setFirstname(firstNameNew);
        AuthorDto requestAuthorDto = mapper.authorDtoToAuthor(requestAuthor);

        String json = objectMapper.writeValueAsString(requestAuthorDto);
        String response = mockMvc.perform(put("/author")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        AuthorDto responseAuthorDto = objectMapper.readValue(response, AuthorDto.class);

        assertEquals(requestAuthorDto.firstname(), responseAuthorDto.firstname());
        assertEquals(requestAuthorDto.lastname(), responseAuthorDto.lastname());
        assertEquals(requestAuthorDto.id(), responseAuthorDto.id());
    }

    @Test
    void updateAuthorNegativeCase() throws Exception {
        AuthorDto authorDto = TestDataGeneratorUtil.fetchAuthorDtoWithoutBooks();
        assertTrue(authorDto.id() > repo.count());

        String json = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(put("/author")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void deleteAuthorPositiveCase() throws Exception {
        Author existAuthor = repo.findById(1L).orElse(null);
        assertNotNull(existAuthor);

        String response = mockMvc.perform(delete("/author/{id}", existAuthor.getId()))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        boolean actualResult = Boolean.parseBoolean(response);

        assertTrue(actualResult);
    }

    @Test
    void deleteAuthorNegativeCase() throws Exception {
        long fakeId = 23L;
        Optional<Author> possibleAuthor = repo.findById(fakeId);
        assertFalse(possibleAuthor.isPresent());

        String response = mockMvc.perform(delete("/author/{id}", fakeId))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        boolean actualResult = Boolean.parseBoolean(response);

        assertFalse(actualResult);
    }

    @Test
    void getAuthorDtoPositiveCase() throws Exception {
        Author existAuthor = repo.findById(1L).orElse(null);
        assertNotNull(existAuthor);

        String response = mockMvc.perform(get("/author/{id}", existAuthor.getId()))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        AuthorDto responseAuthorDto = objectMapper.readValue(response, AuthorDto.class);

        assertNotNull(responseAuthorDto);
        assertEquals(existAuthor.getId(), responseAuthorDto.id());
        assertEquals(existAuthor.getFirstname(), responseAuthorDto.firstname());
        assertEquals(existAuthor.getLastname(), responseAuthorDto.lastname());
    }

    @Test
    void getAuthorDtoNegativeCase() throws Exception {
        long fakeId = 23L;
        Optional<Author> possibleAuthor = repo.findById(fakeId);
        assertFalse(possibleAuthor.isPresent());

        mockMvc.perform(get("/author/{id}", fakeId))
                .andExpectAll(status().is5xxServerError());
    }

    @Test
    void getAllAuthorDtosPositiveCase() throws Exception {
        String response = mockMvc.perform(get("/author/all"))
                .andExpectAll(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        AuthorDto[] authorDto = objectMapper.readValue(response, AuthorDto[].class);

        assertTrue(authorDto.length > 0);
        assertEquals(repo.count(), authorDto.length);
    }

    @Test
    void deleteAllAuthorDtosPositiveCase() throws Exception {
        long countBeforeDeleting = repo.count();
        assertTrue(countBeforeDeleting > 0);

        mockMvc.perform(delete("/author/all"))
                .andExpectAll(status().is2xxSuccessful());
        long countAfterDeleting = repo.count();

        assertEquals(0, countAfterDeleting);
    }
}
