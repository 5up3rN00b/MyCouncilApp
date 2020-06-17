package com.example.mycouncil.Users;

public class Citizen implements User {
    private String firstName, lastName, email;
    private int id, zipCode;

    public Citizen(String email, String firstName, String lastName, int id, int zipCode) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public int getZipCode() {
        return zipCode;
    }
}
