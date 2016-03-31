package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Entry {
    private ArrayList<Component> components;
    private Combination combination;
    private int id;
    private String name;
    private int creon10000taken;
    private int creon25000taken;
    private long dateTaken;
    private String notes;
    private int results;

    public Entry(String name, int creon10000Taken, int creon25000Taken, String notes, long dateTaken, int results) {
        this.creon10000taken = creon10000Taken;
        this.creon25000taken = creon25000Taken;
        this.name = name;
        this.notes = notes;
        this.dateTaken = dateTaken;
        this.results = results;
        combination = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreon25000taken() {
        return creon25000taken;
    }

    public void setCreon25000taken(int creon25000taken) {
        this.creon25000taken = creon25000taken;
    }

    public String getName() {
        String jal = "";
        if(combination != null)
        {
            jal += " " + combination.getName();
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

    public int getCreon10000taken() {
        return creon10000taken;
    }

    public void setCreon10000taken(int creon10000taken) {
        this.creon10000taken = creon10000taken;
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

    public Combination getCombination() {
        return combination;
    }

    public void setCombination(Combination combination) {
        this.combination = combination;
    }
}
