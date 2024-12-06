import AoC.day6.Day6;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class Day6Test {

    @Test
    void MoveTest() {
        Day6.Vec pos = new Day6.Vec(5, 5);
        Day6.Vec dir = new Day6.Vec(1, 2);
        pos = pos.move(dir);
        Day6.Vec expected = new Day6.Vec(6, 7);
        Assertions.assertEquals(expected, pos);
    }

    @Test
    void TurnTest() {
        Day6.Vec pos = new Day6.Vec(5, -5);
        pos = pos.turn();
        Day6.Vec expected = new Day6.Vec(5, 5);
        Assertions.assertEquals(expected, pos);
    }

    @Test
    void CollideTest() {
        HashSet<Day6.Vec> obstacles = new HashSet<>();
        obstacles.add(new Day6.Vec(1, 1));
        obstacles.add(new Day6.Vec(2, 5));
        obstacles.add(new Day6.Vec(3, 4));

        Day6.Vec pos = new Day6.Vec(1, 1);
        boolean collide = obstacles.contains(pos);
        Assertions.assertTrue(collide);
    }

    @Test
    void inputTest() {
        Day6.map patrol_map = Day6.readInput("./src/main/java/AoC/day6/testInput.txt");
        HashSet<Day6.Vec> expected_obs = new HashSet<>();
        expected_obs.add(new Day6.Vec(4, 0));
        expected_obs.add(new Day6.Vec(9, 1));
        expected_obs.add(new Day6.Vec(2, 3));
        expected_obs.add(new Day6.Vec(7, 4));
        expected_obs.add(new Day6.Vec(1, 6));
        expected_obs.add(new Day6.Vec(8, 7));
        expected_obs.add(new Day6.Vec(0, 8));
        expected_obs.add(new Day6.Vec(6, 9));

        Day6.Guard expected_guard = new Day6.Guard(new Day6.Vec(4,6), new Day6.Vec(0, -1));
        int expected_width = 10;
        int expected_height = 10;

        Assertions.assertEquals(expected_obs, patrol_map.getObstacles());
        Assertions.assertEquals(expected_guard, patrol_map.getGuard());
        Assertions.assertEquals(expected_width, patrol_map.getWidth());
        Assertions.assertEquals(expected_height, patrol_map.getHeight());
    }

    @Test
    void star1Test() {
        Day6.map patrol_map = Day6.readInput("./src/main/java/AoC/day6/testInput.txt");
        int result = Day6.star1(patrol_map);
        Assertions.assertEquals(41, result);
    }

    @Test
    void star2Test() {
        Day6.map patrol_map = Day6.readInput("./src/main/java/AoC/day6/testInput.txt");
        int result = Day6.star2(new Day6.map(patrol_map));
        Assertions.assertEquals(6, result);
    }
}
