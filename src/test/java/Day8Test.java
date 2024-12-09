import AoC.day08.Day8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

public class Day8Test {
    @Test
    void addTest() {
        Day8.Vec pos = new Day8.Vec(5, 5);
        Day8.Vec dir = new Day8.Vec(1, 2);
        pos = Day8.Vec.add(pos, dir);
        Day8.Vec expected = new Day8.Vec(6, 7);
        Assertions.assertEquals(expected, pos);
    }

    @Test
    void subTest() {
        Day8.Vec pos = new Day8.Vec(5, 5);
        Day8.Vec dir = new Day8.Vec(6, 7);
        pos = Day8.Vec.subtract(pos, dir);
        Day8.Vec expected = new Day8.Vec(-1, -2);
        Assertions.assertEquals(expected, pos);
    }

    @Test
    void genAntiNodeTest() {
        List<Day8.Vec> antennas = List.of(new Day8.Vec(6, 5), new Day8.Vec(8, 8), new Day8.Vec(9, 9));
        HashSet<Day8.Vec> antiNodes = new HashSet<>(Day8.genFrequiencyAntiNode(antennas, 10, 10));
        HashSet<Day8.Vec> expected = new HashSet<>(List.of(new Day8.Vec(4, 2), new Day8.Vec(3, 1), new Day8.Vec(7, 7)));
        Assertions.assertEquals(expected, antiNodes);
    }

    @Test
    void star1Test() {
        Day8.map data = Day8.readInput("./src/main/java/AoC/day08/testInput.txt");
        int numAntiNodes = Day8.star1(data);
        Assertions.assertEquals(14, numAntiNodes);
    }

    @Test
    void RessonanceTest() {
        Day8.map data = Day8.readInput("./src/main/java/AoC/day08/resonanceInput.txt");
        int numAntiNodes = Day8.star2(data);
        Assertions.assertEquals(9, numAntiNodes);
    }

    @Test
    void star2Test() {
        Day8.map data = Day8.readInput("./src/main/java/AoC/day08/testInput.txt");
        int numAntiNodes = Day8.star2(data);
        Assertions.assertEquals(34, numAntiNodes);
    }
}
