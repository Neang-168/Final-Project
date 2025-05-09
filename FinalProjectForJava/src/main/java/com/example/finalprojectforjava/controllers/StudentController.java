package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.entities.StudentEntity;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import com.example.finalprojectforjava.services.StudentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    // Get All Users
    @GetMapping
    public List<StudentEntity> getAllStudents() {
        return studentService.getAllStudents();
    }

    //Get User By ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable Long id) {
        StudentEntity student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a New User
    @PostMapping
    public StudentEntity createStudent(@RequestBody StudentEntity student) {
        studentService.saveStudent(student);
        return student;
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<StudentEntity> updateStudent(@PathVariable Long id, @RequestBody StudentEntity studentDetails) {
        StudentEntity student = studentService.getStudentById(id);
        if (student != null) {
            student.setName(studentDetails.getName());
            student.setGender(studentDetails.getGender());
            student.setDate(studentDetails.getDate());
            student.setRemark(studentDetails.getRemark());
            studentService.saveStudent(student);
            return ResponseEntity.ok(student);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete da user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        StudentEntity student = studentService.getStudentById(id);
        if (student != null) {
            studentService.deleteStudentById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
}
