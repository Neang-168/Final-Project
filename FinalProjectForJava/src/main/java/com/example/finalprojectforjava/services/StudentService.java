package com.example.finalprojectforjava.services;

import com.example.finalprojectforjava.entities.StudentEntity;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import com.example.finalprojectforjava.repositories.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(StudentEntity studentEntity) {
        studentRepository.save(studentEntity);
    }

    public StudentEntity getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    public long count() {
        return studentRepository.count(); // or bookRepository.count(), etc.
    }

    // Delete a student if no borrowed books exist
    public void deleteStudent(Long studentId) {
        // Retrieve the student by ID
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if the student has any borrowed books using the borrowedBookRepository instance
        if (borrowedBookRepository.existsByStudent(student)) {
            throw new RuntimeException("Cannot delete student because there are borrowed books associated with this student.");
        }

        // If no borrowed books, delete the student
        studentRepository.delete(student);
    }

}
