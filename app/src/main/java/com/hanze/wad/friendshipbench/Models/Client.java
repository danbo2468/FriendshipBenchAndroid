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

public class Client {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String gender;
    private Date birthday;
    private String streetname;
    private String housenumber;
    private String province;
    private String district;
    private String phonenumber;
    private Healthworker healthWorker;

    public Client(JSONObject json){
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
            streetname = json.getString("streetName");
            housenumber = json.getString("housenumber");
            district = json.getString("district");
            province = json.getString("province");
            phonenumber = json.getString("phonenumber");
            if(json.getJSONObject("healthWorker") != null)
                    healthWorker = new Healthworker(json.getJSONObject("healthWorker"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHousenumber() {
        return housenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Healthworker getHealthWorker() {
        return healthWorker;
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

    public String getProvince() {
        return province;
    }

    public String getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public String getHousenmber() {
        return housenumber;
    }

    public String getStreetname() {
        return streetname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getDistrict() {
        return district;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getAddress() {
        return streetname + " " + housenumber + ", " + district + ", " + province;
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

    public String getChatKey(){
        return id + "" + healthWorker.getId();
    }

    public boolean hasHealthworker(){
        return healthWorker != null;
    }
}
