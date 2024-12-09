package AoC.day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day2 {
    public static List<List<Integer>> readInput(String filePath) {
        List<List<Integer>> reports = new ArrayList<>();

        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                List<String> splitLine = List.of(newLine.split(" "));
                List<Integer> report = splitLine.stream().map(Integer::valueOf).collect(Collectors.toList());
                reports.add(report);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return reports;
    }

    public static boolean checkSafety(List<Integer> report) {
        if(Objects.equals(report.get(0), report.get(1)))
            return false;

        boolean increasing = (report.get(1) - report.get(0)) > 0;
        boolean safe = true;

        for(int i = 0; i < report.size() - 1; i++) {
            final int val1 = report.get(i);
            final int val2 = report.get(i + 1);
            final int dif = val2 - val1;

            safe &= dif != 0;
            safe &= (dif > 0) == increasing;
            safe &= (Math.abs(dif) >= 1) && (Math.abs(dif) <= 3);
        }
        return safe;
    }

    public static boolean checkSafetyDampened(List<Integer> report) {
        // first check the report undampened
        if(checkSafety(report)) {
            return true;
        }

        // apply dampening
        for(int i = 0; i < report.size(); i++) {
            List<Integer> dampened = new ArrayList<>(report);
            dampened.remove(i);
            if(checkSafety(dampened)) {
                return true;
            }
        }
        return false;
    }

    public static int star01(List<List<Integer>> reports)
    {
        int totalSafe = 0;
        for(final List<Integer> report : reports) {
            if (checkSafety(report)) {
                totalSafe++;
            }
        }
        return totalSafe;
    }

    public static int star02(List<List<Integer>> reports) {
        int totalSafe = 0;
        for(final List<Integer> report : reports) {
            if (checkSafetyDampened(report)) {
                totalSafe++;
            }
        }
        return totalSafe;
    }

    public static void main(String[] args) {
        List<List<Integer>> reports = readInput("./src/main/java/AoC/day2/input.txt");
        final int star1 = star01(reports);
        System.out.println("Star1: " + star1);
        final int star2 = star02(reports);
        System.out.println("Star2: " + star2);
    }
}
