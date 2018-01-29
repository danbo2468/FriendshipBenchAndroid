package com.hanze.wad.friendshipbench.Models;

import com.hanze.wad.friendshipbench.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Questionnaire {

    private int id;
    private Date time;
    private boolean redflag;
    private Client client;
    private List<Answer> answers;

    public Questionnaire(JSONObject json){
        try {
            id = json.getInt("id");
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                time = formatter.parse(json.getString("timestamp"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            redflag = json.getBoolean("redflag");
            client = new Client(json.getJSONObject("client"));
            answers = new ArrayList<>();
            for (int i = 0; i < json.getJSONArray("answers").length(); i++)
                answers.add(new Answer(json.getJSONArray("answers").getJSONObject(i)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getFancyTime() {;
        DateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm");
        return dateFormat.format(time);
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
