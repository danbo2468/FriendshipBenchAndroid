/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Answer {
    private int id;
    private Question question;
    private boolean answer;

    public Answer(JSONObject json) {
        try {
            id = json.getInt("id");
            answer = json.getBoolean("answer");
            question = new Question(json.getJSONObject("question"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean getAnswer() {
        return answer;
    }
}
