package com.hanze.wad.friendshipbench.ApiModels;

/**
 * Created by danie on 19-Jan-18.
 */

public class AnswerPost {
    private String answer;
    private int question_id;
    private int questionnaire_id;

    public AnswerPost(String answer, int question_id, int questionnaire_id) {
        this.answer = answer;
        this.question_id = question_id;
        this.questionnaire_id = questionnaire_id;
    }
}
