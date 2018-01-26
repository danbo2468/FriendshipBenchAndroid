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
import com.hanze.wad.friendshipbench.Controllers.TokenController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String clientId = "OauthClient";
    private String clientSecret = "OauthSuperSecret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                attemptLogin();
            }
        });
    }

    private void attemptLogin(){
        String username = ((EditText)findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordField)).getText().toString();
        if(username.equals("") || password.equals("")){
            Toast.makeText(getBaseContext(), getResources().getString(R.string.no_password_username_provided), Toast.LENGTH_LONG).show();
            return;
        }
        String stringToEncode = clientId + ":" + clientSecret;
        byte[] bytesEncoded = Base64.encode(stringToEncode.getBytes(), Base64.DEFAULT);
        String base64 =  new String(bytesEncoded);

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

    private void saveToken(JSONObject json){
        String modelJson = new Gson().toJson(TokenController.jsonToModel(json));
        FileController.writeFile(getString(R.string.token_file_name), modelJson, this);
    }
}

