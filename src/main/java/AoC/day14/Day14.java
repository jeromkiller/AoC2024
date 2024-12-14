package AoC.day14;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 {

    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        public Vec(Vec copy) {
            this.x = copy.x;
            this.y = copy.y;
        }

        public Vec move(Vec dir, int steps, Vec bounds) {
            Vec steppedVec = new Vec(x + dir.x * steps, y + dir.y * steps);
            return new Vec(steppedVec.wrap(bounds));
        }

        public Vec wrap(Vec bounds) {
            return new Vec(wrapValue(x, bounds.x), wrapValue(y, bounds.y));
        }

        public static int wrapValue(int val, int max) {
            int newVal = ((val % max) + max) % max;
            return newVal;
        }
    }

    @Value
    @AllArgsConstructor
    public static class Robot{
        Vec pos;
        Vec dir;

        public Robot step(int steps, Vec bounds) {
            return new Robot(pos.move(dir, steps, bounds), dir);
        }
    }

    public static int[] getNumbers(String input) {
        String[] splitStr = input.split(",");
        int num1 = Integer.parseInt(splitStr[0].replaceAll("[^0-9,-]", ""));
        int num2 = Integer.parseInt(splitStr[1].replaceAll("[^0-9,-]", ""));
        return new int[]{num1, num2};
    }

    public static List<Robot> readInput(String filePath) {
        List<Robot> robots = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(" ");
                int[] pos_nums = getNumbers(splitLine[0]);
                int[] dir_nums = getNumbers(splitLine[1]);

                robots.add(new Robot(new Vec(pos_nums[0], pos_nums[1]), new Vec(dir_nums[0], dir_nums[1])));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return robots;
    }

    public static int countInBounds(List<Robot> robots, Vec lowBound, Vec highBound) {
        int total = 0;
        for(Robot robot : robots) {
            Vec pos = robot.getPos();
            if((pos.x >= lowBound.x && pos.x < highBound.x)
                && (pos.y >= lowBound.y && pos.y < highBound.y))
                total++;
        }
        return total;
    }

    public static int countRobots(List<Robot>robots, Vec bounds) {
        int X_half = bounds.x / 2;
        int Y_half = bounds.y / 2;
        int nw_count = countInBounds(robots, new Vec(0, 0), new Vec(X_half, Y_half));
        int ne_count = countInBounds(robots, new Vec(X_half + 1, 0), new Vec(bounds.x, Y_half));
        int sw_count = countInBounds(robots, new Vec(0, Y_half + 1), new Vec(X_half, bounds.y));
        int se_count = countInBounds(robots, new Vec(X_half + 1, Y_half + 1), new Vec(bounds.x, bounds.y));
        return nw_count * ne_count * sw_count * se_count;
    }

    public static int calcVariance(List<Integer> vals) {
        long mean = vals.stream().mapToLong(Integer::longValue).sum() / vals.size();
        long sum = 0;
        for(int val : vals) {
            sum += (long) Math.pow(val - mean, 2);
        }
        return (int) sum / vals.size();
    }

    public static int getVarianceList(List<Robot> robots, Vec bounds) {
        //TreeMap<Integer, Integer> varianceList = new TreeMap<>();
        int lowest_total = Integer.MAX_VALUE;
        int lowest_step = 0;
        for(int i = 0; i < 10000; i++) {
            final int step = i;
            List<Robot> moved_robots = robots.stream().map(r -> r.step(step, bounds)).collect(Collectors.toList());
            List<Integer> x_posses= moved_robots.stream().map(r -> r.getPos().x).collect(Collectors.toList());
            List<Integer> y_posses= moved_robots.stream().map(r -> r.getPos().y).collect(Collectors.toList());
            int variance_x = calcVariance(x_posses);
            int variance_y = calcVariance(y_posses);
            int totalVariance = variance_x + variance_y;
            if(totalVariance < lowest_total) {
                lowest_total = totalVariance;
                lowest_step = step;
            }
        }
        return lowest_step;
    }

    public static int star1(List<Robot> robots, Vec bounds) {
        List<Robot> moved_robots =  robots.stream().map(r -> r.step(100, bounds)).collect(Collectors.toList());
        return countRobots(moved_robots, bounds);
    }

    public static int star2(List<Robot> robots, Vec bounds) {
        return getVarianceList(robots, bounds);
    }

    public static void printTree(List<Robot> robots, Vec bounds, int step) {
        HashSet<Vec> possitions = new HashSet<> (robots.stream().map(r -> r.step(step, bounds)).map(r -> r.getPos()).collect(Collectors.toSet()));
        StringBuilder printStr = new StringBuilder();
        for(int y = 0; y < bounds.y; y++) {
            for(int x = 0; x < bounds.x; x++) {
                char putChar = possitions.contains(new Vec(x, y)) ? '#' : '.';
                printStr.append(putChar);
            }
            printStr.append("\n");
        }
        System.out.println(printStr.toString());
    }

    public static void main(String[] args) {
        List<Robot> robots = readInput("./src/main/java/AoC/day14/input.txt");
        Vec bounds = new Vec(101, 103);
        int star1 = star1(robots, bounds);
        System.out.println("star1: " + star1);

        int star2 = star2(robots, bounds);
        System.out.println("star2: " + star2);
        printTree(robots, bounds, star2);
    }
}
