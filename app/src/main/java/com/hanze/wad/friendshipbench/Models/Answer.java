package com.hanze.wad.friendshipbench.Models;

/**
 * Created by danie on 18-Jan-18.
 */

public class Answer {
    private int id;
    private String question;
    private boolean answer;

    public Answer(int id, String question, boolean answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswer() {
        return answer;
    }
}
