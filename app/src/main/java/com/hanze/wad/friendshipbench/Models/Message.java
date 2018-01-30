/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String id;
    private String userId;
    private String userName;
    private String message;
    private Date time;
    private String room;

    public Message(JSONObject json){
        try {
            id = json.getString("id");
            userId = json.getString("user_id");
            userName = json.getString("user_name");
            message = json.getString("message");
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                time = formatter.parse(json.getString("time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            room = json.getString("room");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getFancyDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(time);
    }

    public String getFancyTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(time);
    }

    public String getFancyDateTime(){
        return getFancyDate() + " " + getFancyTime();
    }
}
