package AoC.day12;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    public static final Vec UP = new Vec(0, -1);
    public static final Vec DOWN = new Vec(0, 1);
    public static final Vec LEFT = new Vec(-1, 0);
    public static final Vec RIGHT = new Vec(1, 0);

    public interface Floodable {
        boolean onMap(Vec pos);
        char getCharAt(Vec pos);
    }

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

        public Vec turn(boolean left) {
            if (left) {
                return new Vec(y, -x);
            }
            return new Vec(-y, x);
        }
    }

    @Value
    public static class GardenMap implements Floodable {
        List<List<Character>> data;

        public char getCharAt(int x, int y) {
            return getCharAt(new Vec(x, y));
        }

        @Override
        public char getCharAt(Vec vector) {
            if(!onMap(vector)) {
                return '\0';
            }
            return data.get(vector.y).get(vector.x);
        }

        public int getHeight() {
            return data.size();
        }

        public int getWidth() {
            return data.get(0).size();
        }

        @Override
        public boolean onMap(Vec location) {
            return (location.x >= 0 && location.x < getWidth())
                    && (location.y >= 0 && location.y < getHeight());
        }
    }

    @Data
    @AllArgsConstructor
    public static class Plot implements Floodable{
        HashSet<Vec> plotTiles;
        char plotChar;

        @Override
        public boolean onMap(Vec pos) {
            return plotTiles.contains(pos);
        }

        @Override
        public char getCharAt(Vec pos) {
            return onMap(pos) ? plotChar : '\0';
        }
    }

    public static GardenMap readInput(String filePath) {
        List<List<Character>> lines = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                List<Character> charLine = newLine.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
                lines.add(charLine);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return new GardenMap(lines);
    }

    public static List<Plot> findPlots(GardenMap map) {
        HashSet<Vec> unvisitedNeighbours = new HashSet<>();
        unvisitedNeighbours.add(new Vec(0, 0));
        HashSet<Vec> visitedPlots = new HashSet<>();
        List<Plot> plots = new ArrayList<>();
        while (!unvisitedNeighbours.isEmpty()) {
            Vec startPos = unvisitedNeighbours.iterator().next();
            final char compareChar = map.getCharAt(startPos);
            if(compareChar == '\0') {
                // no need to read off the edge of the map
                unvisitedNeighbours.remove(startPos);
                continue;
            }

            HashSet<Vec> curPlot = new HashSet<>();



            floodFill(map, compareChar, startPos, curPlot, visitedPlots, unvisitedNeighbours);
            plots.add(new Plot(curPlot, compareChar));
        }
        return plots;
    }

    public static int getFenceSize(Plot plot) {
        Vec anyPlotPoint = plot.plotTiles.iterator().next();
        HashSet<Vec> neighbours = new HashSet<>();
        floodFill(plot, plot.getPlotChar(), anyPlotPoint, new HashSet<>(), new HashSet<>(), neighbours);
        int num_insideCorners = countInsideCorners(new HashSet<>(neighbours), plot);
        return neighbours.size() + num_insideCorners;
    }

    public static int countInsideCorners(HashSet<Vec> Edges, Plot plot) {
        if(Edges.isEmpty()) {
            return 0;
        }
        Vec pos =  Edges.iterator().next();

        Edges.remove(pos);

        int num_insideCorners = 0;
        // check neighbours
        num_insideCorners += countInsideCorners(Edges, plot);

        int corners = countNeighbourCells(plot, pos);
        if(corners > 1) {
            num_insideCorners += corners - 1;
        }

        return num_insideCorners;
    }

    public static int getPlotArea(Plot plot) {
        return plot.getPlotTiles().size();
    }

    public static boolean floodFill(Floodable map, char compareChar, Vec location, HashSet<Vec> plot ,HashSet<Vec> visitedPlots, HashSet<Vec> unvisitedNeighbours) {
        if(visitedPlots.contains(location))
            return false;

        // am I an unvisited neighbour?
        char mychar = map.getCharAt(location);
        if(mychar != compareChar) {
            unvisitedNeighbours.add(location);
            return false;
        }

        // if i was previously unvisited, we are now
        unvisitedNeighbours.remove(location);
        visitedPlots.add(location);
        plot.add(location);

        int num_insideCorners = 0;
        // check neighbours
        floodFill(map, mychar, location.move(UP), plot, visitedPlots, unvisitedNeighbours);
        floodFill(map, mychar, location.move(LEFT), plot, visitedPlots, unvisitedNeighbours);
        floodFill(map, mychar, location.move(DOWN), plot, visitedPlots, unvisitedNeighbours);
        floodFill(map, mychar, location.move(RIGHT), plot, visitedPlots, unvisitedNeighbours);

        return true;
    }

    public static HashSet<Vec> getNeighbours(Plot plot) {
        Vec anyPlotPoint = plot.plotTiles.iterator().next();
        HashSet<Vec> neighbours = new HashSet<>();
        floodFill(plot, plot.getCharAt(anyPlotPoint), anyPlotPoint,new HashSet<>(), new HashSet<>(), neighbours);
        return neighbours;
    }

    public static int countEdges(Plot plot) {
        HashSet<Vec> outsideEdge = getNeighbours(plot);
        int total_edges = 0;
        while(!outsideEdge.isEmpty()) {
            Vec edgeStart = outsideEdge.iterator().next();
            perimiterGuard guard = new perimiterGuard(edgeStart, plot);
            perimiterGuard startGuard = new perimiterGuard(edgeStart, plot);
            total_edges += guard.countCorners(outsideEdge, startGuard, plot);
        }
        return total_edges;
    }

    public static int countNeighbourCells(Floodable plot, Vec pos) {
        int neighbours = 0;
        neighbours += plot.onMap(pos.move(UP)) ? 1 : 0;
        neighbours += plot.onMap(pos.move(DOWN)) ? 1 : 0;
        neighbours += plot.onMap(pos.move(LEFT)) ? 1 : 0;
        neighbours += plot.onMap(pos.move(RIGHT)) ? 1 : 0;
        return neighbours;
    }

    @Data
    @AllArgsConstructor
    public static class perimiterGuard {
        Vec pos;
        Vec walk_dir;
        Vec check_dir;

        perimiterGuard(Vec pos, Plot plot) {
            Vec dir1 = RIGHT;
            this.pos = pos;
            // starting direction depends on where on the edge we are
            if(plot.onMap(pos.move(UP)))
                dir1 = LEFT;
            if(plot.onMap(pos.move(DOWN)))
                dir1 = RIGHT;
            if(plot.onMap(pos.move(LEFT)))
                dir1 = DOWN;
            if(plot.onMap(pos.move(RIGHT)))
                dir1 = UP;
            walk_dir = dir1;
            check_dir = dir1.turn(false);
        }

        public int countCorners(HashSet<Vec> edges, perimiterGuard startPos, Plot plot) {
            int corners = 0;
            boolean firstRun = true;
            while(!startPos.equals(this) || firstRun) {
                firstRun = false;
                System.out.println("guard pos: " + pos.x + ", " + pos.y);

                // ran off the edge, turn right, and step 1
                if (!plot.onMap(pos.move(check_dir))) {
                    corners++;
                    walk_dir = walk_dir.turn(false);
                    check_dir = check_dir.turn(false);
                    pos = pos.move(walk_dir);
                    continue;
                }

                // ran into the plot, turn left
                if (plot.onMap(pos.move(walk_dir))) {
                    corners++;
                    walk_dir = walk_dir.turn(true);
                    check_dir = check_dir.turn(true);
                    continue;
                }

                edges.remove(pos);
                pos = pos.move(walk_dir);

            }
            edges.remove(pos);
            return corners;
        }
    }

    public static int star1(List<Plot> plots) {
        int totalPrice = 0;
        for(Plot plot : plots) {
            totalPrice += getFenceSize(plot) * getPlotArea(plot);
        }
        return totalPrice;
    }

    public static int star2(List<Plot> plots) {
        int totalPrice = 0;
        for(Plot plot : plots) {
            int edges = countEdges(plot);
            int area = getPlotArea(plot);
            System.out.println(plot.getPlotChar()+ ": " + area + " * " + edges);
            totalPrice += countEdges(plot) * getPlotArea(plot);
        }
        return totalPrice;
    }

    public static void main(String[] args) {
        GardenMap map = readInput("./src/main/java/AoC/day12/input.txt");
        List<Plot> plots = findPlots(map);
        int star1 = star1(plots);
        System.out.println("star1: " + star1);

        int star2 = star2(plots);
        System.out.println("star2: " + star2);
    }
}

