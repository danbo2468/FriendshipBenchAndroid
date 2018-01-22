/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.ApiModels;

public class ClientPut {
    private String streetName;
    private String houseNumber;
    private String province;
    private String district;

    public ClientPut(String streetName, String houseNumber, String province, String district) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.province = province;
        this.district = district;
    }
}
