package com.hanze.wad.friendshipbench.ApiModels;

import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 19-Jan-18.
 */

public class QuestionnairePost {
    private String timestamp;
    private boolean redflag;
    private ArrayList<AnswerPost> answers;

    public QuestionnairePost(String timestamp, boolean redflag, ArrayList<AnswerPost> answers) {
        this.timestamp = timestamp + "Z";
        this.redflag = redflag;
        this.answers = answers;
    }
}
