/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Healthworker {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String gender;
    private Date birthday;
    private String phonenumber;

    public Healthworker(JSONObject json){
        try {
            id = json.getString("id");
            firstname = json.getString("firstName");
            lastname = json.getString("lastName");
            email = json.getString("email");
            username = json.getString("username");
            gender = json.getString("gender");
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                birthday = formatter.parse(json.getString("birthDay"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            phonenumber = json.getString("phonenumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getFancyGender(){
        if(gender.toLowerCase().equals("male"))
            return "Male";
        else if(gender.toLowerCase().equals("female"))
            return "Female";
        else
            return "Unknown";
    }

    public String getFancyBirthday() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(birthday);
    }

    public int getAge(){
        Calendar birthdayCalendar = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        birthdayCalendar.set(birthday.getYear(), birthday.getMonth(), birthday.getDay());
        int age = today.get(Calendar.YEAR) - birthdayCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthdayCalendar.get(Calendar.DAY_OF_YEAR))
            age--;
        return age-1900;
    }
}
