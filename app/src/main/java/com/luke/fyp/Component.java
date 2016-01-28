package com.luke.fyp;

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
}
