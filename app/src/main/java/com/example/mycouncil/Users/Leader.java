package com.example.mycouncil.Users;

public class Leader implements User {
    private String firstName, lastName, branch;
    private int zipCode;

    public Leader(String firstName, String lastName, String branch, int zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
        this.zipCode = zipCode;
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

    public int getZipCode() {
        return zipCode;
    }
}
