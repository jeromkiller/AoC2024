package AoC.day07;

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
        MULTIPLY,
        ADD,
        CONCAT
    }

    // order operations from quickest growing to slowest
    static List<OPERATION> star1_ops = List.of(OPERATION.MULTIPLY, OPERATION.ADD);
    static List<OPERATION> star2_ops = List.of(OPERATION.MULTIPLY, OPERATION.CONCAT, OPERATION.ADD);

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
            total = doOp(op, total, val);
        }
        return total == calc.getTotal();
    }

    public static long doOp(OPERATION op, long lhs, long rhs) {
        switch (op) {
            case MULTIPLY:
                return lhs * rhs;
            case ADD:
                return lhs + rhs;
            case CONCAT:
                return Long.parseLong(lhs + "" + rhs);
        }
        assert false;
        return 0L;  // shouldn't happen
    }

    public static boolean check_possible(final Calculation calc, List<OPERATION> ops) {
        int total_permutations = (int) Math.pow(ops.size(), calc.getValues().size() - 1);
        final boolean isPossible = IntStream.range(0, total_permutations).anyMatch(permutation -> Day7.checkPermutation(calc, permutation, ops));
        return isPossible;
    }

    public static Long star1(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible(calc, star1_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    public static Long star1_rec(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible_rec(calc, calc.getValues().get(0), 1, star1_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    public static Long star2(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible(calc, star2_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    public static Long star2_rec(final List<Calculation> calcs) {
        return calcs.stream().filter(calc -> check_possible_rec(calc, calc.getValues().get(0), 1, star2_ops)).map(Calculation::getTotal).reduce(0L, Long::sum);
    }

    public static boolean check_possible_rec(final Calculation calc, long val, int index, List<OPERATION> ops) {
        if( val > calc.getTotal()) {
            // early return
            // we are already larger than the required value, no need to head further down this leg of hte tree
            return false;
        }
        final long rhs = calc.getValues().get(index);
        final boolean lastValue = index == calc.getValues().size() -1;
        for(OPERATION op : ops) {
            long tryVal = doOp(op, val, rhs);
            if(tryVal == calc.getTotal() && lastValue) {
                return true;
            }
            if(!lastValue) {
                if(check_possible_rec(calc, tryVal, index + 1, ops)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        final List<Calculation> input = readPagesInput("./src/main/java/AoC/day07/input.txt");

        // star 1 would also go a lot faster recursively, but for 2^n options, you don't really feel how slow it is
        Long star1 = star1(input);
        System.out.println("star1: " + star1);

        // however, when your options grow to 3^n, you might notice a small difference :P
        long s2_start_rec = System.currentTimeMillis();
        Long star2_rec = star2_rec(input);
        long s2_finish_rec = System.currentTimeMillis();
        long s2_timeElapsed_rec = s2_finish_rec - s2_start_rec;
        System.out.println("\nstar2 recursively: " + star2_rec);
        System.out.println("Time taken: " + s2_timeElapsed_rec + "ms");

        long s2_start_it = System.currentTimeMillis();
        Long star2 = star2(input);
        long s2_finish_it = System.currentTimeMillis();
        long s2_timeElapsed_it = s2_finish_it - s2_start_it;
        System.out.println("\nstar2 iteratively: " + star2);
        System.out.println("Time taken: " + s2_timeElapsed_it + "ms");
    }
}
