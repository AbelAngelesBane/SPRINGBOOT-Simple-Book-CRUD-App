package com.abelbane.books_rest.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abelbane.books_rest.domain.Book;
import com.abelbane.books_rest.services.BookService;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(final BookService bookService){
        this.bookService = bookService;
    }

    @PutMapping(path = "/book/{isbn}")
    public ResponseEntity<Book> createUpdateBook(
        @PathVariable final String isbn,
        @RequestParam("author") final String author, 
        @RequestParam("title") final String title){
            Book book = new Book();
            book.setIsbn(isbn);
            book.setAuthor(author);
            book.setTitle(title);
            final Boolean isBookExist = bookService.isBookExist(book);
            final Book savedBook = bookService.save(book);

            if(isBookExist){
                return new ResponseEntity<Book>(savedBook, HttpStatus.OK);
                
            }
            else{
                return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
                
            }

        };
        

    @GetMapping(path="/bookInformation/{isbn}")
    public ResponseEntity<Book> getBookInformation(
        @PathVariable final String isbn
        ){
            final Optional<Book> foundBook = bookService.findbyId(isbn);
            return foundBook
            .map(book -> new ResponseEntity<Book>(book, HttpStatus.OK))
            .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));         
    }

    @GetMapping(path = "/books")
    public ResponseEntity<Object> listBooks(){
        final List<Book> bookList = bookService.listBooks(); 
        if (!bookList.isEmpty()) {
            return new ResponseEntity<>(bookList,HttpStatus.OK);
        }
        else return new ResponseEntity<>("[No Book Found]",HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable final String isbn){
        bookService.deleteBook(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
