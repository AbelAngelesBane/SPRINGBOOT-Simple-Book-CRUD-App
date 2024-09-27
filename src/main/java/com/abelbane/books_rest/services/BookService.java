package com.abelbane.books_rest.services;


import java.util.List;
import java.util.Optional;

import com.abelbane.books_rest.domain.Book;

public interface BookService {
    
    Book save(Book book);

    Boolean isBookExist(Book book);

    Optional<Book> findbyId(String isbn);

    List<Book> listBooks();

    void deleteBook(String isBn);

}
