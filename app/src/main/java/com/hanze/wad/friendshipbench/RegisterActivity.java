/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.RegisterModel;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * The activity for the register logic.
 */
public class RegisterActivity extends AppCompatActivity {

    private Calendar birthdayCalendar;
    private DatePickerDialog.OnDateSetListener date;

    /**
     * This method will be called upon creation.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        birthdayCalendar = Calendar.getInstance();

        // Handle the registerButton clicks.
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attemptRegister();
            }
        });

        // Handle the loginButton clicks.
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        // Handle the birthdayField clicks.
        findViewById(R.id.birthdayField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Handle the OnDateSetListener.
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthdayCalendar = Calendar.getInstance();
                birthdayCalendar.set(Calendar.YEAR, year);
                birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                ((EditText)findViewById(R.id.birthdayField)).setText(dateFormat.format(birthdayCalendar.getTime()));
            }
        };
    }

    /**
     * Go to the login page when the back button is being pressed.
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

    /**
     * Try to create a new account.
     */
    private void attemptRegister(){
        String email = ((EditText)findViewById(R.id.emailField)).getText().toString();
        String username = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordField)).getText().toString();
        String passwordConfirmation = ((EditText)findViewById(R.id.passwordConfirmField)).getText().toString();
        String firstname = ((EditText)findViewById(R.id.firstnameField)).getText().toString();
        String lastname = ((EditText)findViewById(R.id.lastnameField)).getText().toString();
        String province = ((EditText)findViewById(R.id.provinceField)).getText().toString();
        String district = ((EditText)findViewById(R.id.districtField)).getText().toString();
        String streetname = ((EditText)findViewById(R.id.streetnameField)).getText().toString();
        String number = ((EditText)findViewById(R.id.housenumberField)).getText().toString();
        String birthday = "";
        if(birthdayCalendar != null){
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            birthday = dateFormat.format(birthdayCalendar.getTime());
        }

        String gender;
        if(((RadioButton) findViewById(R.id.genderMale)).isChecked())
            gender = "Male";
        else
            gender = "Female";
        if(email.equals("") || username.equals("") || password.equals("") || passwordConfirmation.equals("") || firstname.equals("") || lastname.equals("") || province.equals("") || district.equals("") || streetname.equals("") || number.equals("") || birthday.equals("")){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(passwordConfirmation)){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.different_passwords), Toast.LENGTH_LONG).show();
            return;
        }
        RegisterModel registerModel = new RegisterModel(email, username, password, firstname, lastname, province, district, streetname, number, gender, birthday);

        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(registerModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiController.getInstance(getBaseContext()).apiRequest(getString(R.string.register_url), Request.Method.POST, json, null, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                Toast.makeText(getBaseContext(), getResources().getString(R.string.succesfully_registered), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

}

