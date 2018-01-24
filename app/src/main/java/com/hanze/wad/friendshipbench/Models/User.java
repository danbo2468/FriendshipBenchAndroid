/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench.Models;

public class User {

    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String healthworkerId;

    public User(String id, String email, String firstname, String lastname, String healthworkerId) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.healthworkerId = healthworkerId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getHealthworkerId() {
        return healthworkerId;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getChatKey(){
        if(healthworkerId !=  null){
            return id + "-" + healthworkerId;
        }
        return null;
    }
}
