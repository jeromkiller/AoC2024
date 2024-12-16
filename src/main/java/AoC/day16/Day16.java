package AoC.day16;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Day16 {
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

        public Vec turnRight() {
            // rotate position vector 90 degrees right
            return new Vec(-y, x);
        }

        public Vec turnLeft() {
            // rotate position vector 90 degrees right
            return new Vec(y, -x);
        }

        public Vec reverse() {
            return new Vec(-x, -y);
        }
    }

    public static Vec UP = new Vec(0, -1);
    public static Vec DOWN = new Vec(0, 1);
    public static Vec LEFT = new Vec(-1, 0);
    public static Vec RIGHT = new Vec(1, 0);

    @Data
    @AllArgsConstructor
    public static class Maze {
        HashMap<Vec, Node> nodes;
        Vec startNode;
        Vec endNode;
        int width;
        int height;
    }

    @Value
    @AllArgsConstructor
    public static class Node {
        int cost;
        Vec path_dir;

        Node() {
            cost = Integer.MAX_VALUE;
            path_dir = new Vec(0, 0);
        }
    }

    @Value
    @AllArgsConstructor
    public static class Reindeer {
        Vec pos;
        Vec dir;
        int cost;

        public Reindeer(Vec pos) {
            this.pos = pos;
            this.dir = new Vec(RIGHT);
            this.cost = 0;
        }

        public Reindeer step() {
            return new Reindeer(pos.move(dir), dir, cost + 1);
        }

        public Reindeer turnLeft() {
            return new Reindeer(pos, dir.turnLeft(), cost + 1000);
        }

        public Reindeer turnRight() {
            return new Reindeer(pos, dir.turnRight(), cost + 1000);
        }
    }

    public static boolean floodMaze(Maze maze, Reindeer reindeer) {
        final Vec curPos = reindeer.getPos();
        if(!maze.getNodes().containsKey(reindeer.getPos())) {
            // not a spot in the maze
            return false;
        }

        final int locValue = maze.getNodes().get(reindeer.getPos()).getCost();
        if(locValue <= reindeer.getCost()) {
            // there is a cheaper path to this point
            return false;
        }

        // update the cost of the current tile
        maze.getNodes().put(curPos, new Node(reindeer.getCost(), reindeer.getDir()));

        //check the other directions
        floodMaze(maze, reindeer.step());
        floodMaze(maze, reindeer.turnLeft().step());
        floodMaze(maze, reindeer.turnRight().step());

        return true;
    }

    public static Maze readMapInput(String filePath) {
        HashMap<Vec, Node> nodes = new HashMap<>();
        Vec startPos = null;
        Vec endPos = null;
        int width = 0;
        int height = 0;
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                width = line.length();

                for(int i = 0; i < width; i++) {
                    Vec pos = new Vec(i, height);
                    switch (line.charAt(i)) {
                        case 'S':
                            startPos = pos;
                            nodes.put(pos, new Node());
                            break;
                        case 'E':
                            endPos = pos;
                            nodes.put(pos, new Node());
                            break;
                        case '.':
                            nodes.put(pos, new Node());
                            break;
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
        assert startPos != null;
        assert endPos != null;

        return new Maze(nodes, startPos, endPos, width, height);
    }

    public static HashSet<Vec> getBestPathPoints(Maze solvedMaze) {
        HashSet<Vec> bestPoints = new HashSet<>();
        Vec endNodepointee = solvedMaze.endNode.move(solvedMaze.getNodes().get(solvedMaze.endNode).getPath_dir().reverse());
        walkBestPath(solvedMaze, bestPoints, endNodepointee);
        return bestPoints;
    }

    public static boolean doesNeighbourPointToMe(Maze maze, Vec sourcePos, Vec dir) {
        Vec neighbourPos = sourcePos.move(dir);
        if(!maze.getNodes().containsKey(neighbourPos))
            return false;


        final Node neighbourNode = maze.getNodes().get(neighbourPos);
        return sourcePos.equals(neighbourPos.move(neighbourNode.path_dir));
    }

    public static boolean walkBestPath(Maze solvedMaze, HashSet<Vec> bestPathPoints, Vec pos) {
        if(bestPathPoints.contains(pos)) {
            // we already found a way to this point
            return false;
        }

        if(!solvedMaze.getNodes().containsKey(pos)) {
            // this is not part of the maze
            return false;
        }

        // this is part of the best route
        bestPathPoints.add(pos);

        // move back into the oposite direction
        Vec oppositeDirection = solvedMaze.getNodes().get(pos).getPath_dir().reverse();
        walkBestPath(solvedMaze, bestPathPoints, pos.move(oppositeDirection));

        // walk along the path of this value
        if(doesNeighbourPointToMe(solvedMaze, pos, UP)) {
            walkBestPath(solvedMaze, bestPathPoints, pos.move(UP));
        }
        if(doesNeighbourPointToMe(solvedMaze, pos, DOWN)) {
            walkBestPath(solvedMaze, bestPathPoints, pos.move(DOWN));
        }
        if(doesNeighbourPointToMe(solvedMaze, pos, LEFT)) {
            walkBestPath(solvedMaze, bestPathPoints, pos.move(LEFT));
        }
        if(doesNeighbourPointToMe(solvedMaze, pos, RIGHT)) {
            walkBestPath(solvedMaze, bestPathPoints, pos.move(RIGHT));
        }

        return true;
    }

    public static int Star1(Maze maze) {
        Reindeer startReindeer = new Reindeer(maze.startNode);
        floodMaze(maze, startReindeer);
        return maze.getNodes().get(maze.endNode).getCost();
    }

    public static int Star2(Maze solvedMaze) {
        return getBestPathPoints(solvedMaze).size() + 1;
    }
    
    public static void printMaze(Maze maze) {
        StringBuilder printStr = new StringBuilder();
        for(int y = 0; y < maze.getHeight(); y++) {
            for(int x = 0; x < maze.getWidth(); x++) {
                Vec pos = new Vec(x, y);
                if(!maze.getNodes().containsKey(pos)) {
                    printStr.append("#");
                    continue;
                }
                Vec nodeDir = maze.getNodes().get(pos).getPath_dir();
                if(nodeDir.equals(UP)) {
                    printStr.append('^');
                    continue;
                }
                if(nodeDir.equals(DOWN)) {
                    printStr.append('v');
                    continue;
                }
                if(nodeDir.equals(LEFT)) {
                    printStr.append('<');
                    continue;
                }
                if(nodeDir.equals(RIGHT)) {
                    printStr.append('>');
                    continue;
                }
                printStr.append('.');
            }
            printStr.append('\n');
        }
        System.out.println(printStr.toString());
    }

    public static void main(String[] args) {
        Maze maze = readMapInput("./src/main/java/AoC/day16/input.txt");
        int star1 = Star1(maze);
        System.out.println("Star1: " + star1);

        int star2 = Star2(maze);
        System.out.println("Star2: " + star2);
    }

}
