/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Controllers;
import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.Models.Bench;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Healthworker;
import com.hanze.wad.friendshipbench.Models.Status;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is an helper class for appointment logic.
 */
public class AppointmentController {

    /**
     * Create an appointment model from a JSON object.
     * @param json The JSON object.
     * @return The created appointment model.
     */
    public static Appointment jsonToModel(JSONObject json){
        try {
            JSONObject statusJson = json.getJSONObject("status");
            JSONObject benchJson = json.getJSONObject("bench");
            JSONObject clientJson = json.getJSONObject("client");
            JSONObject healthworkerJson = json.getJSONObject("healthworker");
            Status status = new Status(statusJson.getInt("id"), statusJson.getString("name"));
            Bench bench = new Bench(benchJson.getInt("id"), benchJson.getString("streetname"), benchJson.getString("housenumber"), benchJson.getString("province"), benchJson.getString("district"));
            Client client = new Client(clientJson.getString("id"), clientJson.getString("lastName"), clientJson.getString("email"), clientJson.getString("province"), clientJson.getString("gender"), clientJson.getString("houseNumber"), clientJson.getString("streetName"), clientJson.getString("birthDay"), clientJson.getString("district"), clientJson.getString("firstName"));
            Healthworker healthworker = new Healthworker(healthworkerJson.getString("id"), healthworkerJson.getString("birthday"), healthworkerJson.getString("phoneNumber"), healthworkerJson.getString("email"), healthworkerJson.getString("gender"), healthworkerJson.getString("lastname"), healthworkerJson.getString("firstname"));
            return new Appointment(json.getInt("id"), json.getString("time"), status, bench, client, healthworker);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
