package com.hanze.wad.friendshipbench.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Healthworker {
    private String id;
    private String birthday;
    private String phonenumber;
    private String email;
    private String gender;
    private String lastname;
    private String firstname;

    public Healthworker(String id, String birthday, String phonenumber, String email, String gender, String lastname, String firstname) {
        this.id = id;
        this.birthday = birthday;
        this.phonenumber = phonenumber;
        this.email = email;
        this.gender = gender;
        this.lastname = lastname;
        this.firstname = firstname;
    }
    public String getId(){
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getFullName(){
        return firstname + " " + lastname;
    }

    public String getGenderString(){
        if(gender.equals("male"))
            return "Male";
        else if(gender.equals("female"))
            return "Female";
        else
            return "Unknown";
    }

    public String getReadableBirthday() {
        Date timeString;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            timeString = formatter.parse(this.birthday);
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            return df.format(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
