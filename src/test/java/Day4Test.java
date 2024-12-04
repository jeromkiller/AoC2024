import AoC.day4.Day4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class Day4Test {
    @Test
    void parseTest() {
        List<String> input = List.of("123", "456", "789");
        Day4.CrossWord cw = new Day4.CrossWord(input);
        List<List<Character>> control = List.of(
                List.of('1', '2', '3'),
                List.of('4', '5', '6'),
                List.of('7', '8', '9')
        );
        Assertions.assertIterableEquals(control, cw.data);
    }

    @Test
    void borderTest() {
        List<String> input = List.of("123", "456", "789");
        Day4.CrossWord cw = new Day4.CrossWord(input);
        cw.addBorder(2);
        List<List<Character>> control = List.of(
                List.of('=', '=', '=', '=', '=', '=', '='),
                List.of('=', '=', '=', '=', '=', '=', '='),
                List.of('=', '=', '1', '2', '3', '=', '='),
                List.of('=', '=', '4', '5', '6', '=', '='),
                List.of('=', '=', '7', '8', '9', '=', '='),
                List.of('=', '=', '=', '=', '=', '=', '='),
                List.of('=', '=', '=', '=', '=', '=', '=')

        );
        Assertions.assertIterableEquals(control, cw.data);
    }

    @Test
    void toStringTest() {
        List<String> input = List.of("123", "456", "789");
        Day4.CrossWord cw = new Day4.CrossWord(input);
        String printable = cw.toString(false);
        String expected = "123\n456\n789\n";
        Assertions.assertEquals(printable, expected);
    }

    @Test
    void directionTest() {
        List<String> directions = Day4.readInput("./src/main/java/AoC/day4/directionsInput.txt");
        Day4.CrossWord cw = new Day4.CrossWord(directions);
        final int border = 4;
        cw.addBorder(border);
        final int center = 3 + border;
        List<Character> word = List.of('X', 'M', 'A', 'S');

        HashMap<List<Integer>, Boolean> results = new LinkedHashMap<>();
        for(int y = -1; y < 2; y++) {
            for(int x = -1; x < 2; x++){
                boolean found = Day4.findLetters(cw, center, center, y, x, 1, word, new HashSet<>());
                results.put(List.of(x, y), found);
            }
        }

        HashMap<List<Integer>, Boolean> expected = new LinkedHashMap<>();
        expected.put(List.of(-1, -1), true);
        expected.put(List.of(-1, 0), true);
        expected.put(List.of(-1, 1), true);
        expected.put(List.of(0, -1), true);
        expected.put(List.of(0, 0), false);
        expected.put(List.of(0, 1), true);
        expected.put(List.of(1, -1), true);
        expected.put(List.of(1, 0), true);
        expected.put(List.of(1, 1), true);

        Assertions.assertEquals(expected, results);
    }

    @Test
    void testToString() {
        List<String> input = Day4.readInput("./src/main/java/AoC/day4/testInput.txt");
        List<String> control = Day4.readInput("./src/main/java/AoC/day4/testInputCheck.txt");
        Day4.CrossWord testCW = new Day4.CrossWord(input);
        Day4.CrossWord controlCW = new Day4.CrossWord(control);

        // solve the crossword, and compare to the control
        Day4.star01(testCW, "XMAS");
        String result = testCW.toString(true);
        String expected = controlCW.toString();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testStar1() {
        List<String> input = Day4.readInput("./src/main/java/AoC/day4/testInput.txt");
        Day4.CrossWord cw = new Day4.CrossWord(input);
        Day4.star01(cw, "XMAS");
        Assertions.assertEquals(18, cw.numFound);
    }

    @Test
    void testStar2() {
        List<String> input = Day4.readInput("./src/main/java/AoC/day4/testInput.txt");
        Day4.CrossWord cw = new Day4.CrossWord(input);
        Day4.star02(cw);
        Assertions.assertEquals(9, cw.numFound);
    }
}
