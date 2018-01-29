/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Client;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is an helper class for client logic.
 */
public class ClientController {

    /**
     * Create a client model from a JSON object.
     * @param json The JSON object.
     * @return The created client model.
     */
    public static Client jsonToModel(JSONObject json){
        String healthworkerId = null;
        try {
            if(json.getString("healthWorker") != null){
                healthworkerId = json.getJSONObject("healthWorker").getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return new Client(json.getString("id"), json.getString("lastName"), json.getString("email"), json.getString("province"), json.getString("gender"), json.getString("housenumber"), json.getString("streetName"), null, json.getString("district"), json.getString("firstName"), healthworkerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
