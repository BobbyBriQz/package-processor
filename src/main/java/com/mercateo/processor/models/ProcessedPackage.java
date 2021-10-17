package com.mercateo.processor.models;

import java.util.List;

public class ProcessedPackage {
    private int totalCost;
    private double totalWeight;
    private List<Item> items;

    public ProcessedPackage() {
    }

    public ProcessedPackage(int totalCost, double totalWeight, List<Item> items) {
        this.totalCost = totalCost;
        this.totalWeight = totalWeight;
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
