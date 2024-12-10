package AoC.day10;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {
    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        public Vec(Vec vector) {
            this.x = vector.x;
            this.y = vector.y;
        }

        public Vec move(int x, int y) {
            return new Vec(this.x + x, this.y + y);
        }

        public Vec move(Vec vector) {
            return move(vector.x, vector.y);
        }
    }

    @Value
    @AllArgsConstructor
    public static class trailMap {
        List<List<Integer>> map;

        public int getWidth() {
            return map.get(0).size();
        }

        public int getHeight() {
            return map.size();
        }

        public int getLocationHeight(int x, int y) {
            return map.get(y).get(x);
        }

        public int getLocationHeight(Vec pos) {
            return getLocationHeight(pos.getX(), pos.getY());
        }

        public boolean isWithinMap(Vec pos) {
            return (pos.getX() >= 0 && pos.getX() < getWidth())
                    && (pos.getY() >= 0 && pos.getY() < getHeight());
        }
    }

    public static trailMap readInput(String filePath) {
        List<List<Integer>> map = new ArrayList<>();

        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                List<Integer> row = Arrays.stream(newLine.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                map.add(row);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return new trailMap(map);
    }

    public static List<Vec> findTrailHeads (trailMap map) {
        List<Vec> trailHeads = new ArrayList<>();
        for(int y=0; y < map.getHeight(); y++) {
            for(int x=0; x < map.getWidth(); x++) {
                Vec pos = new Vec(x, y);
                if(map.getLocationHeight(pos) == 0) {
                    trailHeads.add(pos);
                }
            }
        }
        return trailHeads;
    }

    public static int scoreTrailHead(trailMap map, Vec trailHead, boolean allPaths) {
        HashSet<Vec> visitedPoints = new HashSet<>();
        return walkTrailHead(map, trailHead, -1, visitedPoints, allPaths);
    }

    public static int walkTrailHead(trailMap map, Vec point, int prevHeight, HashSet<Vec> visitedPoints, boolean allPaths) {
        if(!map.isWithinMap(point)) {
            // not on map
            return 0;
        }

        if(!allPaths) {
            if (visitedPoints.contains(point)) {
                // already visited
                return 0;
            }
        }

        final int locationHeight = map.getLocationHeight(point);
        if(locationHeight - prevHeight != 1) {
            // incorrect grade
            return 0;
        }

        // all good, we're at a valid next point
        visitedPoints.add(point);

        if(locationHeight == 9) {
            // reached the top
            return 1;
        }

        int trailScore = 0;
        // try visiting each of the adjecent locations
        // up
        trailScore += walkTrailHead(map, point.move(0, -1), locationHeight, visitedPoints, allPaths);
        // right
        trailScore += walkTrailHead(map, point.move(1, 0), locationHeight, visitedPoints, allPaths);
        // down
        trailScore += walkTrailHead(map, point.move(0, 1), locationHeight, visitedPoints, allPaths);
        // right
        trailScore += walkTrailHead(map, point.move(-1, 0), locationHeight, visitedPoints, allPaths);

        return trailScore;
    }

    public static int star1(trailMap map) {
        List<Vec> trailHeads = findTrailHeads(map);
        int scoreSum = trailHeads.stream().map(trail -> scoreTrailHead(map, trail, false)).mapToInt(Integer::intValue).sum();
        return scoreSum;
    }

    public static int star2(trailMap map) {
        List<Vec> trailHeads = findTrailHeads(map);
        int scoreSum = trailHeads.stream().map(trail -> scoreTrailHead(map, trail, true)).mapToInt(Integer::intValue).sum();
        return scoreSum;
    }

    public static void main(String[] args) {
        trailMap map = readInput("./src/main/java/AoC/day10/input.txt");
        int star1 = star1(map);
        System.out.println("star1: " + star1);

        int star2 = star2(map);
        System.out.println("star2: " + star2);
    }
}
