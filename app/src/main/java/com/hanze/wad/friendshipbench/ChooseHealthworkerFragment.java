/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.AddHealthworkerPut;
import com.hanze.wad.friendshipbench.ApiModels.AnswerPost;
import com.hanze.wad.friendshipbench.ApiModels.QuestionnairePost;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.HealthworkerController;
import com.hanze.wad.friendshipbench.Controllers.QuestionController;
import com.hanze.wad.friendshipbench.Controllers.QuestionnaireController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Answer;
import com.hanze.wad.friendshipbench.Models.Healthworker;
import com.hanze.wad.friendshipbench.Models.Question;
import com.hanze.wad.friendshipbench.Models.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment controller for the choose healthworker page.
 */
public class ChooseHealthworkerFragment extends CustomFragment {

    private ArrayList<Healthworker> healthworkerList = new ArrayList<>();
    private int currentHealthworker;

    /**
     * The OnCreateView method which will be called first.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The created view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initializeSuper(R.layout.choose_healthworker_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Fetch the list with healthworkers.
        fetchHealthworkers();

        // Handle the OnItemClick method for the next button.
        view.findViewById(R.id.buttonNextHealthworker).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showHealthworker(getHealthworker(1));
            }
        });

        // Handle the OnItemClick method for the previous button.
        view.findViewById(R.id.buttonPreviousHealthworker).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showHealthworker(getHealthworker(-1));
            }
        });

        // Handle the OnItemClick method for the choose button.
        view.findViewById(R.id.buttonChooseHealthworker).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chooseHealthworker();
            }
        });
    }

    /**
     * Make a GET request to the API to get all the healthworkers.
     */
    private void fetchHealthworkers() {

        // Make an API GET request.
        ApiController.getInstance(context).getRequest(getResources().getString(R.string.healthworkers_url), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToHealthworker(new JSONArray(result));
                    showHealthworker(getHealthworker(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Convert a JSON object to a healthworker.
     * @param json The JSON object with the healthworker in it.
     */
    private void jsonToHealthworker(JSONArray json) {
        for (int i = 0; i < json.length(); i++) {
            JSONObject questionJson = null;
            try {
                questionJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            healthworkerList.add(HealthworkerController.jsonToModel(questionJson));
        }
    }

    /**
     * Get the next healthworker.
     * @param direction The direction to go. -1 indicates back, 0 indicates the start of the list and +1 indicates forward.
     * @return The healthworker.
     */
    private Healthworker getHealthworker(int direction){
        if(direction == 0){
            currentHealthworker = 0;
        } else if (direction == -1){
            if(currentHealthworker == 0)
                currentHealthworker = healthworkerList.size()-1;
            else
                currentHealthworker--;
        } else if (direction == 1){
            if(currentHealthworker == healthworkerList.size()-1)
                currentHealthworker = 0;
            else
                currentHealthworker++;
        }
        return healthworkerList.get(currentHealthworker);
    }

    /**
     * Set the current healthworker as your healthworker.
     */
    private void chooseHealthworker(){

        // Create a new add healthworker model.
        AddHealthworkerPut addHealthworkerPut = new AddHealthworkerPut(healthworkerList.get(currentHealthworker).getId());
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(addHealthworkerPut));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make an API POST request.
        ApiController.getInstance(context).putRequest(getResources().getString(R.string.account_url) + "/addHealthworker/daniel.boonstra@outlook.com", json, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(context, "You have chosen " + healthworkerList.get(currentHealthworker).getFullName() + " as your healthworker.", Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyHealthworkerFragment()).commit();
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update the view.
     * @param healthworker The healthworker model with the information.
     */
    private void showHealthworker(Healthworker healthworker){

        // Update all the text items.
        ((TextView) activity.findViewById(R.id.healthworkerNameValue)).setText(healthworker.getFullName());
        ((TextView) activity.findViewById(R.id.healthworkerGenderValue)).setText(healthworker.getGenderString());
        ((TextView) activity.findViewById(R.id.healthworkerBirthdayValue)).setText(healthworker.getReadableBirthday());
        ((TextView) activity.findViewById(R.id.healthworkerEmailValue)).setText(healthworker.getEmail());
    }
}
