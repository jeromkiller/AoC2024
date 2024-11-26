import AoC.day1.Day1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Day1Test {
    @Test
    public void readInput() {
        Day1 day1 = new Day1();
        List<String> readInput = day1.readInput("./src/main/resources/testInput.txt");
        List<String> testInput = List.of(
                "1abc2",
                "pqr3stu8vwx",
                "a1b2c3d4e5f",
                "treb7uchet");
        Assertions.assertEquals(testInput, readInput);
    }

    @Test
    public void oneNumber() {
        Day1 day1 = new Day1();
        List<Integer> testInput = List.of(7);
        List<Integer> res = day1.getNumbers("treb7uchet");
        Assertions.assertEquals(testInput, res);
    }

    @Test
    public void twoNumber() {
        Day1 day1 = new Day1();
        List<Integer> testInput = List.of(1, 2);
        List<Integer> res = day1.getNumbers("1abc2");
        Assertions.assertEquals(testInput, res);
    }

    @Test
    public void multiNumber() {
        Day1 day1 = new Day1();
        List<Integer> testInput = List.of(2, 3, 4 ,5);
        List<Integer> res = day1.getNumbers("a1b2c3d4e5f");
        Assertions.assertEquals(testInput, res);
    }
}