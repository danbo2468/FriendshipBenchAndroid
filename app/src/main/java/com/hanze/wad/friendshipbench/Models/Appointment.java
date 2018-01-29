/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import com.hanze.wad.friendshipbench.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {

    private int id;
    private Date time;
    private String status;
    private Bench bench;
    private Client client;
    private Healthworker healthworker;

    public Appointment(JSONObject json){
        try {
            id = json.getInt("id");
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                time = formatter.parse(json.getString("timestamp"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            status = json.getString("status");
            bench = new Bench(json.getJSONObject("bench"));
            client = new Client(json.getJSONObject("client"));
            healthworker = new Healthworker(json.getJSONObject("healthWorker"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Date getTime(){
        return time;
    }

    public String getFancyTime() {
        DateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm");
        return dateFormat.format(time);
    }

    public String getStatus() {
        return status;
    }

    public Bench getBench() {
        return bench;
    }

    public Client getClient() {
        return client;
    }

    public Healthworker getHealthworker() {
        return healthworker;
    }

    public String getSummary(){
        return "Appointment with " + healthworker.getFullname();
    }

    public int getStatusIcon(){
        if(status.equals("PENDING"))
            return R.drawable.ic_waiting_approval;
        if(status.equals("ACCEPTED"))
            return R.drawable.ic_accepted;
        return R.drawable.ic_close;
    }

    public String getFancyStatus(){
        if(status.equals("PENDING"))
            return "Pending";
        if(status.equals("ACCEPTED"))
            return "Accepted";
        return "Cancelled";
    }
}
