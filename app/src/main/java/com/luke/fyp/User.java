package com.luke.fyp;

/**
 * Created by Luke on 01/10/2015.
 */
public class User {
    private int creonType;
    private double fatPerCreon;

    public User(int creonType, double fatPerCreon) {
        this.creonType = creonType;
        this.fatPerCreon = fatPerCreon;
    }

    public int getCreonType() {
        return creonType;
    }

    public void setCreonType(int creonType) {
        this.creonType = creonType;
    }

    public double getFatPerCreon() {
        return fatPerCreon;
    }

    public void setFatPerCreon(int fatPerCreon) {
        this.fatPerCreon = fatPerCreon;
    }
}
