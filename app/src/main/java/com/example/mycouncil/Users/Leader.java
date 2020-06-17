package com.example.mycouncil.Users;

public class Leader implements User {
    private String email, firstName, lastName, branch;
    private int id, zipCode;

    public Leader(String email, String firstName, String lastName, String branch, int id, int zipCode) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
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

    public String getBranch() {
        return branch;
    }

    public int getId() {
        return id;
    }

    public int getZipCode() {
        return zipCode;
    }
}
