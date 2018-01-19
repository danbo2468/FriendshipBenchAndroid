package com.hanze.wad.friendshipbench.Models;

/**
 * Created by danie on 17-Jan-18.
 */

public class Status {
    private int id;
    private String name;

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
