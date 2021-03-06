package com.example.mycouncil.Feedback;

public class Post {
    private String title, description, branch;
    private int userId, postId, upvotes, downvotes;

    public Post(String title, String description, String branch, int userId, int postId, int upvotes, int downvotes) {
        this.title = title;
        this.description = description;
        this.branch = branch;
        this.userId = userId;
        this.postId = postId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getBranch() {
        return branch;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
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

    public void subUpvotes(){
        upvotes--;
    }

    public void addDownvotes(){
        downvotes++;
    }

    public void subDownvotes(){
        downvotes--;
    }
    public int getTotalVotes(){
        return upvotes - downvotes;
    }
}
