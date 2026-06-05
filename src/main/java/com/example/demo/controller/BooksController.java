package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.model.Book;
import com.example.demo.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book saveBook = bookService.createBook(book);

        URI location = URI.create("/api/books/" + saveBook.getId());
        return ResponseEntity.created(location).body(saveBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
        @RequestParam(required = false) String author, 
        @RequestParam(required = false) String title, 
        @RequestParam(required = false) Boolean available) {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {

        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PutMapping("/{id}") 
    public ResponseEntity<Book> updateBook(@PathVariable Long id, Book book) {
        var b = bookService.updateBook(id, book);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}