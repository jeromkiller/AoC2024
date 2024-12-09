import AoC.day02.Day2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class Day2Test {
    @Test
    void readInput() {
        List<List<Integer>> readInput = Day2.readInput("./src/main/java/AoC/day02/testInput.txt");
        List<List<Integer>> expected = List.of(
                List.of(7, 6, 4, 2, 1),
                List.of(1, 2, 7, 8, 9),
                List.of(9, 7, 6, 2, 1),
                List.of(1, 3, 2, 4, 5),
                List.of(8, 6, 4, 4, 1),
                List.of(1, 3, 6, 7, 9)
        );
        Assertions.assertIterableEquals(expected, readInput);
    }

    @Test
    void validity_1() {
        List<List<Integer>> readInput = Day2.readInput("./src/main/java/AoC/day02/testInput.txt");
        List<Boolean> results = readInput.stream().map(Day2::checkSafety).collect(Collectors.toList());
        List<Boolean> expected = List.of(
                true,
                false,
                false,
                false,
                false,
                true
        );
        Assertions.assertIterableEquals(expected, results);
    }

    @Test
    void validity_2() {
        List<List<Integer>> readInput = Day2.readInput("./src/main/java/AoC/day02/testInput.txt");
        List<Boolean> results = readInput.stream().map(Day2::checkSafetyDampened).collect(Collectors.toList());
        List<Boolean> expected = List.of(
                true,
                false,
                false,
                true,
                true,
                true
        );
        Assertions.assertIterableEquals(expected, results);
    }

    @Test
    void star01() {
        List<List<Integer>> readInput = Day2.readInput("./src/main/java/AoC/day02/testInput.txt");
        int sum = Day2.star01(readInput);
        Assertions.assertEquals(2, sum);
    }

    @Test
    void star02() {
        List<List<Integer>> readInput = Day2.readInput("./src/main/java/AoC/day02/testInput.txt");
        int sum = Day2.star02(readInput);
        Assertions.assertEquals(4, sum);
    }

}
