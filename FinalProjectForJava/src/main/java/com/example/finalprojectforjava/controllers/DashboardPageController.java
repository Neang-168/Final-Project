/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.finalprojectforjava.controllers;

import com.example.finalprojectforjava.services.BookService;
import com.example.finalprojectforjava.services.BorrowedBookService;
import com.example.finalprojectforjava.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardPageController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowedBookService borrowedBookService;

    @GetMapping("/dashboard/page")
    public String dashboard(Model model) {
        model.addAttribute("studentCount", studentService.count());
        model.addAttribute("bookCount", bookService.count());
        model.addAttribute("borrowerCount", borrowedBookService.count());
        return "dashboard";  // Name of your Thymeleaf template
    }
}
