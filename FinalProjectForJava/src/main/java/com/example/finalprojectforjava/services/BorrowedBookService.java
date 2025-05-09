package com.example.finalprojectforjava.services;

import com.example.finalprojectforjava.entities.BookEntity;
import com.example.finalprojectforjava.entities.BorrowedBookEntity;
import com.example.finalprojectforjava.entities.StudentEntity;
import com.example.finalprojectforjava.repositories.BookRepository;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import com.example.finalprojectforjava.repositories.StudentRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowedBookService {

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    public BorrowedBookService(BorrowedBookRepository borrowedBookRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
    }

    public List<BorrowedBookEntity> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll(); // Fetch all borrowed books from the database
    }

    // ✅ Borrow a book
    public BorrowedBookEntity borrowBook(Long studentId, Long bookId, LocalDate borrowDate, LocalDate returnDate, String status) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        if (book.getAvailable() <= 0) {
            throw new RuntimeException("Book is not available for borrowing.");
        }

        BorrowedBookEntity borrowedBook = new BorrowedBookEntity();
        borrowedBook.setStudent(student);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(borrowDate);
        borrowedBook.setReturnDate(returnDate);
        borrowedBook.setStatus(status);

        book.setAvailable(book.getAvailable() - 1); // Decrement available copies

        // Save borrowed book and update book availability
        bookRepository.save(book);
        return borrowedBookRepository.save(borrowedBook);
    }

    // ✅ Return a book
    public BorrowedBookEntity returnBook(Long borrowedBookId) {
        BorrowedBookEntity borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new RuntimeException("Borrowed Book not found with ID: " + borrowedBookId));

        if ("returned".equals(borrowedBook.getStatus())) {
            throw new RuntimeException("This book has already been returned.");
        }

        borrowedBook.setReturnDate(LocalDate.now());
        borrowedBook.setStatus("returned");

        BookEntity book = borrowedBook.getBook();
        book.setAvailable(book.getAvailable() + 1); // Increment available copies

        bookRepository.save(book);
        return borrowedBookRepository.save(borrowedBook);
    }

    // ✅ Get all borrowed books by student
    public List<BorrowedBookEntity> getBorrowedBooksByStudent(Long studentId) {
        return borrowedBookRepository.findByStudentId(studentId);
    }

//    // ✅ Get all borrowed books
//    public List<BorrowedBookEntity> getAllBorrowedBooks() {
//        return borrowedBookRepository.findAll();
//    }
    // ✅ Get borrowed books by status (e.g., "borrowed", "returned")
    public List<BorrowedBookEntity> getBorrowedBooksByStatus(String status) {
        return borrowedBookRepository.findByStatus(status);
    }

    // ✅ Get a single borrowed book by ID
    public BorrowedBookEntity getBorrowedBookById(Long id) {
        return borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowed book not found with ID: " + id));
    }

// ✅ Delete a borrowed book by ID
    public void deleteBorrowedBookById(Long id) {
        BorrowedBookEntity borrowedBook = borrowedBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrowed book not found with ID: " + id));

        // Optionally increase available book count if status is still 'borrowed'
        if ("borrowed".equals(borrowedBook.getStatus())) {
            BookEntity book = borrowedBook.getBook();
            book.setAvailable(book.getAvailable() + 1);
            bookRepository.save(book);
        }

        borrowedBookRepository.deleteById(id);
    }

    public long count() {
        return borrowedBookRepository.count();  // Directly count the number of borrowed books
    }

}
