package com.example.crud.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crud.model.Student;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private int currentId = 1;

    public List<Student> getAll() {
        return students;
    }

    public void add(Student student) {
        student.setId(currentId++);
        students.add(student);
    }

    public Student getById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public void update(Student updatedStudent) {
        Student existing = getById(updatedStudent.getId());
        if (existing != null) {
            existing.setName(updatedStudent.getName());
            existing.setEmail(updatedStudent.getEmail());
        }
    }

    public void delete(int id) {
        students.removeIf(s -> s.getId() == id);
    }
}
