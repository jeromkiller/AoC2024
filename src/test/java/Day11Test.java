import AoC.day11.Day11;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class Day11Test {

    @Test
    void AddStoneTest() {
        HashMap<Long, Long> testMap = new HashMap<>();
        Day11.AddStone(testMap, 1, 1);
        Day11.AddStone(testMap, 1, 1);
        Day11.AddStone(testMap, 123, 123);
        HashMap<Long, Long> expected = new HashMap<>();
        expected.put(1L, 2L);
        expected.put(123L, 123L);
        Assertions.assertEquals(expected, testMap);
    }

    @Test
    void BlinkTest() {
        HashMap<Long, Long> testMap = new HashMap<>();
        testMap.put(0L, 1L);
        testMap.put(2L, 2L);
        testMap.put(99L, 3L);
        testMap = Day11.blink(testMap);

        HashMap<Long, Long> expected = new HashMap<>();
        expected.put(1L, 1L);
        expected.put(4048L, 2L);
        expected.put(9L, 6L);
        Assertions.assertEquals(expected, testMap);
    }

    @Test
    void inputTest() {
        HashMap<Long, Long> testMap = Day11.readInput("./src/main/java/AoC/day11/testInput.txt");
        HashMap<Long, Long> expected = new HashMap<>();
        expected.put(125L, 1L);
        expected.put(17L, 1L);
        Assertions.assertEquals(expected, testMap);
    }

    @Test
    void star1Test() {
        HashMap<Long, Long> input = Day11.readInput("./src/main/java/AoC/day11/testInput.txt");
        long star1 = Day11.star1(input);
        Assertions.assertEquals(55312, star1);
    }

}
