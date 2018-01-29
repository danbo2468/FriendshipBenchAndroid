/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Healthworker;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is an helper class for healthworker logic.
 */
public class HealthworkerController {

    /**
     * Create a healthworker model from a JSON object.
     * @param json The JSON object.
     * @return The created healthworker model.
     */
    public static Healthworker jsonToModel(JSONObject json){
        try {
            return new Healthworker(json.getString("id"), null, json.getString("phonenumber"), json.getString("email"), json.getString("gender"), json.getString("lastName"), json.getString("firstName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
