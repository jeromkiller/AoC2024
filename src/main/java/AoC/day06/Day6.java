package AoC.day06;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day6 {
    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        public Vec(Vec vector) {
            this.x = vector.x;
            this.y = vector.y;
        }

        public Vec move(Vec vector) {
            return new Vec(x + vector.x, y + vector.y);
        }

        public Vec turn() {
            // rotate position vector 90 degrees right
            return new Vec(-y, x);
        }
    }

    @Value
    @AllArgsConstructor
    public static class Guard {
        Vec pos;
        Vec orient;

        public Guard(Guard guard) {
            this.pos = new Vec(guard.pos);
            this.orient = new Vec(guard.orient);
        }

        public Guard move() {
            return new Guard(pos.move(orient), orient);
        }

        public Guard turn() {
            return new Guard(pos, orient.turn());
        }
    }

    @Data
    public static class map {
        int width;
        int height;
        HashSet<Vec> obstacles;
        Guard guard;
        HashSet<Guard> guard_positions;

        public map(int width, int height, HashSet<Vec> obstacles, Guard guard) {
            this.width = width;
            this.height = height;
            this.obstacles = obstacles;
            this.guard = guard;
            this.guard_positions = new HashSet<>();
            // include the guard's start position;
            this.guard_positions.add(guard);
        }

        public map(map map) {
            this.width = map.width;
            this.height = map.height;
            this.obstacles = new HashSet<>(map.obstacles);
            this.guard = new Guard(map.guard);
            this.guard_positions = new HashSet<>(map.guard_positions);
        }

        public boolean addObstacle(Vec obstacle) {
            // don't put int on the guards start location
            if(getGuard().getPos().equals(obstacle)) {
                return false;
            }

            return obstacles.add(obstacle);
        }

        public boolean guard_escaped(Guard guard) {
            final Vec guard_pos = guard.pos;
            return (guard_pos.getX() < 0) || (guard_pos.getY() < 0)
                    || (guard_pos.getX() >= width) || (guard_pos.getY() >= height);
        }

        public boolean guard_looped(Guard guard) {
            // has the guard ran in to an obstacle from this position already?
            return guard_positions.contains(guard);
        }

        public boolean guard_collided(Guard guard) {
            final Vec guard_pos = guard.pos;
            return obstacles.contains(guard_pos);
        }

        public boolean move_guard() {
            Guard new_guard_pos = guard.move();

            if(guard_escaped(new_guard_pos))
                return false;

            if(guard_looped(new_guard_pos)) {
                return false;
            }

            if(guard_collided(new_guard_pos)) {

                guard = guard.turn();
            } else {
                guard = new_guard_pos;
            }
            guard_positions.add(guard);

            return true;
        }

        public boolean tryEscape() {
            while(move_guard()) {
                // nothing
                // could print each guard pos and history to a gif or something in here
            }

            // has the guard escaped or is he in a loop?
            return guard_escaped(guard.move());
        }
    }

    public static map readInput(String filePath) {
        int width = 0;
        int height = 0;
        HashSet<Vec> obstacles = new HashSet<>();
        Guard guard = null;

        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                width = newLine.length();

                // pattern match for obstacles and the guard
                Pattern pattern = Pattern.compile("[#V<>^]");
                Matcher matcher = pattern.matcher(newLine);

                while(matcher.find()) {
                    final char foundChar = matcher.group().charAt(0);
                    final int x_pos = matcher.start();
                    final int y_pos = height;
                    Vec pos = new Vec(x_pos, y_pos);
                    switch (foundChar) {
                        case '#':
                            obstacles.add(pos);
                            break;
                        case '^':
                            guard = new Guard(pos, new Vec(0, -1));
                            break;
                        case '>':
                            guard = new Guard(pos, new Vec(1, 0));
                            break;
                        case 'V':
                            guard = new Guard(pos, new Vec(0, 1));
                            break;
                        case '<':
                            guard = new Guard(pos, new Vec(-1, 0));
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

        return new map(width, height, obstacles, guard);
    }

    public static int star1(map patrol_map) {
        patrol_map.tryEscape();

        HashSet<Vec> unique_guard_positions = patrol_map.guard_positions.stream().map(Guard::getPos).collect(Collectors.toCollection(HashSet::new));
        return unique_guard_positions.size();
    }

    public static int star2(map orig_map) {
        // brute force place an obstacle into each pos
        HashSet<Vec> possible_obs_locations = new HashSet<>();
        for(int y = 0; y < orig_map.getHeight(); y++) {
            for(int x = 0; x < orig_map.getWidth(); x++) {
                map workMap = new map(orig_map);
                if(!workMap.addObstacle(new Vec(x, y))) {
                    continue;   // we don't have to try it if the obstacle can't be added
                }

                // did the guard escape?
                if(workMap.tryEscape()) {
                    continue;   // we don't have to count this if the guard escaped
                }
                possible_obs_locations.add(new Vec(x, y));
            }
        }
        return possible_obs_locations.size();
    }

    public static void main(String[] args) {
        Day6.map patrol_map = Day6.readInput("./src/main/java/AoC/day06/input.txt");
        int star1 = Day6.star1(new map(patrol_map));
        System.out.println("star1: " + star1);

        int star2 = Day6.star2(new map(patrol_map));
        System.out.println("star2: " + star2);
    }
}
