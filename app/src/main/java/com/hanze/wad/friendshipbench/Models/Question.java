package com.hanze.wad.friendshipbench.Models;

/**
 * Created by danie on 19-Jan-18.
 */

public class Question {
    private int id;
    private String question;
    private boolean active;

    public Question(int id, String question, boolean active) {
        this.id = id;
        this.question = question;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isActive() {
        return active;
    }
}
