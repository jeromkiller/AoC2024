package AoC.day08;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {
    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        public Vec(Vec vector) {
            this.x = vector.x;
            this.y = vector.y;
        }

        public boolean isWithinBounds(int width, int height) {
            return (x >= 0 && x < width) && (y >= 0 && y < height);
        }

        public static Vec add(Vec lhs, Vec rhs) {
            return new Vec(lhs.x + rhs.x, lhs.y + rhs.y);
        }

        public static Vec subtract(Vec lhs, Vec rhs) {
            return new Vec(lhs.x - rhs.x, lhs.y - rhs.y);
        }
    }

    @Value
    @AllArgsConstructor
    public static class map {
        int width;
        int height;
        HashMap<Character, List<Vec>> antennas;
    }

    public static map readInput(String filePath) {
        HashMap<Character, List<Vec>> antennas = new HashMap<>();
        int width = 0;
        int height = 0;

        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                width = newLine.length();

                // pattern match for obstacles and the guard
                Pattern pattern = Pattern.compile("[^.#]");
                Matcher matcher = pattern.matcher(newLine);

                while(matcher.find()) {
                    final char foundChar = matcher.group().charAt(0);
                    final int x_pos = matcher.start();
                    final int y_pos = height;
                    Vec pos = new Vec(x_pos, y_pos);
                    if(!antennas.containsKey(foundChar)) {
                        antennas.put(foundChar, new ArrayList<>());
                    }
                    antennas.get(foundChar).add(pos);
                }
                height++;
            }

            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return new map(width, height, antennas);
    }

    private static Vec calcAntinode(Vec lhs, Vec rhs) {
        Vec offset = Vec.subtract(lhs, rhs);
        return Vec.add(lhs, offset);
    }

    public static List<Vec> genFrequiencyAntiNode(List<Vec> antennas, int width, int height) {
        List<Vec> antiNodes = new ArrayList<>();
        for(Vec lhs : antennas) {
            for(Vec rhs: antennas) {
                if(lhs.equals(rhs)) {
                    // don't check yourself
                    continue;
                }
                Vec newAntinode = calcAntinode(lhs, rhs);
                if(newAntinode.isWithinBounds(width, height)) {
                    antiNodes.add(newAntinode);
                }
            }
        }
        return antiNodes;
    }

    public static HashSet<Vec> genAllAntiNodes(map antennas) {
        HashSet<Vec> antiNodes = new HashSet<>();
        for(final List<Vec> frequencyAntennas : antennas.getAntennas().values()) {
            antiNodes.addAll(genFrequiencyAntiNode(frequencyAntennas, antennas.getWidth(), antennas.getHeight()));
        }
        return antiNodes;
    }

    public static int star1(map antennas) {
        return genAllAntiNodes(antennas).size();
    }

    private static List<Vec> calcResonantAntinode(Vec lhs, Vec rhs, int width, int height) {
        // also add the antena itself
        List<Vec> antiNodes = new ArrayList<>();
        Vec newAntiNode = lhs;
        Vec prevAntiNode = rhs;
        while(newAntiNode.isWithinBounds(width, height)) {
            antiNodes.add(newAntiNode);
            Vec tmp = newAntiNode;
            newAntiNode = calcAntinode(newAntiNode, prevAntiNode);
            prevAntiNode = tmp;
        }
        return antiNodes;
    }

    public static int star2(map antennas) {
        HashSet<Vec> antiNodes = new HashSet<>();
        for(final List<Vec> frequencyAntennas : antennas.getAntennas().values()) {
            //antiNodes.addAll(frequencyAntennas);
            for(final Vec lhs : frequencyAntennas) {
                for(final Vec rhs : frequencyAntennas) {
                    if(lhs.equals(rhs)) {
                        continue; //don't check yourself
                    }
                    antiNodes.addAll(calcResonantAntinode(lhs, rhs, antennas.getWidth(), antennas.getHeight()));
                }
            }

            //antiNodes.addAll(genFrequiencyAntiNode(frequencyAntennas, antennas.getWidth(), antennas.getHeight()));
        }
        return antiNodes.size();
    }

    public static String antiNodeString(HashSet<Vec> AntiNode, int width, int height) {
        StringBuilder returnString = new StringBuilder();
        for(int y = 0; y < width; y++) {
            for(int x = 0; x < height; x++) {
                char putChar = AntiNode.contains(new Vec(x, y)) ? '#' : '.';
                returnString.append(putChar);
            }
            returnString.append('\n');
        }
        return returnString.toString();
    }

    public static void main(String[] args) {
        Day8.map data = Day8.readInput("./src/main/java/AoC/day8/input.txt");
        int star1 = Day8.star1(data);
        System.out.println("Star1: " + star1);

        int star2 = Day8.star2(data);
        System.out.println("Star2: " + star2);
    }
}
