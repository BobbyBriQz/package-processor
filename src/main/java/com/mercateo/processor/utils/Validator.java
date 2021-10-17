package com.mercateo.processor.utils;

import com.mercateo.processor.models.Item;
import com.mercateo.processor.models.PackagingCandidate;

import java.util.List;

public class Validator {

    private static final int MAXIMUM_WEIGHT_ALLOWED = 100;
    private static final int MAXIMUM_NO_OF_ITEMS = 15;
    private static final int MAXIMUM_COST_OF_ITEM = 100;

    public void validate(PackagingCandidate candidate) {
        int weight = candidate.getWeightLimit();
        List<Item> items = candidate.getItems();
        if(weight > MAXIMUM_WEIGHT_ALLOWED){
            throw new IllegalArgumentException("Weight should not be greater than "+ MAXIMUM_WEIGHT_ALLOWED);
        }

        if(items.size() > MAXIMUM_NO_OF_ITEMS){
            throw new IllegalArgumentException("Items should not be more than " + MAXIMUM_NO_OF_ITEMS);
        }

        if(items.stream().anyMatch(item -> item.getWeight() > MAXIMUM_WEIGHT_ALLOWED)){
            throw new IllegalArgumentException("Item weight should not be greater than "+ MAXIMUM_WEIGHT_ALLOWED);
        }

        if(items.stream().anyMatch(item -> item.getCost() > MAXIMUM_COST_OF_ITEM)){
            throw new IllegalArgumentException("Item cost should not be greater than "+ MAXIMUM_COST_OF_ITEM );
        }
    }
}
