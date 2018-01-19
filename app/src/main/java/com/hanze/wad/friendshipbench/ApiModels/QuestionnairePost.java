package com.hanze.wad.friendshipbench.ApiModels;

/**
 * Created by danie on 19-Jan-18.
 */

public class QuestionnairePost {
    private String client_id;
    private String time;
    private boolean redflag;

    public QuestionnairePost(String client_id, String time, boolean redflag) {
        this.client_id = client_id;
        this.time = time;
        this.redflag = redflag;
    }
}
