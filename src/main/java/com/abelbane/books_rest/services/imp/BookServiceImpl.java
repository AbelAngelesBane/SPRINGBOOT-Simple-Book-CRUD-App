package com.abelbane.books_rest.services.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.abelbane.books_rest.domain.Book;
import com.abelbane.books_rest.domain.BookEntity;
import com.abelbane.books_rest.repository.BookRepository;
import com.abelbane.books_rest.services.BookService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(final BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(final Book book) { 
        final BookEntity bookEntity = bookToBookEntity(book); 
        //to/save db
        final BookEntity savedBookEntity = bookRepository.save(bookEntity);  
        return bookEntityToBook(savedBookEntity);
    }

    private BookEntity bookToBookEntity(final Book book){
        return BookEntity.builder() 
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }

    private Book bookEntityToBook(final BookEntity bookEntity){
        return Book.builder()
                    .isbn(bookEntity.getIsbn())
                    .title(bookEntity.getTitle())
                    .author(bookEntity.getAuthor())
                    .build();
    }

    @Override
    public Optional<Book> findbyId(String isbn) {
        final Optional<BookEntity> bookEntity = bookRepository.findById(isbn); 
        return bookEntity.map(book -> bookEntityToBook(book));
    }

    @Override
    public List<Book> listBooks() {
       final List<BookEntity> listBookEntity = bookRepository.findAll();
       return listBookEntity.stream().map(book -> bookEntityToBook(book)).toList(); 
        }

    @Override
    public Boolean isBookExist(Book book) {
        return bookRepository.existsById(book.getIsbn());
    }


    @Override
    //I didn't get an empty exception but I'll still put it here for future reference
    public void deleteBook(String isBn) {
        try {
            bookRepository.deleteById(isBn);
        } catch (final EmptyResultDataAccessException e) {
            log.debug("Attempted to delete" + e);
        }
    }
            
}
