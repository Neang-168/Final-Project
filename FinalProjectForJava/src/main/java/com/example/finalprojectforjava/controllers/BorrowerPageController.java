/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.entities.BorrowedBookEntity;
import com.example.finalprojectforjava.services.BookService;
import com.example.finalprojectforjava.services.BorrowedBookService;
import com.example.finalprojectforjava.services.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BorrowerPageController {

    @Autowired
    private BorrowedBookService borrowedBookService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookService bookService;

    @GetMapping
    public String listBorrowers(Model model) {
        List<BorrowedBookEntity> borrowers = borrowedBookService.getAllBorrowedBooks();
        model.addAttribute("borrowers", borrowers);
        return "borrowers"; // this should match borrowers.html
    }

    // List all borrowed books
    @GetMapping("/borrowers/list")
    public String list(Model model) {
        // Logic to list borrowers
        return "borrowersListPage";
    }

    @GetMapping("/create-borrower")
    public String showCreateForm(Model model) {
        model.addAttribute("borrowedBook", new BorrowedBookEntity());
        model.addAttribute("students", studentService.getAllStudents()); // Get all students
        model.addAttribute("books", bookService.getAllBooks()); // Get all books
        return "createBorrower"; // Thymeleaf template name
    }

    @PostMapping("/borrowers/save")
    public String save(@ModelAttribute("borrowedBook") BorrowedBookEntity borrowedBook) {
        // Call the service layer to borrow a book with all necessary parameters
        borrowedBookService.borrowBook(
                borrowedBook.getStudent().getId(), // studentId
                borrowedBook.getBook().getId(), // bookId
                borrowedBook.getBorrowDate(), // borrowDate
                borrowedBook.getReturnDate(), // returnDate
                borrowedBook.getStatus() // status
        );
        return "redirect:/borrowers";  // Redirect to the list of borrowers after saving
    }

    // Show update form (for returning a book)
    @GetMapping("/borrowers/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("borrowedBook", borrowedBookService.getBorrowedBookById(id));
        model.addAttribute("students", studentService.getAllStudents()); // don't forget!
        model.addAttribute("books", bookService.getAllBooks());         // don't forget!
        return "updateBorrower";
    }

    // Mark book as returned
    @PostMapping("/borrowers/update")
    public String update(@ModelAttribute BorrowedBookEntity borrower) {
        borrowedBookService.returnBook(borrower.getId());
        return "redirect:/borrowers";
    }

    // View borrowed book details
    @GetMapping("/borrowers/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("borrower", borrowedBookService.getBorrowedBookById(id));
        return "viewBorrower";
    }

    // GET to show delete confirmation
    @GetMapping("/borrowers/delete/confirm/{id}")
    public String confirmDeleteBorrower(@PathVariable Long id, Model model) {
        model.addAttribute("borrower", borrowedBookService.getBorrowedBookById(id));
        return "deleteBorrower";
    }

// POST to perform deletion
    @PostMapping("/borrowers/delete/{id}")
    public String deleteBorrower(@PathVariable Long id) {
        borrowedBookService.deleteBorrowedBookById(id);
        return "redirect:/borrowers";
    }
}
