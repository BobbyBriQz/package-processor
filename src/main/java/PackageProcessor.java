


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * PLEASE NOTE: that this branch only solves the problem using a SINGLE java file for easier compilation, review and testing
 * For a more modular approach please switch to the "full-implementation" branch
 * **/
public class PackageProcessor {
    private static final int MAXIMUM_WEIGHT_ALLOWED = 100;
    private static final int MAXIMUM_NO_OF_ITEMS = 15;
    private static final int MAXIMUM_COST_OF_ITEM = 100;
    private final String testCasePath;

    public PackageProcessor(String testCasePath){
        this.testCasePath = testCasePath;
    }

    public static void main(String[] args){
        //Validate that program is ran with a path argument
        if (args.length == 0) throw new IllegalArgumentException("Please specify a path");

        PackageProcessor processor = new PackageProcessor(args[0]);
        processor.processAndPrintPackage();
    }

    /**
     * Method that triggers all operations to be carried out
     * */
    public void processAndPrintPackage() {
        //Extract all test cases from file
        List<String> testCases = getTestCases();
        //Remove line breaks or new lines from the extracted testcases
        testCases = testCases.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        for(String testCase : testCases){
            //process each test case
            System.out.println(processPackage(testCase));
            System.out.println();
        }
    }

    /**
     * Method that retrieves testcases from the input file
     * @throws IllegalArgumentException when it cannot retrieve testcases from file
     * */
    private List<String> getTestCases() {
        try {
            //Set Encoding to UTF-8
            Charset inputCharset = StandardCharsets.UTF_8;
            //Use bufferReader to read the file and extract text line by line
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(testCasePath), inputCharset));
            String line;
            List<String> result = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
            return result;
        }catch (Exception e){
            throw new IllegalArgumentException("Could not extract test cases from file: " + e.getMessage());
        }
    }

    /**
     * Method processes the test case and returns item numbers of eligible items
     * It uses tabulation method of dynamic programming to fill the grid and backtracks to get the list of items in
     * the package
     * Time complexity is O(rc)
     * where r (row) = number of items and c (column) is = weight capacity of the package
     *
     * @param testCase text representation of the weight and list of items
     * @return String of concatenated item numbers as specified in documentation
     * */
    private String processPackage(String testCase){
        //Split the test case into 2 ==> Weight | list of items
        String[] weightXItems = testCase.split(" : ");

        //Extract weight from first part of the split result
        int weight = Integer.parseInt(weightXItems[0].trim());

        //Extract list of items from second part of the split result
        List<Item> items = extractItems(weightXItems[1]);
        validate(weight, items);

        int n = items.size();
        int row,column;
        //2-dimensional grid for tabulation
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
        //int costOfItems = String.valueOf(K[n][weight]);
        return getPackagedItems(grid,items);
    }

    private String getPackagedItems(int[][] grid, List<Item> items){
        List<Integer> itemNumbers = new ArrayList<>();

        int row = grid.length - 1;
        int column = grid[0].length -1;

        while (row > 0){
            if(grid[row][column] == grid[row-1][column]) {
                //Move one step up as the particular row (or item) was not used in getting the result
                row -= 1;
            }else{
                //Row (or Item) was used to get the
                Item item = items.get(row-1);
                itemNumbers.add(item.getItemNo());
                column -= Math.round(item.getWeight());
                row -= 1;
            }

            if(column == 0)
                break;
        }

        if(itemNumbers.isEmpty()) return "-";
        Collections.sort(itemNumbers);
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer x : itemNumbers){
            if(stringBuilder.length() == 0){
                stringBuilder.append(x);
                continue;
            }
            stringBuilder.append(", ");
            stringBuilder.append(x);
        }
        return stringBuilder.toString();
    }

    /**
     * Extracts the list of items from it's text representation
     * @param itemsInText String of all items in text format
     * @return a list of Item objects
     * **/
    private List<Item> extractItems(String itemsInText) {
        String[] i = itemsInText.split(" ");
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
     * Method validates that the input is in compliance with the specified constraints
     * The constraints are:
     *
     * The maximum weight that a package can hold must be <= 100.
     * There may be up to 15 items you can to choose from.
     * The maximum weight of an item should be <= 100.
     * The maximum cost of an item should be <= €100.
     * */
    private void validate(int weight, List<Item> items) {

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


    /**
     * Use regex to extract item number between a ( and , in text
     * @param item text representation of an item
     * @return item number of the item
     * */
    private int extractItemNo(String item){
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
    private double extractWeight(String item){
        Pattern p = Pattern.compile(",(.*?),");
        Matcher m = p.matcher(item);
        if(m.find()) {
            return Double.parseDouble(m.group(1));
        }
        throw new IllegalArgumentException("Weight is wrongly formatted in: " + item);
    }

    /**
     * Use regex to extract price between a € and )
     * @param item text representation of an item
     * @return cost of the item
     * */
    private int extractCost(String item){
        Pattern p = Pattern.compile("€(.*?)\\)");
        Matcher m = p.matcher(item);
        if(m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalArgumentException("Price is wrongly formatted in: " + item);
    }

    /**
     * Item Model class
     * */
    static class Item{

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

        @Override
        public String toString() {
            return "Item{" + "itemNo=" + itemNo + ", weight=" + weight + ", price=" + cost + '}';
        }
    }

}
