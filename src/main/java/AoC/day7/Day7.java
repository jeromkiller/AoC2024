package AoC.day7;

import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Day7 {

    @Value
    public static class Calculation {
        long total;
        ArrayList<Long> values;
    }

    public enum OPERATION {
        MULT,
        ADD,
        CONCAT
    }

    static List<OPERATION> star1_ops = List.of(OPERATION.MULT, OPERATION.ADD);
    static List<OPERATION> star2_ops = List.of(OPERATION.MULT, OPERATION.ADD, OPERATION.CONCAT);

    public static List<Calculation> readPagesInput(String filePath) {
        List<Calculation> calculations = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                String[] total_split = newLine.split(": ");
                long total = Long.parseLong(total_split[0]);
                List<Long> values = Arrays.stream(total_split[1].split(" ")).map(Long::valueOf).collect(Collectors.toList());
                calculations.add(new Calculation(total, new ArrayList<>(values)));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return calculations;
    }

    public static OPERATION getOperation(int permutation, int index, List<OPERATION> ops) {
        // gets the base N value of the I'th operation
        int numOps = ops.size();
        int isolatedIndex =  ((permutation / (int) Math.pow(numOps, index - 1)) % numOps);
        return ops.get(isolatedIndex);
    }

    public static boolean checkPermutation(Calculation calc, int permutation, List<OPERATION> ops) {
        long total = calc.getValues().get(0);
        for(int i = 1; i < calc.getValues().size(); i++) {
            final long val = calc.getValues().get(i);
            //final boolean doMult = (permutation & (1 << i-1)) == 0;
            OPERATION op = getOperation(permutation, i, ops);
            switch (op) {
                case MULT:
                    total *= val;
                    break;
                case ADD:
                    total += val;
                    break;
                case CONCAT:
                    total = Long.parseLong(total + "" + val);
            }
        }
        return total == calc.getTotal();
    }

    public static boolean check_possible(final Calculation calc, List<OPERATION> ops) {
        int total_permutations = (int) Math.pow(ops.size(), calc.getValues().size() - 1);
        final boolean isPossible = IntStream.range(0, total_permutations).anyMatch(permutation -> Day7.checkPermutation(calc, permutation, ops));
        return isPossible;
    }

    public static Long star1(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible(calc, star1_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    public static Long star2(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible(calc, star2_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    // took an iterative approach could be optimized using a recursive approach
    // but hey, at least it uses constant memory
    public static void main(String[] args) {
        final List<Calculation> input = readPagesInput("./src/main/java/AoC/day7/input.txt");
        Long star1 = star1(input);
        System.out.println("star1: " + star1);

        Long star2 = star2(input);
        System.out.println("star2: " + star2);
    }
}
