/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Healthworker;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment controller for the my healhtworker page.
 */
public class MyHealthworkerFragment extends CustomFragment {

    private Healthworker healthworker;

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
        initializeSuper(R.layout.my_healthworker_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){
        fetchHealthworker(activity.user.getHealthWorker().getId());
    }

    /**
     * Make a GET request to the API to get the requested healthworker.
     * @param id The healthworkers id.
     */
    private void fetchHealthworker(String id) {

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.healthworkers_url) + "/" + id, Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    healthworker = new Healthworker(new JSONObject(result));
                    updateView();
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
     * Update the view with the right profile information.
     */
    private void updateView(){

        // Update all the text items.
        ((TextView) activity.findViewById(R.id.healthworkerNameValue)).setText(healthworker.getFullname());
        ((TextView) activity.findViewById(R.id.healthworkerGenderValue)).setText(healthworker.getFancyGender());
        ((TextView) activity.findViewById(R.id.healthworkerEmailValue)).setText(healthworker.getEmail());
        ((TextView) activity.findViewById(R.id.healthworkerAgeValue)).setText(healthworker.getAge() + " years");
    }
}
