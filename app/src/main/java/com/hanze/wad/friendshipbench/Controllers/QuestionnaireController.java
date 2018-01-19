package com.hanze.wad.friendshipbench.Controllers;
import android.util.Log;

import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.Models.Bench;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Healthworker;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import com.hanze.wad.friendshipbench.Models.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is an helper class for questionnaire logic.
 * Created by Daniel Boonstra (Friendship Bench).
 */
public class QuestionnaireController {

    /**
     * Create a summarized questionnaire model from a JSON object.
     * @param json The JSON object.
     * @return The created questionnaire model.
     */
    public static Questionnaire jsonToSummarizedModel(JSONObject json){
        try {
            return new Questionnaire(json.getInt("id"), json.getString("time"), json.getString("client_id"), json.getBoolean("redflag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a detailed questionnaire model from a JSON object.
     * @param json The JSON object.
     * @return The created questionnaire model.
     */
    public static Questionnaire jsonToDetailedModel(JSONObject json){
        try {
            JSONObject clientJson = json.getJSONObject("client");
            Client client = new Client(clientJson.getString("id"), clientJson.getString("lastName"), clientJson.getString("email"), clientJson.getString("province"), clientJson.getString("gender"), clientJson.getString("houseNumber"), clientJson.getString("streetName"), clientJson.getString("birthDay"), clientJson.getString("district"), clientJson.getString("firstName"));
            ArrayList<Answer> answers = new ArrayList();
            JSONArray answersJson = json.getJSONArray("answers");
            for (int i = 0; i < answersJson.length(); i++)
                answers.add(new Answer(answersJson.getJSONObject(i).getInt("questionId"), answersJson.getJSONObject(i).getString("question"), answersJson.getJSONObject(i).getString("answer")));
            return new Questionnaire(0, json.getString("time"), client, answers, json.getBoolean("redflag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
