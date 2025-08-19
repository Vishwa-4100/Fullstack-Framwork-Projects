package com.example.excelhandler.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_email", columnList = "email", unique = true)
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;

    private double salary;

    private LocalDateTime inTime;
    private LocalDateTime outTime;

    public Employee() {}

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public LocalDateTime getInTime() { return inTime; }
    public void setInTime(LocalDateTime inTime) { this.inTime = inTime; }

    public LocalDateTime getOutTime() { return outTime; }
    public void setOutTime(LocalDateTime outTime) { this.outTime = outTime; }
}
