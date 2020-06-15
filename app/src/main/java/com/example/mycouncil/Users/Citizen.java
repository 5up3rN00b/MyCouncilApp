package com.example.mycouncil.Users;

public class Citizen implements User {
    private String firstName, lastName, email;
    private int zipCode;

    public Citizen(String firstName, String lastName, int zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.zipCode = zipCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getZipCode() {
        return zipCode;
    }
}
