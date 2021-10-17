package com.mercateo.processor.models;

public class Item {

    private int itemNo;
    private double weight;
    private int cost;

    public Item(){}

    public Item(int itemNo, double weight, int cost){
        this.itemNo = itemNo;
        this.weight = weight;
        this.cost = cost;
    }

    public int getItemNo() {
        return itemNo;
    }

    public double getWeight() {
        return weight;
    }

    public int getCost() {
        return cost;
    }
}
