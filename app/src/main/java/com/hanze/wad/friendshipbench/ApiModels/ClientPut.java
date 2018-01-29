/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.ApiModels;

public class ClientPut {
    private String firstName;
    private String lastName;
    private String gender;
    private String streetName;
    private String houseNumber;
    private String province;
    private String district;
    private String birthDay;

    public ClientPut(String firstName, String lastName, String gender, String streetName, String houseNumber, String province, String district, String birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.province = province;
        this.district = district;
        this.birthDay = birthday + "T00:00:00Z";
    }
}
