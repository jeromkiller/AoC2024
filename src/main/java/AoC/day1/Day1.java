package AoC.day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1 {
    public List<String> readInput(String filePath) {
        List<String> inputList = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                inputList.add(newLine);
            }
            reader.close();

        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return inputList;
    }

    public List<Integer> getNumbers(String text) {
        // scan each character
        List<Integer> res = new ArrayList<>();
        for(final char c : text.toCharArray()){
            if(c >= '1' && c <= '9')
                res.add(c - '0');
        }
        return res;
    }

    public int ammendNumbers(List<Integer> ints) {
        int val1 = ints.get(0);
        int val2 = ints.get(ints.size() -1);
        return val1 * 10 + val2;
    }

    public static void main(String args[]){
        System.out.println("Test");
        Day1 day1 = new Day1();
        List<String> input = day1.readInput("./src/main/resources/testInput.txt");
        List<Integer> results = new ArrayList<>();
        for(String line : input) {
            List<Integer> nums = day1.getNumbers(line);
            results.add(day1.ammendNumbers(nums));
        }
        System.out.println(results);
    }
}

