package com.example.mycouncil.Feedback;

public class Post {
    private String title, description;
    private int upvotes, downvotes;

    public Post(String title, String description, int upvotes, int downvotes) {
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void addUpvotes(){
        upvotes++;
    }

    public void addDownvotes(){
        downvotes++;
    }
    public int getTotalvotes(){
        return upvotes - downvotes;
    }
}
