import AoC.day13.Day13;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day13Test {


    @Test
    void equationTest() {
        Day13.Equation x = new Day13.Equation(8400, 94, 22);
        Day13.Equation y = new Day13.Equation(5400, 34, 67);
        Day13.EquationSet set = new Day13.EquationSet(x, y);

        Assertions.assertTrue(set.hasSolution());
    }

    @Test
    void equationTest_impossible() {
        Day13.Equation x = new Day13.Equation(12748, 26, 67);
        Day13.Equation y = new Day13.Equation(12176, 66, 21);
        Day13.EquationSet set = new Day13.EquationSet(x, y);

        Assertions.assertFalse(set.hasSolution());
    }

    @Test
    void star1Test() {
        List<Day13.EquationSet> input = Day13.readInput("./src/main/java/AoC/day13/testInput.txt");
        long star1 = Day13.star1(input);
        Assertions.assertEquals(480, star1);
    }

    @Test
    void star12est() {
        List<Day13.EquationSet> input = Day13.readInput("./src/main/java/AoC/day13/testInput.txt");
        long star1 = Day13.star2(input);
        Assertions.assertEquals(875318608908L, star1);
    }
}
