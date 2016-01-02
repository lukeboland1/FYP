package com.luke.fyp;

/**
 * Created by Luke on 22/09/2015.
 */
public class Meal {

    private String name;
    private int fatContent;

    public Meal()
    {

    }
    public Meal(String name, int fatContent)
    {
        this.name = name;
        this.fatContent = fatContent;
    }

    public String getName() {
        return name;
    }

    public int getFatContent() {
        return fatContent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFatContent(int fatContent) {
        this.fatContent = fatContent;
    }
}
