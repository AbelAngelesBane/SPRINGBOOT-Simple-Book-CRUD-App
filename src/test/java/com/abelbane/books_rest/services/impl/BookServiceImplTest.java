package com.abelbane.books_rest.services.impl;

import static com.abelbane.books_rest.TestData.testBook;
import static com.abelbane.books_rest.TestData.testBookEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abelbane.books_rest.domain.Book;
import com.abelbane.books_rest.domain.BookEntity;
import com.abelbane.books_rest.repository.BookRepository;
import com.abelbane.books_rest.services.imp.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    //mock is standalone and doesn't involve dependency injection
    private BookRepository bookRepository;

    @InjectMocks
    //use to instantiate class testing and depends on @Mock
    private BookServiceImpl underTest;

    @Test
    public void TestBookIsSaved(){
        final Book book = Book.builder().isbn("023445678")
                        .author("Abel")
                        .title("My Book")
                        .build();
        
        final BookEntity bookEntity = BookEntity.builder().isbn("023445678")
                        .author("Abel")
                        .title("My Book")
                        .build();

        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.save(book);
        assertEquals(book, result);
    }

    //When there is no book mock!
    @Test
    public void testFindByIdWhenNoBook(){
        final String isbnId = "23234";
        when(bookRepository.findById(eq(isbnId))).thenReturn(Optional.empty());      
        final Optional<Book> result = underTest.findbyId(isbnId);
        assertEquals(Optional.empty(), result);   
    }


    @Test
    public void testFindByIdWhenBookExist(){
        final String isbnId = "023445678";
        final Book book = testBook();
        final BookEntity bookEntity = testBookEntity();
        when(bookRepository.findById(eq(isbnId))).thenReturn(Optional.of(bookEntity));      
        final Optional<Book> result = underTest.findbyId(isbnId);
        assertEquals(Optional.of(book), result);   
    }

    @Test
    public void testListAllBooksWhenEmpty(){
        //You keep forgeting you'll check ServiceImpl so use serviceImpl
        //new array diba, kase empty hahaha, what's happening to you?!!
        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());
        final List<Book> result = underTest.listBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void testListAllBooksWhenNotEmpty(){
        final BookEntity bookEntity = testBookEntity();
        //01: Repository check kung meron sa db shrue? 
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));

        //02: Check naman sa IMPL here
        final List<Book> booksList = underTest.listBooks();
        assertEquals(1,booksList.size());
    }

    @Test
    public void testReturnsFalseWhenBookDoesntExist(){
        when(bookRepository.existsById(any())).thenReturn(false);
        final Boolean result = underTest.isBookExist(testBook());
        assertEquals(false, result);
    }

    @Test
    public void testReturnTrueWhenBookExists(){
        when(bookRepository.existsById(testBook().getIsbn())).thenReturn(true);
        final Boolean result = underTest.isBookExist(testBook());
        assertEquals(true, result);
    }

    @Test
    public void testDeleteWhenBookExists(){
        String isbn = "1234";
        underTest.deleteBook(isbn);
        verify(bookRepository,times(1)).deleteById(isbn);
    }




}
