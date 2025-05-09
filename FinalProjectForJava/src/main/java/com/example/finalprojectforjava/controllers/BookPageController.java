/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.entities.BookEntity;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import com.example.finalprojectforjava.services.BookService;
import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    // Show the list of books
    @GetMapping("/books/page")
    public String showBooksPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";  // This will load books.html
    }

    // Show the form to create a new book
    @GetMapping("/create-book")
    public String showCreateBookPage(Model model) {
        model.addAttribute("book", new BookEntity());  // Add an empty book entity
        return "createBook";  // This will load createBook.html
    }

    // Handle the submission of the form and save the book
    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") BookEntity book) {
        bookService.saveBook(book);  // Save the book to the database
        return "redirect:/books/page";  // Redirect to the books list after saving
    }

    // Show the form to update a book
    @GetMapping("/books/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "updateBook";  // Load updateBook.html
        } else {
            return "redirect:/books/page";  // If book not found, redirect to books page
        }
    }

    // Handle the submission of the updated book
    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("book") BookEntity book) {
        bookService.saveBook(book);  // Save the book (will update if ID exists)
        return "redirect:/books/page";  // Redirect to the books list after updating
    }

    // View details of a book
    @GetMapping("/books/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "viewBook";  // Load viewBook.html
        } else {
            return "redirect:/books/page";  // If book not found, redirect to books page
        }
    }

    // Confirm delete book
    @GetMapping("/books/delete/{id}")
    public String confirmDeleteBook(@PathVariable("id") Long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "deleteBook";  // Load deleteBook.html
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);  // Delete the book by its ID
        return "redirect:/books";  // Redirect to the books list after deletion
    }

    @GetMapping("/checkBorrowedBooksByBook/{bookId}")
    public ResponseEntity<Map<String, Boolean>> checkBorrowedBooksByBook(@PathVariable Long bookId) {
        boolean bookIsBorrowed = borrowedBookRepository.existsByBookId(bookId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("bookIsBorrowed", bookIsBorrowed);
        return ResponseEntity.ok(response);
    }
}
