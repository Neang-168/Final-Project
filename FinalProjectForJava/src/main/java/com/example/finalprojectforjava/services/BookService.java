/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.finalprojectforjava.services;

import com.example.finalprojectforjava.entities.BookEntity;
import com.example.finalprojectforjava.repositories.BookRepository;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import java.awt.print.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    // ✅ Add a new book
    public BookEntity addBook(BookEntity book) {
        if (book.getAvailable() == null) {
            book.setAvailable(book.getTotal());  // Set available = total if null
        }

        if (book.getAvailable() > book.getTotal()) {
            throw new RuntimeException("Available copies cannot exceed total copies.");
        }

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("ISBN already exists.");
        }

        return bookRepository.save(book);
    }

    public void saveBook(BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    // ✅ Get all books
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    // ✅ Get book by ID
    public BookEntity getBookById(Long id) {  // Changed to Long to match BookEntity's id type
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    // ✅ Update a book
    public BookEntity updateBook(Long id, BookEntity book) {  // Changed to Long to match BookEntity's id type
        BookEntity existing = getBookById(id);

        if (book.getAvailable() == null) {
            book.setAvailable(book.getTotal());  // Set available = total if null
        }

        if (book.getAvailable() > book.getTotal()) {
            throw new RuntimeException("Available copies cannot exceed total copies.");
        }

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setTotal(book.getTotal());
        existing.setAvailable(book.getAvailable());
        existing.setCategory(book.getCategory());
        existing.setRemark(book.getRemark());

        return bookRepository.save(existing);
    }

    // ✅ Delete a book
    // Delete a book if no students have borrowed it
    public void deleteBook(Long bookId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the book is borrowed
        if (borrowedBookRepository.existsByBook(book)) {
            throw new RuntimeException("Cannot delete book because it is currently borrowed.");
        }

        // If not borrowed, delete the book
        bookRepository.delete(book);
    }

    public long count() {
        return bookRepository.count();  // Directly count the number of books in the database
    }
}
