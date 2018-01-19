package com.hanze.wad.friendshipbench.Models;

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
}
