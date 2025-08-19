package com.example.crud.controller;

import com.example.crud.model.Student;
import com.example.crud.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("students", service.getAll());
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("student", new Student());
        return "add";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
        service.add(student);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("student", service.getById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String editStudent(@ModelAttribute Student student) {
        service.update(student);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        service.delete(id);
        return "redirect:/";
    }
}
