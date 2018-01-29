/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.FileController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Token;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The activity for the main logic.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Client user;
    public Token token;
    public CustomFragment currentFragment;

    /**
     * This method will be called upon creation.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // If the user isn't logged in, redirect the user to the login activity.
        if(!FileController.fileExists(getString(R.string.token_file_name), this)) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            return;
        }

        // Set the token.
        token = new Gson().fromJson(FileController.readFile(getString(R.string.token_file_name), this), Token.class);
        fetchUser();

        // Set the drawer.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
    }

    /**
     * Show the previous fragment when the back button has been pressed.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            currentFragment.goBack();
        }
    }

    /**
     * Get the user details.
     */
    public void fetchUser(){

        // Make an API GET request.
        ApiController.getInstance(getBaseContext()).apiRequest(getResources().getString(R.string.account_url) + "/me", Request.Method.GET, null, token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    user = new Client(new JSONObject(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handle the navigation.
     * @param item The item that has been pressed.
     * @return Whether the action has been completed.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_appointments) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AppointmentOverviewFragment()).commit();
        } else if (id == R.id.nav_questionnaires) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new QuestionnaireOverviewFragment()).commit();
        } else if (id == R.id.nav_healthworker) {
            if(user.hasHealthworker()) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MyHealthworkerFragment()).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ChooseHealthworkerFragment()).commit();
            }
        } else if (id == R.id.nav_conversations) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ConversationFragment()).commit();
        } else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
        } else if (id == R.id.nav_logout) {
            FileController.deleteFile(getString(R.string.token_file_name), this);
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
