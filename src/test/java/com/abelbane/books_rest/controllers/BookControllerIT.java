package com.abelbane.books_rest.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.abelbane.books_rest.domain.Book;
import com.abelbane.books_rest.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import static com.abelbane.books_rest.TestData.testBook;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
// enables Spring's testing support within JUnit 5, since it's what you use 
@ExtendWith(SpringExtension.class)
//clears db every after method
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) 
@Slf4j
public class BookControllerIT {
    
    @Autowired
    private MockMvc mockMvc;

    
    @Autowired
    private BookService bookService;


    @Test
    public void testThatRetrieveBookReturns484WhenBookNotFound()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/bookInformation/99287361"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatRetrieveBookReturnBookWhenExists()throws Exception{
        final Book book = testBook();  
        bookService.save(book);
        System.out.println("PRINTED "+book.getIsbn());      
        mockMvc.perform(MockMvcRequestBuilders.get("/bookInformation/"  + book.getIsbn()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatListBookReturnsHttp200WhenNoBookExists()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("[No Book Found]"));
    }

    @Test
    public void testThatListBookReturnsHttp200WhenBookExists()throws Exception{
        final Book book = testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value(book.getTitle()));
    }

    @Test
    public void testThatbookIsCreated201() throws Exception{
        final Book book = testBook();
        // final ObjectMapper objectMapper = new ObjectMapper();
        // String bookJson = objectMapper.writeValueAsString(book);
        mockMvc.perform(MockMvcRequestBuilders.put("/book/"  + book.getIsbn())
        .contentType(MediaType.APPLICATION_JSON)

        //If I'm using BODY use:
        // .content(bookJson)
        .param("author",book.getAuthor())
        .param("title", book.getTitle()))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatbookIsUpdated200() throws Exception{
        final Book book = testBook();
        bookService.save(book);

        book.setAuthor("ABEL UPDATES");
        //USE THIS FOR: 
        // final ObjectMapper objectMapper = new ObjectMapper();
        // String bookJson = objectMapper.writeValueAsString(book);
        
        

        mockMvc.perform(MockMvcRequestBuilders.put("/book/"  + book.getIsbn())
        .contentType(MediaType.APPLICATION_JSON)
        .param("author", book.getAuthor())
        .param("title", book.getTitle())
        // .content(bookJson)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    public void testThatHTTP204IsReturnedWhenBookDoesntExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/123s13"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatHTTP204IsReturnedWhenExistingBookIsDeleted() throws Exception{
        Book book = bookService.save(testBook());
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+ book.getIsbn()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
