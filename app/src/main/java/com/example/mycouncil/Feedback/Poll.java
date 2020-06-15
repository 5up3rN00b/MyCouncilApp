package com.example.mycouncil.Feedback;

import java.util.ArrayList;

public class Poll {
    private String question;
    private ArrayList<String> choices;

    public Poll(String question, ArrayList<String> choices) {
        this.question = question;
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }
}
