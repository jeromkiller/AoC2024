package AoC.day11;

import AoC.day10.Day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {

    public static HashMap<Long, Long> readInput(String filePath) {
        HashMap<Long, Long> stones = new HashMap<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                List<Integer> row = Arrays.stream(newLine.split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                for(int stone_val : row) {
                    AddStone(stones, stone_val, 1);
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return stones;
    }

    public static void AddStone(HashMap<Long, Long> stones, long stone_number, long amount) {
        if(!stones.containsKey(stone_number)) {
            stones.put(stone_number, 0L);
        }
        stones.put(stone_number, stones.get(stone_number) + amount);
    }

    public static HashMap<Long, Long> blink(final HashMap<Long, Long> stones) {
        HashMap<Long, Long> new_stones = new HashMap<>();
        for(Map.Entry<Long, Long> stone_count : stones.entrySet()) {
            // check 0
            if(stone_count.getKey() == 0) {
                AddStone(new_stones, 1L, stone_count.getValue());
                continue;
            }

            // check even number len
            String num_string = stone_count.getKey().toString();
            int num_length = num_string.length();
            if(num_length % 2 == 0) {
                long lhs_stone = Long.parseLong(num_string.substring(0, num_length / 2));
                long rhs_stone = Long.parseLong(num_string.substring(num_length / 2, num_length));
                AddStone(new_stones, lhs_stone, stone_count.getValue());
                AddStone(new_stones, rhs_stone, stone_count.getValue());
                continue;
            }

            // times 2024
            AddStone(new_stones, stone_count.getKey() * 2024, stone_count.getValue());
        }
        return new_stones;
    }

    public static HashMap<Long, Long> doBlinks(HashMap<Long, Long> initial_stones, int num_blinks) {
        HashMap<Long, Long> stones = new HashMap<>(initial_stones);
        for(int i = 0; i < num_blinks; i++) {
            stones = blink(stones);
        }
        return stones;
    }

    public static long star1(HashMap<Long, Long> initial_stones) {
        return doBlinks(initial_stones, 25).values().stream().reduce(0L, Long::sum);
    }

    public static long star2(HashMap<Long, Long> initial_stones) {
        return doBlinks(initial_stones, 75).values().stream().reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
        HashMap<Long, Long> input = readInput("./src/main/java/AoC/day11/input.txt");
        long star1 = star1(input);
        System.out.println("Star1: " + star1);

        long star2 = star2(input);
        System.out.println("Star2: " + star2);
    }
}
