/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.ClientPut;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment controller for the edit profile page.
 */
public class EditProfileFragment extends CustomFragment {

    private Client client;
    private Calendar birthdayCalendar;
    private DatePickerDialog.OnDateSetListener date;

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
        initializeSuper(R.layout.edit_profile_layout, true, new ProfileFragment(), inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Get the current client details.
        fetchProfile();
        birthdayCalendar = Calendar.getInstance();

        // Handle the OnItemClick method for update button. It will do an API PUT request.
        view.findViewById(R.id.updateProfileButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                updateProfile();
            }
        });

        // Handle the OnItemClick method for the calendar popup.
        view.findViewById(R.id.editBirthdayField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, date, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Handle the OnDateSetListener.
        final EditText field = view.findViewById(R.id.editBirthdayField);
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayCalendar = Calendar.getInstance();
                birthdayCalendar.set(Calendar.YEAR, year);
                birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                field.setText(dateFormat.format(birthdayCalendar.getTime()));
            }
        };
    }

    /**
     * Make a GET request to the API to get the requested user.
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
    private void updateView() {

        // Update all the text items.
        ((TextView) activity.findViewById(R.id.editFirstnameField)).setText(client.getFirstname());
        ((TextView) activity.findViewById(R.id.editLastnameField)).setText(client.getLastname());
        ((TextView) activity.findViewById(R.id.editStreetnameField)).setText(client.getStreetname());
        ((TextView) activity.findViewById(R.id.editHousenumberField)).setText(client.getHousenmber());
        ((TextView) activity.findViewById(R.id.editDistrictField)).setText(client.getDistrict());
        ((TextView) activity.findViewById(R.id.editProvinceField)).setText(client.getProvince());
        ((TextView) activity.findViewById(R.id.editBirthdayField)).setText(client.getFancyBirthday());

        // Update the gender radio buttons.
        ((RadioButton) activity.findViewById(R.id.editGenderMale)).setChecked(client.getGender().equals("Male"));
        ((RadioButton) activity.findViewById(R.id.editGenderFemale)).setChecked(client.getGender().equals("Female"));
    }

    /**
     * Update the client's profile.
     */
    private void updateProfile(){

        // Get the gender.
        String gender;
        if(((RadioButton) activity.findViewById(R.id.editGenderMale)).isChecked())
            gender = "Male";
        else
            gender = "Female";

        // Get the birthday.
        String birthday = "";
        if(birthdayCalendar != null){
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            birthday = dateFormat.format(birthdayCalendar.getTime());
        }

        // Update the client details.
        ClientPut clientPut = new ClientPut(((EditText)activity.findViewById(R.id.editFirstnameField)).getText().toString(), ((EditText)activity.findViewById(R.id.editLastnameField)).getText().toString(), gender, ((EditText)activity.findViewById(R.id.editStreetnameField)).getText().toString(), ((EditText)activity.findViewById(R.id.editHousenumberField)).getText().toString(), ((EditText)activity.findViewById(R.id.editProvinceField)).getText().toString(), ((EditText)activity.findViewById(R.id.editDistrictField)).getText().toString(), birthday);
        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(clientPut));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make an API PUT request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.account_url) + "/me", Request.Method.PUT, json, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(context, "Your profile details have been updated.", Toast.LENGTH_LONG).show();
                switchFragment(new ProfileFragment(), false);
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }
}
