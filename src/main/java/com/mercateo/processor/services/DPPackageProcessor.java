package com.mercateo.processor.services;

import com.mercateo.processor.models.Item;
import com.mercateo.processor.models.PackagingCandidate;
import com.mercateo.processor.models.ProcessedPackage;
import com.mercateo.processor.utils.Parser;
import com.mercateo.processor.utils.Validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DPPackageProcessor extends PackageProcessor{

    public DPPackageProcessor(Parser parser, Validator validator){
        this.parser = parser;
        this.validator = validator;
    }

    @Override
    public void run(String path) {
        List<String> testCases = parser.getTestCases(path);
        List<PackagingCandidate> candidates = parser.extractPackagingCandidates(testCases);
        List<ProcessedPackage> processedPackages = processCandidates(candidates);
        display(processedPackages);
    }

    @Override
    void display(List<ProcessedPackage> packages) {
        for(ProcessedPackage processedPackage: packages) {
            List<Item> items = processedPackage.getItems();
            String output = getDisplayText(items);
            System.out.println(output);
            System.out.println();
        }
    }

    @Override
    public ProcessedPackage process(PackagingCandidate candidate) {
        validator.validate(candidate);
        int weight = candidate.getWeightLimit();
        List<Item> items = candidate.getItems();

        int n = items.size();
        int row,column;
        //2-dimensional grid for DP tabulation
        int[][] grid = new int[n +1][weight+1];

        for(row = 0; row <= n; row++){
            for (column = 0; column <= weight; column++){
                if(row == 0 || column == 0) {
                    grid[row][column] = 0;
                }else if(items.get(row-1).getWeight() <= column) {

                    grid[row][column] = Math.max(grid[row - 1][(column - (int) Math.round(items.get(row - 1).getWeight()))] + items.get(row - 1).getCost(), grid[row - 1][column]);
                }else {

                    grid[row][column] = grid[row - 1][column];
                }
            }
        }
        int costOfItems = grid[n][weight];
        List<Item> packagedItems = getPackagedItems(grid,items);
        double packageWeight = packagedItems.stream().mapToDouble(Item::getWeight).sum();
        return new ProcessedPackage(costOfItems, packageWeight, packagedItems);
    }

    private List<Item> getPackagedItems(int[][] grid, List<Item> items){
        List<Item> processedItems = new ArrayList<>();

        int row = grid.length - 1;
        int column = grid[0].length -1;

        while (row > 0){
            if(grid[row][column] == grid[row-1][column]) {
                //Move one step up as the particular row (or item) was not used in getting the result
                row -= 1;
            }else{
                //Row (or Item) was used to get the
                Item item = items.get(row-1);
                processedItems.add(item);
                column -= Math.round(item.getWeight());
                row -= 1;
            }

            if(column == 0)
                break;
        }
        return processedItems;
    }

    private String getDisplayText(List<Item> items){
        if (items.isEmpty()) return "-";

        items.sort(Comparator.comparingInt(Item::getItemNo));

        StringBuilder stringBuilder = new StringBuilder();
        for (Item x : items) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(x.getItemNo());
                continue;
            }
            stringBuilder.append(", ");
            stringBuilder.append(x.getItemNo());
        }
        return stringBuilder.toString();
    }

}
