package com.hanze.wad.friendshipbench.Models;

/**
 * Created by danie on 17-Jan-18.
 */

public class Bench {
    private int id;
    private String streetname;
    private String housenumber;
    private String province;
    private String district;

    public Bench(int id, String streetname, String housenumber, String province, String district) {
        this.id = id;
        this.streetname = streetname;
        this.housenumber = housenumber;
        this.province = province;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public String getStreetname() {
        return streetname;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getFullLocation(){
        return streetname + " " + housenumber + ", " + district + ", " + province;
    }
}
