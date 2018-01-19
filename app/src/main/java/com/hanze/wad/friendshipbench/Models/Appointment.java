package com.hanze.wad.friendshipbench.Models;

import com.hanze.wad.friendshipbench.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Appointment {

    private int id;
    private String time;
    private Status status;
    private Bench bench;
    private Client client;
    private Healthworker healthworker;

    public Appointment(int id, String time, Status status, Bench bench, Client client, Healthworker healthworker) {
        this.id = id;
        this.time = time;
        this.status = status;
        this.bench = bench;
        this.client = client;
        this.healthworker = healthworker;
    }

    public int getId() {
        return id;
    }

    public String getTime(){
        return time;
    }

    public String getReadableTime() {
        Date timeString;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            timeString = formatter.parse(this.time);
            DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm");
            return df.format(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Status getStatus() {
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
        return "Appointment with " + healthworker.getFullName();
    }

    public int getStatusIcon(){
        if(status.getId() == 1)
            return R.drawable.ic_waiting_approval;
        if(status.getId() == 2)
            return R.drawable.ic_accepted;
        return R.drawable.ic_close;
    }
}
