package AoC.day13;

import AoC.day12.Day12;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day13 {

    @Value
    @AllArgsConstructor
    public static class Equation {
        long target;
        long a;
        long b;

        Equation mult_all(long val) {
            return new Equation(target * val, a * val, b * val);
        }

        Equation sub(Equation rhs) {
            return new Equation(target - rhs.target, a - rhs.a, b - rhs.b);
        }

        long solveForNonblank() {
            if(a == 0) {
                if(b == 0) {
                    return -1;
                }
                return target / b;
            }
            else {
                return target / a;
            }
        }

        Equation addToTarget(long val) {
            return new Equation(target + val, a, b);
        }

        static long solveFor_A(Equation lhs, Equation rhs) {
            Equation eq1 = lhs.mult_all(rhs.b);
            Equation eq2 = rhs.mult_all(lhs.b);

            Equation subtracted = eq1.sub(eq2);
            return subtracted.solveForNonblank();
        }

        static long solveFor_B(Equation lhs, Equation rhs) {
            Equation eq1 = lhs.mult_all(rhs.a);
            Equation eq2 = rhs.mult_all(lhs.a);

            Equation subtracted = eq1.sub(eq2);
            return subtracted.solveForNonblank();
        }

        boolean validate(long x, long y) {
            return target == x * a + y * b;
        }
    }

    @Value
    @AllArgsConstructor
    public static class EquationSet {
        Equation x;
        Equation y;

        public long getA() {
            return Equation.solveFor_A(x, y);
        }

        public long getB() {
            return Equation.solveFor_B(x, y);
        }

        public boolean hasSolution() {
            long a = getA();
            long b = getB();

            return x.validate(a, b) && y.validate(a, b);
        }

        public EquationSet addToTotal(long val) {
            return new EquationSet(x.addToTarget(val), y.addToTarget(val));
        }
    }

    public static int[] getNumbers(String input) {
        String[] splitStr = input.split(",");
        int num1 = Integer.parseInt(splitStr[0].replaceAll("[^0-9]", ""));
        int num2 = Integer.parseInt(splitStr[1].replaceAll("[^0-9]", ""));
        return new int[]{num1, num2};
    }

    public static List<EquationSet> readInput(String filePath) {
        List<EquationSet> equations = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if(line.isEmpty()) {
                    line = reader.nextLine();
                }
                int[] A_nums = getNumbers(line);
                int[] B_nums = getNumbers(reader.nextLine());
                int[] solve_nums = getNumbers(reader.nextLine());
                Equation X = new Equation(solve_nums[0], A_nums[0], B_nums[0]);
                Equation Y = new Equation(solve_nums[1], A_nums[1], B_nums[1]);
                equations.add(new EquationSet(X, Y));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return equations;
    }

    public static long getScore(EquationSet set) {
        if(!set.hasSolution()) {
            return 0;
        }

        return set.getA() * 3 + set.getB();
    }

    public static long star1(List<EquationSet> set) {
        return set.stream().map(Day13::getScore).mapToLong(Long::longValue).sum();
    }

    public static long star2(List<EquationSet> sets) {
        return sets.stream().map(set -> set.addToTotal(10000000000000L)).map(Day13::getScore).mapToLong(Long::longValue).sum();
    }

    public static void main(String[] args) {
        List<EquationSet> input = readInput("./src/main/java/AoC/day13/input.txt");
        long star1 = star1(input);
        System.out.println("star1: " + star1);

        long star2 = star2(input);
        System.out.println("star2: " + star2);
    }
}
