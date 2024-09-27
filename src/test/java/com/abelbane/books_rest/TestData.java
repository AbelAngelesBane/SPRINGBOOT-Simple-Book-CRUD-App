package com.abelbane.books_rest;

import com.abelbane.books_rest.domain.Book;
import com.abelbane.books_rest.domain.BookEntity;

public class TestData {

    private TestData(){}

    public static Book testBook(){
        final Book book = Book.builder().isbn("023445678")
                        .author("Abel")
                        .title("My Book")
                        .build();
        return book;
    }

    public static BookEntity testBookEntity(){
        final BookEntity bookeEntity = BookEntity.builder().isbn("023445678")
                        .author("Abel")
                        .title("My Book")
                        .build();
        return bookeEntity;
    }
    
}
