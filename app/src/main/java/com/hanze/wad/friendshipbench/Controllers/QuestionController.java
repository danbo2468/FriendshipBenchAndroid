package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Question;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is an helper class for question logic.
 * Created by Daniel Boonstra (Friendship Bench).
 */
public class QuestionController {

    /**
     * Create an question model from a JSON object.
     * @param json The JSON object.
     * @return The created appointment model.
     */
    public static Question jsonToModel(JSONObject json){
        try {
            return new Question(json.getInt("id"), json.getString("question_text"), json.getBoolean("active"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
