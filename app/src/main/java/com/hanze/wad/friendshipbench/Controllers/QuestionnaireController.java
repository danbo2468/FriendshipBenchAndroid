package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            return new Questionnaire(json.getInt("id"), json.getString("timestamp"), json.getJSONObject("client").getString("id"), json.getBoolean("redflag"));
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
            Client client = new Client(clientJson.getString("id"), clientJson.getString("lastName"), clientJson.getString("email"), clientJson.getString("province"), clientJson.getString("gender"), clientJson.getString("housenumber"), clientJson.getString("streetName"), null, clientJson.getString("district"), clientJson.getString("firstName"), clientJson.getJSONObject("healthWorker").getString("id"));
            ArrayList<Answer> answers = new ArrayList();
            JSONArray answersJson = json.getJSONArray("answers");
            for (int i = 0; i < answersJson.length(); i++)
                answers.add(new Answer(answersJson.getJSONObject(i).getJSONObject("question").getInt("id"), answersJson.getJSONObject(i).getJSONObject("question").getString("question_text"), answersJson.getJSONObject(i).getBoolean("answer")));
            return new Questionnaire(json.getInt("id"), json.getString("timestamp"), client, answers, json.getBoolean("redflag"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
