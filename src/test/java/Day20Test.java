import AoC.day20.Day20;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day20Test {
    @Test
    void MazeFillTest(){
        Day20.Maze maze = Day20.readMapInput("./src/main/java/AoC/day20/testInput.txt");
        int race_cost = maze.runMaze();
        Assertions.assertEquals(84, race_cost);
    }

    @Test
    void cheatTest(){
        Day20.Maze maze = Day20.readMapInput("./src/main/java/AoC/day20/testInput.txt");
        maze.runMaze();
        HashMap<Integer, Integer> expected_values = new HashMap<>();
        expected_values.put(2, 14);
        expected_values.put(4, 14);
        expected_values.put(6, 2);
        expected_values.put(8, 4);
        expected_values.put(10, 2);
        expected_values.put(12, 3);
        expected_values.put(20, 1);
        expected_values.put(36, 1);
        expected_values.put(38, 1);
        expected_values.put(40, 1);
        expected_values.put(64, 1);
        HashMap<Integer, Integer> cheats = Day20.find_cheats(maze, 2);
        Assertions.assertEquals(expected_values, cheats);
    }

    @Test
    void largeCheatTest(){
        Day20.Maze maze = Day20.readMapInput("./src/main/java/AoC/day20/testInput.txt");
        maze.runMaze();
        HashMap<Integer, Integer> expected_values = new HashMap<>();
        expected_values.put(50, 32);
        expected_values.put(52, 31);
        expected_values.put(54, 29);
        expected_values.put(56, 39);
        expected_values.put(58, 25);
        expected_values.put(60, 23);
        expected_values.put(62, 20);
        expected_values.put(64, 19);
        expected_values.put(66, 12);
        expected_values.put(68, 14);
        expected_values.put(70, 12);
        expected_values.put(72, 22);
        expected_values.put(74, 4);
        expected_values.put(76, 3);
        HashMap<Integer, Integer> cheats = Day20.find_cheats(maze, 20);
        HashMap<Integer, Integer> cheatsOver50 = new HashMap<>();
        for(Map.Entry<Integer, Integer> cheat : cheats.entrySet()) {
            if(cheat.getKey() < 50) {
                continue;
            }
            cheatsOver50.put(cheat.getKey(), cheat.getValue());
        }
        Assertions.assertEquals(expected_values, cheatsOver50);
    }
}
