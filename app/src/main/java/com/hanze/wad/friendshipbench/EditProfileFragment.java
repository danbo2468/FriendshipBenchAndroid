/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.ClientPut;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.ClientController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Client;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The fragment controller for edit_profile_layout.
 */
public class EditProfileFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.edit_profile_layout, container, false);

        // Get the current client email.
        Bundle bundle = getArguments();
        fetchProfile(bundle.getString("client_email"));

        // Handle the OnItemClick method for update button. It will do an API PUT request.
        view.findViewById(R.id.updateProfileButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                updateProfile();
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
    private void updateView() {

        // Update all the text items.
        ((TextView) getActivity().findViewById(R.id.editFirstnameField)).setText(client.getFirstname());
        ((TextView) getActivity().findViewById(R.id.editLastnameField)).setText(client.getLastname());
        ((TextView) getActivity().findViewById(R.id.editStreetnameField)).setText(client.getStreetname());
        ((TextView) getActivity().findViewById(R.id.editHousenumberField)).setText(client.getHousenmber());
        ((TextView) getActivity().findViewById(R.id.editDistrictField)).setText(client.getDistrict());
        ((TextView) getActivity().findViewById(R.id.editProvinceField)).setText(client.getProvince());

        // Update the gender radio buttons.
        ((RadioButton) getActivity().findViewById(R.id.editGenderMale)).setChecked(client.getGender().equals("male"));
        ((RadioButton) getActivity().findViewById(R.id.editGenderFemale)).setChecked(client.getGender().equals("female"));

    }

    private void updateProfile(){
        ClientPut clientPut = new ClientPut(((EditText)getActivity().findViewById(R.id.editStreetnameField)).getText().toString(), ((EditText)getActivity().findViewById(R.id.editHousenumberField)).getText().toString(), ((EditText)getActivity().findViewById(R.id.editProvinceField)).getText().toString(), ((EditText)getActivity().findViewById(R.id.editDistrictField)).getText().toString());
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(clientPut));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TEST", json.toString());

        // Make an API PUT request.
        ApiController.getInstance(getActivity().getBaseContext()).putRequest(getResources().getString(R.string.account_url) + "/edit/" + client.getEmail(), json, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(getActivity().getBaseContext(), "Your profile details have been updated.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }
}
