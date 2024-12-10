import AoC.day10.Day10;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day10Test {

    @Test
    void testStar1() {
        Day10.trailMap map = Day10.readInput("./src/main/java/AoC/day10/testInput.txt");
        int score = Day10.star1(map);
        Assertions.assertEquals(36, score);
    }

    @Test
    void testStar2() {
        Day10.trailMap map = Day10.readInput("./src/main/java/AoC/day10/testInput.txt");
        int score = Day10.star2(map);
        Assertions.assertEquals(81, score);
    }
}
