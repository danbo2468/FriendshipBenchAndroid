package com.hanze.wad.friendshipbench.ApiModels;

/**
 * Created by danie on 19-Jan-18.
 */

public class AnswerPost {
    private boolean answer;
    private QuestionPost question;

    public AnswerPost(boolean answer, QuestionPost question) {
        this.answer = answer;
        this.question = question;
    }
}
