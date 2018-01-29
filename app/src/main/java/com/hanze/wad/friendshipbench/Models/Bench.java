/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;

public class Bench {

    private int id;
    private String streetname;
    private String housenumber;
    private String province;
    private String district;

    public Bench(JSONObject json) {
        try {
            id = json.getInt("id");
            streetname = json.getString("streetname");
            housenumber = json.getString("housenumber");
            province = json.getString("province");
            district = json.getString("district");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public LatLng getLatLong(Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latlng = null;
        try {
            address = coder.getFromLocationName(getFullLocation(), 5);
            if (address == null)
                return null;
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            latlng = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return latlng;
    }
}
