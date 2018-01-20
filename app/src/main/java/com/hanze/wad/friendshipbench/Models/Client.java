package com.hanze.wad.friendshipbench.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danie on 17-Jan-18.
 */

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

    public Client(String id, String lastname, String email, String province, String gender, String housenumber, String streetname, String birthday, String district, String firstname) {
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

    public String getAddress() {
        return streetname + " " + housenumber + ", " + district + ", " + province;
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
