package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Component implements I_Component{
    private String name;
    private int fatContent;
    private int quantity;
    private int id;

    public Component() {};
    public Component(String name, int fatContent, int quantity, int id) {
        this.name = name;
        this.fatContent = fatContent;
        this.quantity = quantity;
        this.id = id;
    }

    public void setFatContent(int fatContent) {
        this.fatContent = fatContent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getFatContent() {
        return fatContent;
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<Entry> getEntries(ArrayList<Entry> e)
    {
        ArrayList<Entry> en = new ArrayList<>();
        for(int i = 0; i < e.size(); i++) {
            ArrayList<Component> et = e.get(i).getComponents();
            ArrayList<Combination> co = e.get(i).getCombinations();
            for (int j = 0; j < et.size(); j++) {
                if (this.name.equals(et.get(j).getName())) {
                    en.add(e.get(i));
                }
            }

            for (int k = 0; k < co.size(); k++) {
                if (this.name.equals(co.get(k).getName())) {
                    en.add(e.get(i));
                }

                ArrayList<Component> comps = co.get(k).getComponents();
                if(comps != null) {
                    for (int l = 0; l < comps.size(); l++) {
                        if (this.name.equals(comps.get(l).getName())) {
                            en.add(e.get(i));
                        }
                    }
                }
            }
        }

        return en;
    }
}
