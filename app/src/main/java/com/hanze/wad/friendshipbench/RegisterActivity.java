/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.ApiModels.RegisterModel;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.FileController;
import com.hanze.wad.friendshipbench.Controllers.TokenController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attemptRegister();
            }
        });

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
    }

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
        String gender;
        if(((RadioButton) findViewById(R.id.genderMale)).isChecked())
            gender = "Male";
        else
            gender = "Female";
        if(email.equals("") || username.equals("") || password.equals("") || passwordConfirmation.equals("") || firstname.equals("") || lastname.equals("") || province.equals("") || district.equals("") || streetname.equals("") || number.equals("")){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.fill_in_all_fields), Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(passwordConfirmation)){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.different_passwords), Toast.LENGTH_LONG).show();
            return;
        }
        RegisterModel registerModel = new RegisterModel(email, username, password, firstname, lastname, province, district, streetname, number, gender);

        JSONObject json = null;
        try {
            json = new JSONObject(new Gson().toJson(registerModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TEST", json.toString());
        ApiController.getInstance(getBaseContext()).register(getString(R.string.register_url), json, new VolleyCallback(){
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

