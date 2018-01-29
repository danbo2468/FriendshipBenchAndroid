/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.FileController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Token;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The activity for the login.
 */
public class LoginActivity extends AppCompatActivity {

    private String clientId = "OauthClient";
    private String clientSecret = "OauthSuperSecret";

    /**
     * This method will be called upon creation.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // If the user is loggedin, redirect the user to the main activity.
        if(FileController.fileExists(getString(R.string.token_file_name), this)) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            return;
        }

        // Handle the loginButton clicks.
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attemptLogin();
            }
        });

        // Handle the registerButton clicks.
        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
    }

    /**
     * Don't do anything when the back button is pressed.
     */
    @Override
    public void onBackPressed() {}

    /**
     * Try to log the user in.
     */
    private void attemptLogin(){

        // Check and set the information.
        String username = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordField)).getText().toString();
        if(username.equals("") || password.equals("")){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.no_password_username_provided), Toast.LENGTH_LONG).show();
            return;
        }
        String stringToEncode = clientId + ":" + clientSecret;
        byte[] bytesEncoded = Base64.encode(stringToEncode.getBytes(), Base64.DEFAULT);
        String base64 =  new String(bytesEncoded);

        // Do a request to retrieve the token.
        ApiController.getInstance(getBaseContext()).getToken(getString(R.string.authentication_url), username, password, base64, new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    saveToken(new JSONObject(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), getResources().getString(R.string.succesfully_logged_in), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
            @Override
            public void onError(VolleyError result){
                if(result.networkResponse.statusCode == 400)
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * Save the token.
     * @param json The json with the token.
     */
    private void saveToken(JSONObject json){
        String modelJson = new Gson().toJson(new Token(json));
        FileController.writeFile(getString(R.string.token_file_name), modelJson, this);
    }
}

