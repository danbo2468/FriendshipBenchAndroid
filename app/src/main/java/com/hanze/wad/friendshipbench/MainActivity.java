package com.hanze.wad.friendshipbench;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.Models.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        user = new User("4216d9b5-43ae-47e8-b63a-ec200e8fc2a6", "daniel.boonstra@outlook.com", "Mustafa", "Nig Nog", "06f5e5b1-9ce4-4eb7-8632-3ab59380e7d");
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("user", userJson);
        editor.apply();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_appointments) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AppointmentOverviewFragment())
                    .commit();
        } else if (id == R.id.nav_questionnaires) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new QuestionnaireOverviewFragment())
                    .commit();
        } else if (id == R.id.nav_healthworker) {
            if(user.getHealthworkerId() != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new MyHealthworkerFragment())
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new ChooseHealthworkerFragment())
                        .commit();
            }
        } else if (id == R.id.nav_conversations) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ConversationFragment())
                    .commit();
        } else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ProfileFragment())
                    .commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AboutFragment())
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
