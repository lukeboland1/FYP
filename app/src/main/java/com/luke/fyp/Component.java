package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Component implements I_Component{
    private String name;
    private double fatContent;
    private double quantity;
    private String servingType;
    private int id;

    public Component() {};
    public Component(String name, double fatContent, double quantity, int id, String servingType) {
        this.name = name;
        this.fatContent = fatContent;
        this.quantity = quantity;
        this.id = id;
        this.servingType = servingType;
    }

    public String getServingType() {
        return servingType;
    }

    public void setServingType(String servingType) {
        this.servingType = servingType;
    }


    public void setFatContent(double fatContent) {
        this.fatContent = fatContent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
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
    public double getFatContent() {
        return fatContent;
    }

    public double getTotalFat(){
        if (servingType.equals("Millilitres") || servingType.equals("Grams") || servingType.equals("Milligrams"))
        {
            double fat = quantity / 100;
            double fatP = fatContent * fat;
            return fatP;
        }

        else
        {
            double fat = quantity * fatContent;
            return fat;
        }
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
