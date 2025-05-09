/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.entities.StudentEntity;
import com.example.finalprojectforjava.repositories.BorrowedBookRepository;
import com.example.finalprojectforjava.repositories.StudentRepository;
import com.example.finalprojectforjava.services.StudentService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentPageController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;
    @Autowired
    private StudentRepository studentRepository;

    // Show the list of students
    @GetMapping("/students/page")
    public String showStudentsPage(Model model) {
        // Add the list of students to the model
        model.addAttribute("students", studentService.getAllStudents());
        return "students";  // This will load students.html
    }

    // Show the form to create a new student
    @GetMapping("/create-student")
    public String showCreateStudentPage(Model model) {
        model.addAttribute("student", new StudentEntity());  // Add an empty student entity to the model
        return "createStudent";  // This will load createStudent.html
    }

    // Handle the submission of the form and save the student
    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") StudentEntity student) {
        studentService.saveStudent(student);  // Save the student to the database
        return "redirect:/students";  // Redirect to the students list after saving
    }

    @GetMapping("/students/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        StudentEntity student = studentService.getStudentById(id);
        if (student != null) {
            model.addAttribute("student", student);
            return "updateStudent"; // Load updateStudent.html
        } else {
            return "redirect:/students"; // If student not found, redirect
        }
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute("student") StudentEntity student) {
        studentService.saveStudent(student); // Will update if ID exists
        return "redirect:/students";
    }

    @GetMapping("/students/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        StudentEntity student = studentService.getStudentById(id);
        if (student != null) {
            model.addAttribute("student", student);
            return "viewStudent"; // the name of the Thymeleaf template (viewStudent.html)
        } else {
            return "redirect:/students";
        }
    }

    @GetMapping("/students/delete/{id}")
    public String confirmDeleteStudent(@PathVariable("id") Long id, Model model) {
        StudentEntity student = studentService.getStudentById(id);
        if (student == null) {
            // Handle error: Student not found
            model.addAttribute("errorMessage", "Student not found.");
            return "students"; // Return to students page if student not found
        }
        model.addAttribute("student", student);
        return "deleteStudent"; // Proceed to delete student page
    }

    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {
        StudentEntity student = studentService.getStudentById(id);
        if (student == null) {
            model.addAttribute("errorMessage", "Student not found.");
            return "students"; // Redirect or forward to students list
        }

        boolean hasBorrowedBooks = borrowedBookRepository.existsByStudentId(id);

        if (hasBorrowedBooks) {
            model.addAttribute("errorMessage", "Cannot delete student because they have borrowed books.");
            model.addAttribute("student", student); // Add student back to the model!
            return "deleteStudent";
        }

        studentRepository.deleteById(id);
        model.addAttribute("successMessage", "Student deleted successfully.");
        model.addAttribute("student", student); // Still needed to re-show student name
        return "deleteStudent";
    }

    // Endpoint to check if a student has borrowed books
    @GetMapping("/checkBorrowedBooks/{id}")
    public ResponseEntity<Map<String, Boolean>> checkBorrowedBooks(@PathVariable Long id) {
        boolean hasBorrowedBooks = borrowedBookRepository.existsByStudent(studentService.getStudentById(id)); // Ensure this query is correct
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasBorrowedBooks", hasBorrowedBooks);
        return ResponseEntity.ok(response);
    }
}
