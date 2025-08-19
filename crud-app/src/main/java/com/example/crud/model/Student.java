package com.example.crud.model;

public class Student {
    private int id;
    private String name;
    private String email;

    // Constructors, getters, setters
    public Student() {}

    public Student(int id, String name, String email) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    // Getters and setters
}
