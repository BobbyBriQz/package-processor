package com.mercateo.processor.utils;

import com.mercateo.processor.models.Item;
import com.mercateo.processor.models.PackagingCandidate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleParser implements Parser{

    /**
     * Method that retrieves testcases from the input file
     * @throws IllegalArgumentException when it cannot retrieve testcases from file
     * */
    @Override
    public List<String> getTestCases(String path) {
        try{
            //Set Encoding to UTF-8
            Charset inputCharset = StandardCharsets.UTF_8;
            //Use bufferReader to read the file and extract text line by line
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), inputCharset));
            String line;
            List<String> result = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
            result = result.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
            return result;
        }catch (Exception e){
            throw new IllegalArgumentException("Could not extract test cases from file: " + e.getMessage());
        }
    }

    /**
     * Extracts the list of items from it's text representation
     * @param testCases List of Strings, each representing a testcase
     * @return a list of PackagingCandidates
     * **/
    @Override
    public List<PackagingCandidate> extractPackagingCandidates(List<String> testCases) {
        List<PackagingCandidate> candidates = new ArrayList<>();
        for(String testCase: testCases){
            //Split the test case into 2 ==> Weight | list of items
            String[] weightXItems = testCase.split(" : ");

            //Extract weight from first part of the split result
            int weight = Integer.parseInt(weightXItems[0].trim());

            //Extract list of items from second part of the split result
            List<Item> items = extractItems(weightXItems[1]);

            candidates.add(new PackagingCandidate(weight, items));
        }
        return candidates;
    }

    /**
     * Extracts the list of items from its text representation
     * @param input String of all items in text format
     * @return a list of Item objects
     * **/
    private List<Item> extractItems(String input) {
        String[] i = input.split(" ");
        List<Item> items = new ArrayList<>();

        for(String s: i) {
            int itemNo = extractItemNo(s);
            double weight = extractWeight(s);
            int price = extractCost(s);
            items.add(new Item(itemNo, weight, price));
        }

        items.sort(Comparator.comparingDouble(Item::getWeight));
        return items;
    }

    /**
     * Use regex to extract item number between a ( and , in text
     * @param item text representation of an item
     * @return item number of the item
     * */
    private int extractItemNo(String item) {
        Pattern p = Pattern.compile("\\((.*?),");
        Matcher m = p.matcher(item);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalArgumentException("Item No is wrongly formatted in: " + item);

    }

    /**
     * Use regex to extract weight between two commas in text
     * @param item text representation of an item
     * @return weight of the item
     * */
    private double extractWeight(String item) {
        Pattern p = Pattern.compile(",(.*?),");
        Matcher m = p.matcher(item);
        if(m.find()) {
            return Double.parseDouble(m.group(1));
        }
        throw new IllegalArgumentException("Weight is wrongly formatted in: " + item);
    }

    /**
     * Use regex to extract price between  € and )
     * @param item text representation of an item
     * @return cost of the item
     * */
    private int extractCost(String item) {
        Pattern p = Pattern.compile("€(.*?)\\)");
        Matcher m = p.matcher(item);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalArgumentException("Price is wrongly formatted in: " + item);
    }
}
