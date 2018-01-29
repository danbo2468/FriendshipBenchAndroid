/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.ApiModels;

public class RegisterModel {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String province;
    private String district;
    private String streetName;
    private String housenumber;
    private String birthDay;
    private String gender;

    public RegisterModel(String email, String username, String password, String firstname, String lastname, String province, String district, String streetname, String number, String gender) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastname;
        this.province = province;
        this.district = district;
        this.streetName = streetname;
        this.housenumber = number;
        this.gender = gender;
        this.birthDay = "2018-01-29T13:30:08.864Z";
    }
}
