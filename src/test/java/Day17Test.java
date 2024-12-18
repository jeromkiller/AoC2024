import AoC.day17.Day17;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day17Test {
    @Test
    void test1() {
        Day17.CPU cpu = new Day17.CPU(0, 0, 9, List.of(2L, 6L));
        cpu.run();
        Assertions.assertEquals(1, cpu.getReg_B());
    }

    @Test
    void test2() {
        Day17.CPU cpu = new Day17.CPU(10, 0, 0, List.of(5L,0L,5L,1L,5L,4L));
        cpu.run();
        List<Long> expected = List.of(0L, 1L, 2L);
        Assertions.assertEquals(expected, cpu.getOutStream());
    }

    @Test
    void test3() {
        Day17.CPU cpu = new Day17.CPU(2024, 0, 0, List.of(0L,1L,5L,4L,3L,0L));
        cpu.run();
        List<Long> expected = List.of(4L,2L,5L,6L,7L,7L,7L,7L,3L,1L,0L);
        Assertions.assertEquals(expected, cpu.getOutStream());
        Assertions.assertEquals(0, cpu.getReg_A());
    }

    @Test
    void test4() {
        Day17.CPU cpu = new Day17.CPU(0, 29, 0, List.of(1L, 7L));
        cpu.run();
        Assertions.assertEquals(26, cpu.getReg_B());
    }

    @Test
    void test5() {
        Day17.CPU cpu = new Day17.CPU(0, 2024, 43690, List.of(4L, 0L));
        cpu.run();
        Assertions.assertEquals(44354, cpu.getReg_B());
    }

}
