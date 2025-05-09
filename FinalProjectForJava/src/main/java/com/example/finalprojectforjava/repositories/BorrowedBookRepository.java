package com.example.finalprojectforjava.repositories;

import com.example.finalprojectforjava.entities.BookEntity;
import com.example.finalprojectforjava.entities.BorrowedBookEntity;
import com.example.finalprojectforjava.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBookEntity, Long> {

    // Find all borrowed books by a specific student
    List<BorrowedBookEntity> findByStudentId(Long studentId);  // This will throw error, need to update.

    // Corrected method to use the 'student' property
    List<BorrowedBookEntity> findByStudent_Id(Long studentId);  // Correct way to reference the 'student' ID

    // Find all borrowed books by a specific book (e.g., to track all borrowings of a book)
    List<BorrowedBookEntity> findByBookId(Long bookId);

    // Find all borrowed books with 'borrowed' status
    List<BorrowedBookEntity> findByStatus(String status);

    // Check if a student has any borrowed books
    boolean existsByStudent(StudentEntity student);

    // Check if a book is borrowed by any student
    boolean existsByBook(BookEntity book);
    boolean existsByStudentId(Long studentId);
    boolean existsByBookId(Long bookId);
}
