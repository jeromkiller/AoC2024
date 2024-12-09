import AoC.day03.Day3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day3Test {

    static final String testString1 = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
    static final String testString2 = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

    @Test
    void matchMulTest() {
        List<String> muls = Day3.getMuls(testString1);
        List<String> expected = List.of("mul(2,4)","mul(5,5)","mul(11,8)","mul(8,5)");
        Assertions.assertIterableEquals(expected, muls);
    }

    @Test
    void matchOpTest() {
        List<String> ops = Day3.getOps(testString2);
        List<String> expected = List.of("mul(2,4)","don't()","mul(5,5)","mul(11,8)","do()","mul(8,5)");
        Assertions.assertIterableEquals(expected, ops);
    }

    @Test
    void filterOps() {
        List<String> ops = Day3.getOps(testString2);
        List<String> muls = Day3.filterOps(ops);
        List<String> expected = List.of("mul(2,4)","mul(8,5)");
        Assertions.assertIterableEquals(expected, muls);
    }

    @Test
    void star1Test() {
        List<String> muls = Day3.getMuls(testString1);
        int total = Day3.star01(muls);
        Assertions.assertEquals(161, total);
    }

    @Test
    void star2Test() {
        List<String> ops = Day3.getOps(testString2);
        int total = Day3.star02(ops);
        Assertions.assertEquals(48, total);
    }
}
