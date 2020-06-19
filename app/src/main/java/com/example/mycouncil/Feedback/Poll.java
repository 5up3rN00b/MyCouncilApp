package com.example.mycouncil.Feedback;

import java.util.ArrayList;

public class Poll {
    private String title;
    private ArrayList<Choice> choices;

    public Poll(String title, ArrayList<Choice> choices) {
        this.title = title;
        this.choices = choices;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public Choice getChoice(int index) {
        return choices.get(index);
    }

    public int getTotalVotes(){
        int total = 0;
        for (Choice c : choices){
            total += c.getVotes();
        }
        return  total;
    }
}
