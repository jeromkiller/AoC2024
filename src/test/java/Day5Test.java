import AoC.day05.Day5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day5Test {

    @Test
    void testBeforeCheck() {
        List<Integer> pages = List.of(1, 2, 3, 4);
        int res = Day5.isBefore(pages, 1, 2);
        Assertions.assertTrue(res < 0);
        res = Day5.isBefore(pages, 3, 999);
        Assertions.assertTrue(res < 0);
        res = Day5.isBefore(pages, 999, 3);
        Assertions.assertTrue(res < 0);
        res = Day5.isBefore(pages, 3, 1);
        Assertions.assertFalse(res < 0);
    }

    @Test
    void testLocationCheck() {
        List<Integer> pages = List.of(1, 2, 3, 4);
        HashMap<Integer, HashSet<Integer>> ordering = new HashMap<>();
        ordering.put(1, new HashSet<>(List.of(2, 3, 4)));
        ordering.put(2, new HashSet<>(List.of(3, 6)));

        boolean res = Day5.isInRightSpot(pages, 1, new HashSet<>(List.of(2, 3, 4)));
        Assertions.assertTrue(res);
        res = Day5.isInRightSpot(pages, 2, new HashSet<>(List.of(3, 999)));
        Assertions.assertTrue(res);
        res = Day5.isInRightSpot(pages, 999, new HashSet<>(List.of(1, 3)));
        Assertions.assertTrue(res);
        res = Day5.isInRightSpot(pages, 3, new HashSet<>(List.of(1, 4)));
        Assertions.assertFalse(res);
    }

    @Test
    void testOrderCheck() {
        List<Integer> pages = List.of(1, 2, 3, 4);
        HashMap<Integer, HashSet<Integer>> ordering = new HashMap<>();
        ordering.put(1, new HashSet<>(List.of(2, 3, 4)));
        ordering.put(2, new HashSet<>(List.of(3, 6)));

        boolean res = Day5.inOrder(pages, ordering);
    }

    @Test
    void testTestInputOrder() {
        List<List<Integer>> pages_list = Day5.readPagesInput("./src/main/java/AoC/Day5/testPages.txt");
        HashMap<Integer, HashSet<Integer>> ordering = Day5.readOrderInput("./src/main/java/AoC/Day5/testOrder.txt");

        List<Boolean> results = pages_list.stream().map(pages -> Day5.inOrder(pages, ordering)).collect(Collectors.toList());
        List<Boolean> expected = List.of(true, true, true, false, false, false);

        Assertions.assertEquals(expected, results);
    }

    @Test
    void testFindMiddle() {
        List<Integer> values = List.of(1, 2, 3, 4, 5);
        int mid = Day5.findMiddleNum(values);
        Assertions.assertEquals(3, mid);
    }

    @Test
    void testSorting() {
        List<Integer> values = new ArrayList<>(List.of(97,13,75,29,47));
        HashMap<Integer, HashSet<Integer>> ordering = Day5.readOrderInput("./src/main/java/AoC/Day5/testOrder.txt");

        List<Integer> expected = List.of(97,75,47,29,13);
        List<Integer> sorted = Day5.sort(values, ordering);

        String expectedString = expected.toString();
        String sortedString = sorted.toString();
        Assertions.assertEquals(expectedString, sortedString);
    }

    @Test
    void testStar1() {
        List<List<Integer>> pages_list = Day5.readPagesInput("./src/main/java/AoC/Day5/testPages.txt");
        HashMap<Integer, HashSet<Integer>> ordering = Day5.readOrderInput("./src/main/java/AoC/Day5/testOrder.txt");

        int result = Day5.star1(pages_list, ordering);

        Assertions.assertEquals(143, result);
    }

    @Test
    void testStar2() {
        List<List<Integer>> pages_list = Day5.readPagesInput("./src/main/java/AoC/Day5/testPages.txt");
        HashMap<Integer, HashSet<Integer>> ordering = Day5.readOrderInput("./src/main/java/AoC/Day5/testOrder.txt");

        int result = Day5.star2(pages_list, ordering);
        Assertions.assertEquals(123, result);
    }
}
