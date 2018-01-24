/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.ClientController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The fragment controller for profile_layout.
 */
public class ProfileFragment extends Fragment {

    private Client client;

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
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        // Get the user
        User user = ((MainActivity)getActivity()).user;
        fetchProfile(user.getEmail());

        // Handle the OnItemClick method for the Floating Action Button
        view.findViewById(R.id.profileFab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new EditProfileFragment()).commit();
            }
        });

        // Return the view.
        return view;
    }

    /**
     * Make a GET request to the API to get the requested user.
     * @param email The users email.
     */
    private void fetchProfile(String email) {

        // Make an API GET request.
        ApiController.getInstance(getActivity().getBaseContext()).getRequest(getResources().getString(R.string.account_url) + "/currentUser/" + email, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToClient(new JSONObject(result));
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
     * Convert a JSON object to a client.
     * @param json The JSON object with a client in it.
     */
    private void jsonToClient(JSONObject json) {
        this.client = ClientController.jsonToModel(json);
        updateView();
    }

    /**
     * Update the view with the right profile information.
     */
    private void updateView(){

        // Update all the text items.
        ((TextView) getActivity().findViewById(R.id.profileNameValue)).setText(client.getFullname());
        ((TextView) getActivity().findViewById(R.id.profileGenderValue)).setText(client.getGenderString());
        ((TextView) getActivity().findViewById(R.id.profileBirthdayValue)).setText(client.getReadableBirthday());
        ((TextView) getActivity().findViewById(R.id.profileAddressValue)).setText(client.getAddress());
        ((TextView) getActivity().findViewById(R.id.profileEmailValue)).setText(client.getEmail());
    }
}
