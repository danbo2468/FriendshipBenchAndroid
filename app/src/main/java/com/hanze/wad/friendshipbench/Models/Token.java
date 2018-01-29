/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    private String accessToken;
    private String refreshToken;

    public Token(JSONObject json) {
        try {
            accessToken = json.getString("access_token");
            refreshToken = json.getString("refresh_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
