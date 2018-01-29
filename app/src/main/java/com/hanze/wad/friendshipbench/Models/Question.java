package com.hanze.wad.friendshipbench.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Question {

    private int id;
    private String questionText;
    private boolean active;

    public Question(JSONObject json) {
        try {
            id = json.getInt("id");
            questionText = json.getString("question_text");
            active = json.getBoolean("active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean isActive() {
        return active;
    }
}
