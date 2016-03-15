package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Entry {
    private ArrayList<Component> components;
    private ArrayList<Combination> combinations;
    private int id;
    private String name;
    private int creonTaken;
    private long dateTaken;
    private String notes;
    private int results;

    public Entry(String name, int creonTaken, String notes, long dateTaken, int results) {
        this.creonTaken = creonTaken;
        this.name = name;
        this.notes = notes;
        this.dateTaken = dateTaken;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        String jal = "";
        if(combinations.size() > 0)
        {
            jal += " " + combinations.get(0).getName();
        }
        for(int i = 0; i < components.size(); i++)
        {
            jal += " " + components.get(i).getName();
        }

        return jal;
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

    public boolean isBetweenTwoDates(long date1, long date2)
    {
        if(dateTaken >= date1 && dateTaken < date2)
        {
            return true;
        }
        else
        {
            return false;
        }
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

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public ArrayList<Combination> getCombinations() {
        return combinations;
    }

    public void setCombinations(ArrayList<Combination> combinations) {
        this.combinations = combinations;
    }
}
