package com.example.demo.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.database.model.Book;

@Repository
public interface BookRepositorie extends JpaRepository<Book, Long> {

    Book findByAuthor(String author);
    Book findByTitle(String title);

}
