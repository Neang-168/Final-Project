
package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.entities.BorrowedBookEntity;
import com.example.finalprojectforjava.services.BorrowedBookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrowed-books")
public class BorrowedBookController {

    
    @Autowired
    public BorrowedBookController(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    @GetMapping("/borrowers")
    public String listBorrowers(Model model) {
        List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooks();
        model.addAttribute("borrowedBooks", borrowedBooks);  // Passing the data to the view
        return "borrowers"; // This corresponds to your Thymeleaf template
    }
    @Autowired
    private BorrowedBookService borrowedBookService;

    // ✅ Borrow a book
//    @PostMapping("/borrow")
//public ResponseEntity<BorrowedBookEntity> borrowBook(@RequestParam Long studentId, @RequestParam Long bookId) {
//    try {
//        // Make sure your service method only expects studentId and bookId
//        BorrowedBookEntity borrowedBook = borrowedBookService.borrowBook(studentId, bookId);
//        return new ResponseEntity<>(borrowedBook, HttpStatus.CREATED);
//    } catch (RuntimeException e) {
//        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//    }
//}

    // ✅ Return a book
    @PostMapping("/return/{borrowedBookId}")
    public ResponseEntity<BorrowedBookEntity> returnBook(@PathVariable Long borrowedBookId) {
        try {
            BorrowedBookEntity returnedBook = borrowedBookService.returnBook(borrowedBookId);
            return new ResponseEntity<>(returnedBook, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ Get all borrowed books by a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<BorrowedBookEntity>> getBorrowedBooksByStudent(@PathVariable Long studentId) {
        List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getBorrowedBooksByStudent(studentId);
        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }

    // ✅ Get all borrowed books
    @GetMapping
    public ResponseEntity<List<BorrowedBookEntity>> getAllBorrowedBooks() {
        List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getAllBorrowedBooks();
        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }

    // ✅ Get borrowed books by status (e.g., "borrowed", "returned")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowedBookEntity>> getBorrowedBooksByStatus(@PathVariable String status) {
        List<BorrowedBookEntity> borrowedBooks = borrowedBookService.getBorrowedBooksByStatus(status);
        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }
}
