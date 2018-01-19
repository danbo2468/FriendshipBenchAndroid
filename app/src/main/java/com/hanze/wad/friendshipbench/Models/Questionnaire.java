package com.hanze.wad.friendshipbench.Models;

import com.hanze.wad.friendshipbench.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Questionnaire {
    private int id;
    private String time;
    private String clientId;
    private boolean redflag;
    private Client client;
    private List<Answer> answers;

    public Questionnaire(int id, String time, String clientId, boolean redflag) {
        this.id = id;
        this.time = time;
        this.clientId = clientId;
        this.redflag = redflag;
    }

    public Questionnaire(int id, String time, Client client, List<Answer> answers, boolean redflag) {
        this.id = id;
        this.time = time;
        this.client = client;
        this.answers = answers;
        this.redflag = redflag;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
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

    public String getClientId() {
        return clientId;
    }

    public Client getClient(){
        return client;
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public boolean isRedflag() {
        return redflag;
    }

    public int getRedFlagIcon(){
        return R.drawable.ic_red_flag;
    }
}
