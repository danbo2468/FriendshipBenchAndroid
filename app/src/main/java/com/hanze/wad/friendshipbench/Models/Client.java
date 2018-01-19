package com.hanze.wad.friendshipbench.Models;

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
}
