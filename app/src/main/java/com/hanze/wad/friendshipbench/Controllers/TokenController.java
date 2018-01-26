/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Token;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is an helper class for token logic.
 */
public class TokenController {

    /**
     * Create a token model from a JSON object.
     * @param json The JSON object.
     * @return The created token model.
     */
    public static Token jsonToModel(JSONObject json){
        try {
            return new Token(json.getString("access_token"), json.getString("refresh_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
