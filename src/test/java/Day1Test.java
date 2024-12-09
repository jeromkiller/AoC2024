import AoC.day01.Day1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1Test {
    @Test
    public void readInput() {
        Day1 day1 = new Day1();
        List<Integer>[] readInput = day1.readInput("./src/main/java/AoC/day1/testInput.txt");
        List<Integer>[] expected = new List[]{List.of(3, 4, 2, 1, 3, 3), List.of(4, 3, 5 ,3, 9, 3)};
        Assertions.assertArrayEquals(expected, readInput);
    }

    @Test
    public void sort() {
        Day1 day1 = new Day1();
        List<Integer> list = new ArrayList(List.of(4, 3, 5 ,3, 9, 3));
        Collections.sort(list);

        List<Integer> expected = List.of(3, 3, 3, 4, 5, 9);
        Assertions.assertEquals(expected, list);
    }

    @Test void star01() {
        Day1 day1 = new Day1();
        List<Integer>[] readInput = day1.readInput("./src/main/java/AoC/day1/testInput.txt");
        int sum = day1.star1(readInput[0], readInput[1]);
        Assertions.assertEquals(11, sum);
    }

    @Test void star02() {
        Day1 day1 = new Day1();
        List<Integer>[] readInput = day1.readInput("./src/main/java/AoC/day1/testInput.txt");
        int sum = day1.star2(readInput[0], readInput[1]);
        Assertions.assertEquals(31, sum);
    }
}