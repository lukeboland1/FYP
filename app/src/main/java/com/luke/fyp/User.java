package com.luke.fyp;

/**
 * Created by Luke on 01/10/2015.
 */
public class User {
    private String creonType;
    private double fatPerCreon;

    public User(String creonType, double fatPerCreon) {
        this.creonType = creonType;
        this.fatPerCreon = fatPerCreon;
    }

    public String getCreonType() {
        return creonType;
    }

    public void setCreonType(String creonType) {
        this.creonType = creonType;
    }

    public double getFatPerCreon() {
        return fatPerCreon;
    }

    public void setFatPerCreon(int fatPerCreon) {
        this.fatPerCreon = fatPerCreon;
    }
}
