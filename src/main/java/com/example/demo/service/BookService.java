package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.database.model.Book;
import com.example.demo.database.repository.BookRepositorie;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepositorie bookRepositorie;

    public Book createBook(Book book) {
        return bookRepositorie.save(book);
    }

    public List<Book> getAllBooks(String author, String title, Boolean available) {
        List<Book> books = bookRepositorie.findAll();

        if (author != null) {
            books = books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .toList();
        }

        if (title != null) {
            books = books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
        }

        if (available != null && available) {
            books = books.stream()
                .filter(b -> b.getTotalCopies() > 0)
                .toList();
        }

        return books;

    }

    public Book getBookById(Long id) {
        Book book = bookRepositorie.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return book;
    }

    public Book updateBook(Long id, Book book) {
        Book b = bookRepositorie.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        b.setTitle(book.getTitle());
        b.setAuthor(book.getAuthor());
        b.setIsbn(book.getIsbn());
        b.setPublishedYear(book.getPublishedYear());
        b.setTotalCopies(book.getTotalCopies());
    
        return bookRepositorie.save(b);
    }

    public void deleteBook(Long id) {
        bookRepositorie.deleteById(id);
    }
}
