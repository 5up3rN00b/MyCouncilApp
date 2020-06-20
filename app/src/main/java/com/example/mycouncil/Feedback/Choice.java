package com.example.mycouncil.Feedback;

public class Choice {
    private String description;
    private int votes;
    private  boolean alreadyClicked;
    private  Integer clicked;

    public Choice(String description, int votes) {
        this.description = description;
        this.votes = votes;
        alreadyClicked = false;
    }

    public String getDescription() {
        return description;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote(){
        votes++;
    }

    public void switchBoo(int a){
        alreadyClicked = !alreadyClicked;
        clicked = a;
    }

    public boolean isAlreadyClicked() {
        return alreadyClicked;
    }

    public Integer getClicked() {
        return clicked;
    }
}
