import AoC.day19.Day19;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day19Test {
    @Test
    void testProducableFilter() {
        List<String> towels = Day19.readInputTowels("./src/main/java/AoC/day19/testInput.txt");
        towels = Day19.removeProducableTowels(towels);
        Set<String> expected = Set.of("r", "wr", "b", "g", "bwu");
        Assertions.assertEquals(expected, new HashSet<>(towels));
    }

    @Test
    void star1Test() {
        List<String> towels = Day19.readInputTowels("./src/main/java/AoC/day19/testInput.txt");
        List<String> designs = Day19.readInputDesigns("./src/main/java/AoC/day19/testInput.txt");
        int star1 = Day19.star1(towels, designs);
        Assertions.assertEquals(6, star1);
    }

    @Test
    void star2Test() {
        List<String> towels = Day19.readInputTowels("./src/main/java/AoC/day19/testInput.txt");
        List<String> designs = Day19.readInputDesigns("./src/main/java/AoC/day19/testInput.txt");
        long star2 = Day19.star2(towels, designs);
        Assertions.assertEquals(16, star2);
    }
}
