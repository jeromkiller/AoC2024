import AoC.day14.Day14;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day14Test {

    @Test
    void star1Test() {
        List<Day14.Robot> robots = Day14.readInput("./src/main/java/AoC/day14/testInput.txt");
        int star1 = Day14.star1(robots, new Day14.Vec(11, 7));
        Assertions.assertEquals(12, star1);
    }
}
