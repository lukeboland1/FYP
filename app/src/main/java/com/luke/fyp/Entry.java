package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Entry {
    private ArrayList<I_Component> components;
    private int id;
    private String name;
    private int creonTaken;
    private long dateTaken;
    private String notes;
    private int results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreonTaken() {
        return creonTaken;
    }

    public void setCreonTaken(int creonTaken) {
        this.creonTaken = creonTaken;
    }

    public String getDateTaken() {
        String s = "0";
        s = Long.toString(dateTaken);
        return s;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }
}
