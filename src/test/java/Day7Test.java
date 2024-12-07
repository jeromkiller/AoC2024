import AoC.day7.Day7;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day7Test {
    @Test
    void star1Test() {
        final List<Day7.Calculation> input = Day7.readPagesInput("./src/main/java/AoC/day7/testInput.txt");
        Long star1 = Day7.star1(input);
        Assertions.assertEquals(3749, star1);
    }

    @Test
    void star2Test() {
        final List<Day7.Calculation> input = Day7.readPagesInput("./src/main/java/AoC/day7/testInput.txt");
        Long star2 = Day7.star2(input);
        Assertions.assertEquals(11387, star2);
    }

    @Test
    void star2TestRecursive() {
        final List<Day7.Calculation> input = Day7.readPagesInput("./src/main/java/AoC/day7/testInput.txt");
        Long star2 = Day7.star2_rec(input);
        Assertions.assertEquals(11387, star2);
    }
}
