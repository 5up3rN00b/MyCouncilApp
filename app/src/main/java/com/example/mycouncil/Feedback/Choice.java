package com.example.mycouncil.Feedback;

public class Choice {
    private String description;
    private int votes;

    public Choice(String description, int votes) {
        this.description = description;
        this.votes = votes;
    }

    public String getDescription() {
        return description;
    }

    public int getVotes() {
        return votes;
    }
}
