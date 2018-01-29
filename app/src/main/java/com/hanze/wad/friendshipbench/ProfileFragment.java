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
import com.hanze.wad.friendshipbench.Models.Client;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment controller for the profile page.
 */
public class ProfileFragment extends CustomFragment {

    private Client client;

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
        initializeSuper(R.layout.profile_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Get the user information
        fetchProfile();

        // Handle the OnItemClick method for the Floating Action Button
        view.findViewById(R.id.profileFab).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchFragment(new EditProfileFragment(), true);
            }
        });
    }

    /**
     * Make a GET request to the API to get the requested user.
     * @param email The users email.
     */
    private void fetchProfile() {

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.account_url) + "/me", Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    client = new Client(new JSONObject(result));
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
        ((TextView) activity.findViewById(R.id.profileNameValue)).setText(client.getFullname());
        ((TextView) activity.findViewById(R.id.profileGenderValue)).setText(client.getFancyGender());
        ((TextView) activity.findViewById(R.id.profileAddressValue)).setText(client.getAddress());
        ((TextView) activity.findViewById(R.id.profileEmailValue)).setText(client.getEmail());
        ((TextView) activity.findViewById(R.id.profileUsernameValue)).setText(client.getUsername());
        ((TextView) activity.findViewById(R.id.profileBirthdayValue)).setText(client.getFancyBirthday());
    }
}
