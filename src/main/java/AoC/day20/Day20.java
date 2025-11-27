package AoC.day20;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day20 {
    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        Vec(Vec copy) {
            this.x = copy.x;
            this.y = copy.y;
        }

        public Vec move(Vec dir) {
            return new Vec(x + dir.x, y + dir.y);
        }
    }

    public static Vec UP = new Vec(0, -1);
    public static Vec DOWN = new Vec(0, 1);
    public static Vec LEFT = new Vec(-1, 0);
    public static Vec RIGHT = new Vec(1, 0);

    @AllArgsConstructor
    public static class Maze{
        public HashMap<Vec, Integer> floors;
        public HashSet<Vec> walls;
        public Vec start;
        public Vec end;

        public int runMaze() {
            HashMap<Vec, Integer> unvisited = new HashMap<>(floors);
            Vec visit = start;
            int distance = 0;

            while(!unvisited.isEmpty()) {
                unvisited.remove(visit);
                floors.put(visit, distance);
                distance += 1;

                if(unvisited.containsKey(visit.move(UP))) {
                    visit = visit.move(UP);
                }else if(unvisited.containsKey(visit.move(LEFT))) {
                    visit = visit.move(LEFT);
                }else if(unvisited.containsKey(visit.move(DOWN))) {
                    visit = visit.move(DOWN);
                }else if(unvisited.containsKey(visit.move(RIGHT))) {
                    visit = visit.move(RIGHT);
                }
            }
            return floors.get(end);
        }
    }

    public static Maze readMapInput(String filePath) {
        HashMap<Vec, Integer> floor = new HashMap<>();
        HashSet<Vec> walls = new HashSet<>();
        Vec start = null;
        Vec end = null;
        int width = 0;
        int height = 0;
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while(reader.hasNext()) {
                String line = reader.nextLine();
                width = line.length();
                for(int i = 0; i < width; i++) {
                    Vec pos = new Vec(i, height);
                    switch (line.charAt(i)) {
                        case 'S':
                            start = pos;
                            floor.put(pos, Integer.MAX_VALUE);
                            break;
                        case 'E':
                            end = pos;
                            floor.put(pos, Integer.MAX_VALUE);
                            break;
                        case '.':
                            floor.put(pos, Integer.MAX_VALUE);
                            break;
                        default:
                            walls.add(pos);
                    }
                }
                height++;
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        assert start != null;
        assert end != null;
        return new Maze(floor, walls, start, end);
    }

//    public static HashMap<Integer, Integer> find_cheats(Maze maze) {
//        HashMap<Integer, Integer> cheats = new HashMap<>();
//
//        // check how much time we can save using a cheat
//        for(Map.Entry<Vec, Integer> tile : maze.floors.entrySet()) {
//            Vec pos = tile.getKey();
//            int cost = tile.getValue();
//            for(Vec direction: List.of(pos.move(UP).move(UP), pos.move(RIGHT).move(RIGHT), pos.move(DOWN).move(DOWN), pos.move(LEFT).move(LEFT))) {
//                int visit_cost = maze.floors.getOrDefault(direction, 0);
//                int cheat_val = visit_cost - (cost + 2);
//                if(cheat_val > 0) {
//                    cheats.put(cheat_val, cheats.getOrDefault(cheat_val, 0) + 1);
//                }
//            }
//        }
//        return cheats;
//    }

    public static HashMap<Integer, Integer> find_cheats(Maze maze, int cheatDistance) {
        HashMap<Integer, Integer> cheats = new HashMap<>();

        // check how much time we can save using each cheat
        for(Map.Entry<Vec, Integer> tile : maze.floors.entrySet()) {
            Vec pos = tile.getKey();
            int dist = tile.getValue();
            HashMap<Vec, Integer> cheatEndpoints = findCheatEndpoints(maze, pos, cheatDistance);
            for(Map.Entry<Vec, Integer> cheatTile : cheatEndpoints.entrySet()) {
                Vec cheatPos = cheatTile.getKey();
                int cheatLength = cheatTile.getValue();
                int visit_cost = maze.floors.getOrDefault(cheatPos, 0);
                int savings = visit_cost - (dist + cheatLength);
                if(savings > 0) {
                    cheats.put(savings, cheats.getOrDefault(savings, 0) + 1);
                }
            }
        }
        return cheats;
    }

    public static HashMap<Vec, Integer> findCheatEndpoints(Maze maze, Vec pos, int fillSize) {
        HashMap<Vec, Integer> cheatEndpoints = new HashMap<>();
        HashMap<Vec, Integer> visited = new HashMap<>();
        floodEndpoints(pos, fillSize, 0, visited);
        for(Map.Entry<Vec, Integer> visit : visited.entrySet()) {
            if(!maze.floors.containsKey(visit.getKey())) {
                continue;
            }
            cheatEndpoints.put(visit.getKey(), visit.getValue());
        }
        return cheatEndpoints;
    }

    public static boolean floodEndpoints(Vec pos, int fillSize, int fill, HashMap<Vec, Integer> visited) {
        if(fill > fillSize) {
            return false;
        }

        if(fill >= visited.getOrDefault(pos, Integer.MAX_VALUE)) {
            return false;
        }

        visited.put(pos, fill);
        floodEndpoints(pos.move(UP), fillSize, fill + 1, visited);
        floodEndpoints(pos.move(RIGHT), fillSize, fill + 1, visited);
        floodEndpoints(pos.move(DOWN), fillSize, fill + 1, visited);
        floodEndpoints(pos.move(LEFT), fillSize, fill + 1, visited);
        return true;
    }

    public static int Star1(Maze maze) {
        HashMap<Integer, Integer> cheats = find_cheats(maze, 2);
        int valid_count = 0;
        for(Map.Entry<Integer, Integer> cheat: cheats.entrySet()) {
            if(cheat.getKey() >= 100) {
                valid_count += cheat.getValue();
            }
        }
        return valid_count;
    }

    public static int Star2(Maze maze) {
        HashMap<Integer, Integer> cheats = find_cheats(maze, 20);
        int valid_count = 0;
        for(Map.Entry<Integer, Integer> cheat: cheats.entrySet()) {
            if(cheat.getKey() >= 100) {
                valid_count += cheat.getValue();
            }
        }
        return valid_count;
    }

    public static void main(String[] args) {
        Maze maze = readMapInput("./src/main/java/AoC/day20/input.txt");
        maze.runMaze();

        int star1 = Star1(maze);
        System.out.println("Star1: " + star1);

        int star2 = Star2(maze);
        System.out.println("Star2: " + star2);
    }
}
