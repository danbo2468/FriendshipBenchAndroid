package com.hanze.wad.friendshipbench.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private String id;
    private String lastname;
    private String email;
    private String province;
    private String gender;
    private String housenumber;
    private String streetname;
    private String birthday;
    private String district;
    private String firstname;
    private String healthWorkerId;

    public Client(String id, String lastname, String email, String province, String gender, String housenumber, String streetname, String birthday, String district, String firstname, String healthWorkerId) {
        this.id = id;
        this.lastname = lastname;
        this.email = email;
        this.province = province;
        this.gender = gender;
        this.housenumber = housenumber;
        this.streetname = streetname;
        this.birthday = birthday;
        this.district = district;
        this.firstname = firstname;
        this.healthWorkerId = healthWorkerId;
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

    public String getHousenmber() {
        return housenumber;
    }

    public String getStreetname() {
        return streetname;
    }

    public String getBirthday() {
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

    public String getHealthWorkerId() {
        return healthWorkerId;
    }

    public void setHealthWorkerId(String healthWorkerId){
        this.healthWorkerId = healthWorkerId;
    }

    public String getAddress() {
        return streetname + " " + housenumber + ", " + district + ", " + province;
    }

    public String getGenderString(){
        if(gender.equals("Male"))
            return "Male";
        else if(gender.equals("Female"))
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

    public String getChatKey(){
        return id + "" + healthWorkerId;
    }

    public boolean hasHealthworker(){
        return healthWorkerId != null;
    }
}
