package com.hanze.wad.friendshipbench.Models;

/**
 * Created by danie on 19-Jan-18.
 */

public class Question {
    private int id;
    private String question;

    public Question(int id, String question) {
        this.id = id;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }
}
