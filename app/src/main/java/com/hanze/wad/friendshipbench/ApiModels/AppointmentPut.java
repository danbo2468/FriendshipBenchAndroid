package com.hanze.wad.friendshipbench.ApiModels;

public class AppointmentPut {
    int id;
    String time;
    int statusId;
    int benchId;
    String clientId;
    String healthworkerId;

    public AppointmentPut(int id, String time, int statusId, int benchId, String clientId, String healthworkerId) {
        this.id = id;
        this.time = time;
        this.statusId = statusId;
        this.benchId = benchId;
        this.clientId = clientId;
        this.healthworkerId = healthworkerId;
    }
}
