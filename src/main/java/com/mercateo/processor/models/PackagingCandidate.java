package com.mercateo.processor.models;

import java.util.List;

public class PackagingCandidate {

    int weightLimit;
    List<Item> items;

    public PackagingCandidate() {
    }

    public PackagingCandidate(int weightLimit, List<Item> items) {
        this.weightLimit = weightLimit;
        this.items = items;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
