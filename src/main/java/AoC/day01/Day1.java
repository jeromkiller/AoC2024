package AoC.day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day1 {
    public List<Integer>[] readInput(String filePath) {
        List<Integer> row1 = new ArrayList<>();
        List<Integer> row2 = new ArrayList<>();

        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                newLine = newLine.replaceAll(" +", " ");
                String[] splitvals = newLine.split(" ");
                row1.add(Integer.valueOf(splitvals[0]));
                row2.add(Integer.valueOf(splitvals[1]));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return new List[]{row1, row2};
    }

    public int star1(List<Integer> row1, List<Integer> row2) {
        // sort the lists
        List<Integer> sorted1 = row1.stream().sorted().collect(Collectors.toList());
        List<Integer> sorted2 = row2.stream().sorted().collect(Collectors.toList());

        // compare the values
        List<Integer> differences = new ArrayList<>();
        for(int i = 0; i < sorted1.size(); i++) {
            final int val1 = sorted1.get(i);
            final int val2 = sorted2.get(i);
            int dif = Math.abs(val1 - val2);
            differences.add(dif);
        }

        // sum the differences
        final int sum = differences.stream().mapToInt(Integer::intValue).sum();
        return sum;
    }

    public int star2(List<Integer> row1, List<Integer> row2) {
        List<Integer> similarities = new ArrayList<>();
        for(Integer val : row1) {
            int similarity = Collections.frequency(row2, val) * val;
            similarities.add(similarity);
        }

        final int sum = similarities.stream().mapToInt(Integer::intValue).sum();
        return sum;
    }

    public static void main(String args[]){
        System.out.println("Test");
        Day1 day1 = new Day1();
        List<Integer>[] input = day1.readInput("./src/main/java/AoC/day01/input.txt");
        int sum1 = day1.star1(input[0], input[1]);
        System.out.println("Star 1: " + sum1);
        int sum2 = day1.star2(input[0], input[1]);
        System.out.println("Star 2: " + sum2);
    }
}

