package com.abelbane.books_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abelbane.books_rest.domain.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String>{
    
}
