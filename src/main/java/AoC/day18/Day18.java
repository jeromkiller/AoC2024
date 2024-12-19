package AoC.day18;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day18 {

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

        public int dist(Vec vec) {
            int x = Math.abs(this.x - vec.x);
            int y = Math.abs(this.y - vec.y);
            return x + y;
        }
    }

    public static Vec UP = new Vec(0, -1);
    public static Vec DOWN = new Vec(0, 1);
    public static Vec LEFT = new Vec(-1, 0);
    public static Vec RIGHT = new Vec(1, 0);

    public static Set<Vec> createMap(Vec size) {
        Set<Vec> fullMap = new HashSet<>();
        for(int y = 0; y <= size.getY(); y++) {
            for(int x = 0; x <= size.getX(); x++) {
                fullMap.add(new Vec(x, y));
            }
        }
        return fullMap;
    }

    public static List<Vec> readInput(String filePath) {
        List<Vec> obstacles = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(",");
                obstacles.add(new Vec(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1])));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return obstacles;
    }

    public static AbstractMap.SimpleEntry<Vec, Integer> createNode(Vec pos, int cost) {
        return new AbstractMap.SimpleEntry<>(pos, cost);
    }

    // my implementation of dijkstra doesn't keep track of the path, it just outputs the weight of the endnode once it finds it
    public static int Dijkstra(Set<Vec> nodes, Vec size) {
        PriorityQueue<Map.Entry<Vec, Integer>> neighbourQueue = new PriorityQueue<>(Map.Entry.comparingByValue());
        Set<Vec> VisitedList = new HashSet<>();

        Vec startPoint = new Vec(0, 0);
        Vec endPoint = size;

        neighbourQueue.add(createNode(startPoint, 0));

        while(!neighbourQueue.isEmpty()) {
            Map.Entry<Vec, Integer> currentNode = neighbourQueue.poll();
            Vec currentPos = currentNode.getKey();
            int currentCost = currentNode.getValue();
            // if we've already Visited this, we don't have to check
            if(VisitedList.contains(currentPos)) {
                continue;
            }

            // if we expanded into the end node, that means we found the shortest path
            if(currentPos.equals(endPoint)) {
                return currentCost;
            }

            // check if the neighbours exist, or have been visited
            Vec upNeighbour = currentPos.move(UP);
            if(nodes.contains(upNeighbour)) {
                neighbourQueue.add(createNode(upNeighbour, currentCost + 1));
            }
            Vec downNeighbour = currentPos.move(DOWN);
            if(nodes.contains(downNeighbour)) {
                neighbourQueue.add(createNode(downNeighbour, currentCost + 1));
            }
            Vec leftNeighbour = currentPos.move(LEFT);
            if(nodes.contains(leftNeighbour)) {
                neighbourQueue.add(createNode(leftNeighbour, currentCost + 1));
            }
            Vec rightNeighbour = currentPos.move(RIGHT);
            if(nodes.contains(rightNeighbour)) {
                neighbourQueue.add(createNode(rightNeighbour, currentCost + 1));
            }
            // mark this point as visited
            VisitedList.add(currentPos);
        }
        return -1;
    }

    public static int star1(List<Vec> blockedPositions, Vec size) {
        Set<Vec> accessable_nodes = createMap(size);
        accessable_nodes.removeAll(blockedPositions.subList(0, 1024));
        return Dijkstra(accessable_nodes, size);
    }

    public static Vec star2(List<Vec> blockedPositions, Vec size) {
        // we know from star 1 we can solve the maze with at least the first 1024 bytes removed
        Set<Vec> accessable_nodes = createMap(size);
        int i = 1024;
        accessable_nodes.removeAll(blockedPositions.subList(0, i));
        while (Dijkstra(accessable_nodes, size) > 0) {
            i++;
            accessable_nodes.remove(blockedPositions.get(i));
        }
        return blockedPositions.get(i);
    }

    public static void main(String[] args) {
        Vec size = new Vec(70, 70);
        List<Vec> input = readInput("./src/main/java/AoC/day18/input.txt");
        int star1 = star1(input, size);
        System.out.println("Star1: " + star1);


        Vec star2 = star2(input, size);
        System.out.printf("Star2: %d,%d\n", star2.getX(), star2.getY());
    }
}
