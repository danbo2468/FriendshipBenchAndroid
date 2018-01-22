/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.ClientController;
import com.hanze.wad.friendshipbench.Controllers.HealthworkerController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Healthworker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The fragment controller for my_healthworker_layout.
 */
public class MyHealthworkerFragment extends Fragment {

    private Healthworker healthworker;

    /**
     * Initialize the view.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The current view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Get the current view.
        View view = inflater.inflate(R.layout.my_healthworker_layout, container, false);

        // Get the user information
        fetchHealthworker("06f5e5b1-9ce4-4eb7-8632-3ab593680e7d");

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get the requested healthworker.
     * @param id The healthworkers id.
     */
    private void fetchHealthworker(String id) {

        // Make an API GET request.
        ApiController.getInstance(getActivity().getBaseContext()).getRequest(getResources().getString(R.string.healthworkers_url) + "/" + id, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToHealthworker(new JSONObject(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Convert a JSON object to a healthworker.
     * @param json The JSON object with a healthworker in it.
     */
    private void jsonToHealthworker(JSONObject json) {
        this.healthworker = HealthworkerController.jsonToModel(json);
        updateView();
    }

    /**
     * Update the view with the right profile information.
     */
    private void updateView(){

        // Update all the text items.
        ((TextView) getActivity().findViewById(R.id.healthworkerNameValue)).setText(healthworker.getFullName());
        ((TextView) getActivity().findViewById(R.id.healthworkerGenderValue)).setText(healthworker.getGenderString());
        ((TextView) getActivity().findViewById(R.id.healthworkerBirthdayValue)).setText(healthworker.getReadableBirthday());
        ((TextView) getActivity().findViewById(R.id.healthworkerEmailValue)).setText(healthworker.getEmail());
    }
}
