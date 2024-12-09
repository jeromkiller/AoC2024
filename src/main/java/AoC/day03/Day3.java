package AoC.day03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    public static List<String> readInput(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                lines.add(newLine);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> getMuls(String line) {
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        Matcher matcher = pattern.matcher(line);
        List<String> muls = new ArrayList<>();
        while(matcher.find()) {
            muls.add(matcher.group());
        }
        return muls;
    }

    public static List<String> getOps(String line) {
        String mulPattern = "(mul\\(\\d{1,3},\\d{1,3}\\))";
        String dontPattern = "(don't\\(\\))";
        String doPattern = "(do\\(\\))";
        Pattern pattern = Pattern.compile(mulPattern + "|" + dontPattern + "|" + doPattern);
        Matcher matcher = pattern.matcher(line);
        List<String> ops = new ArrayList<>();
        while(matcher.find()) {
            ops.add(matcher.group());
        }
        return ops;
    }

    public static List<String> filterOps(List<String> ops) {
        boolean enabled = true;
        List<String> filtered = new ArrayList<>();
        for(final String op : ops) {
            if(op.equals("do()")) {
                enabled = true;
            }
            else if(op.equals("don't()")) {
                enabled = false;
            }
            else if(enabled) {
                filtered.add(op);
            }
        }
        return filtered;
    }

    public static int doMul(String mul) {
        //extract the two numbers from each other
        String cleaned = mul.replace("mul(", "").replace(")", "");
        String[] split = cleaned.split(",");
        return Arrays.stream(split).mapToInt(Integer::valueOf).reduce(1, (a,b) -> a * b);
    }

    public static int star01(List<String> muls) {
        int total = 0;
        for(final String mul : muls) {
            total += Day3.doMul(mul);
        }
        return total;
    }

    public static int star02(List<String> ops) {
        List<String> muls = filterOps(ops);
        return star01(muls);
    }

    public static void main(String[] args) {
        List<String> lines = readInput("./src/main/java/AoC/day03/input.txt");
        List<String> muls = new ArrayList<>();
        for(final String line: lines) {
            final List<String> mul_list = getMuls(line);
            muls.addAll(mul_list);
        }

        int star1 = star01(muls);
        System.out.println("Star1: " + star1);

        List<String> ops = new ArrayList<>();
        for(final String line: lines) {
            final List<String> op_list = getOps(line);
            ops.addAll(op_list);
        }
        int star2 = star02(ops);
        System.out.println("Star2: " + star2);
    }

}
