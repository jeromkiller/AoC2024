package AoC.day15;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class day15 {

    public static Vec UP = new Vec(0, -1);
    public static Vec DOWN = new Vec(0, 1);
    public static Vec LEFT = new Vec(-1, 0);
    public static Vec RIGHT = new Vec(1, 0);


    @Value
    @AllArgsConstructor
    public static class Vec {
        int x;
        int y;

        public Vec(Vec copy) {
            this.x = copy.x;
            this.y = copy.y;
        }

        public static Vec fromChar(char c) {
            switch (c) {
                case '^':
                    return new Vec(UP);
                case 'v':
                    return new Vec(DOWN);
                case '<':
                    return new Vec(LEFT);
                case '>':
                    return new Vec(RIGHT);
            }
            return new Vec(-1, -1);
        }

        public Vec move(Vec dir) {
            return new Vec(x + dir.x, y + dir.y);
        }
    }

    public interface WearhouseObject {
        static enum ObjectType {
            WALL,
            BOX,
            WIDE_BOX,
            ROBOT
        }

        //Vec pos = new Vec(-1, -1);

        ObjectType getType();

        boolean tryPush(Vec dir, Wearhouse wearhouse);

        boolean canPush(Vec dir, Wearhouse wearhouse);

        void push(Vec dir, Wearhouse wearhouse);

        String toString();
    }

    @Data
    public static class Wall implements WearhouseObject {
        @Override
        public ObjectType getType() {
            return ObjectType.WALL;
        }

        @Override
        public boolean tryPush(Vec dir, Wearhouse wearhouse) {
            return false;
        }

        @Override
        public boolean canPush(Vec dir, Wearhouse wearhouse) {
            return false;
        }

        @Override
        public void push(Vec dir, Wearhouse wearhouse) {
            return;
        }

        public String toString() {
            return "#";
        }
    }

    @Data
    @AllArgsConstructor
    public abstract static class PushableWearhouseObject implements WearhouseObject {
        Vec pos;

        @Override
        public boolean canPush(Vec dir, Wearhouse wearhouse) {
            Vec nextSpot = pos.move(dir);
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();
            if(!objects.containsKey(nextSpot)) {
                return true;
            }
            return objects.get(nextSpot).canPush(dir, wearhouse);
        }

        @Override
        public boolean tryPush(Vec dir, Wearhouse wearhouse) {
            // see if we can push the object behind this as well
            Vec nextSpot = pos.move(dir);
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();

            if(!canPush(dir, wearhouse)) {
                return false;
            }
            push(dir, wearhouse);
            return true;
        }

        @Override
        public void push(Vec dir, Wearhouse wearhouse) {
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();
            Vec nextSpot = pos.move(dir);
            if(!objects.containsKey(nextSpot)) {
                pushSelf(dir, wearhouse);
                return;
            }

            objects.get(nextSpot).push(dir, wearhouse);
            pushSelf(dir, wearhouse);
        }

        private void pushSelf(Vec dir, Wearhouse wearhouse) {
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();

            objects.remove(pos);    // remove yourself from the map
            pos = pos.move(dir);
            objects.put(pos, this);
        }
    }

    public static class Box extends PushableWearhouseObject {
        public Box(Vec pos) {
            super(pos);
        }

        @Override
        public ObjectType getType() {
            return ObjectType.BOX;
        }

        @Override
        public String toString() {
            return "O";
        }
    }

    public static class WideBox extends PushableWearhouseObject {
        public WideBox(Vec pos) {
            super(pos);
        }

        @Override
        public ObjectType getType() {
            return ObjectType.WIDE_BOX;
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        public boolean canPush(Vec dir, Wearhouse wearhouse) {
            Vec nextSpot = pos.move(dir);
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();
            boolean isAllowed = true;
            if(dir.equals(UP) || dir.equals(DOWN)) {
                if(objects.containsKey(pos.move(dir))) {
                    isAllowed &= objects.get(nextSpot).canPush(dir, wearhouse);
                }
                if(objects.containsKey(pos.move(dir).move(RIGHT))) {
                    isAllowed &= objects.get(nextSpot.move(RIGHT)).canPush(dir, wearhouse);
                }
            } else {
                if(dir.equals(LEFT)) {
                    if(objects.containsKey(nextSpot)) {
                        isAllowed &= objects.get(nextSpot).canPush(dir, wearhouse);
                    }
                } else {
                    if(objects.containsKey(nextSpot.move(RIGHT))) {
                        isAllowed &= objects.get(nextSpot.move(RIGHT)).canPush(dir, wearhouse);
                    }
                }
            }
            return isAllowed;
        }

        @Override
        public void push(Vec dir, Wearhouse wearhouse) {
            Vec nextSpot = pos.move(dir);
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();
            if(dir.equals(UP) || dir.equals(DOWN)) {
                if(objects.containsKey(pos.move(dir))) {
                    objects.get(nextSpot).push(dir, wearhouse);
                }
                if(objects.containsKey(pos.move(dir).move(RIGHT))) {
                    objects.get(nextSpot.move(RIGHT)).push(dir, wearhouse);
                }
            } else {
                if(dir.equals(LEFT)) {
                    if(objects.containsKey(nextSpot)) {
                        objects.get(nextSpot).push(dir, wearhouse);
                    }
                } else {
                    if(objects.containsKey(nextSpot.move(RIGHT))) {
                        objects.get(nextSpot.move(RIGHT)).push(dir, wearhouse);
                    }
                }
            }

            pushSelf(dir, wearhouse);
        }

        private void pushSelf(Vec dir, Wearhouse wearhouse) {
            HashMap<Vec, WearhouseObject> objects = wearhouse.getObjects();

            objects.remove(pos);    // remove yourself from the map
            objects.remove(pos.move(RIGHT));

            // add yourself back to the map
            pos = pos.move(dir);
            objects.put(pos, this);
            objects.put(pos.move(RIGHT), this);
        }
    }

    public static class Robot extends PushableWearhouseObject {
        public Robot(Vec pos) {
            super(pos);
        }

        @Override
        public ObjectType getType() {
            return ObjectType.ROBOT;
        }

        @Override
        public String toString() {
            return "@";
        }

        public Vec pushRobot(Vec dir, Wearhouse wearhouse) {
            tryPush(dir, wearhouse);
            return pos;
        }
    }

    @Data
    @AllArgsConstructor
    public static class Wearhouse {
        HashMap<Vec, WearhouseObject> objects;
        int width;
        int height;
        Vec robot_pos;

        public void moveRobot(Vec dir) {
            WearhouseObject object = objects.get(robot_pos);
            if(object.getType() != WearhouseObject.ObjectType.ROBOT) {
                System.out.println("Lost Track of the Robot");
                assert false;
                return;
            }
            Robot robot = (Robot) object;
            robot_pos = robot.pushRobot(dir, this);
        }

        public void printWearhouse() {
            StringBuilder string = new StringBuilder();
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Vec pos = new Vec(x, y);
                    if(objects.containsKey(pos)) {
                        string.append(objects.get(pos).toString());
                        if(objects.get(pos).getType() == WearhouseObject.ObjectType.WIDE_BOX) {
                            x++;
                        }
                    } else {
                        string.append(".");
                    }
                }
                string.append('\n');
            }
            System.out.println(string.toString());
        }

        public int calcGPS() {
            int gps_sum = 0;
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Vec pos = new Vec(x, y);
                    if(!objects.containsKey(pos)) {
                        continue;
                    }
                    WearhouseObject.ObjectType type = objects.get(pos).getType();
                    if(type != WearhouseObject.ObjectType.BOX
                            && type != WearhouseObject.ObjectType.WIDE_BOX) {
                        continue;
                    }
                    gps_sum += pos.x + (pos.y * 100);
                    if(type == WearhouseObject.ObjectType.WIDE_BOX) {
                        x++;
                    }
                }
            }
            return gps_sum;
        }
    }

    public static List<Vec> readDirectionInput(String filePath) {
        List<Vec> robot_directions = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                List<Vec> new_directions = line.chars().mapToObj(c -> Vec.fromChar((char) c)).collect(Collectors.toList());
                robot_directions.addAll(new_directions);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return robot_directions;
    }

    public static Wearhouse readMapInput(String filePath) {
        HashMap<Vec, WearhouseObject> objects = new HashMap<>();
        int width = 0;
        int height = 0;
        Vec robot_pos = null;
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                width = line.length();

                for(int i = 0; i < width; i++) {
                    Vec pos = new Vec(i, height);
                    switch (line.charAt(i)) {
                        case '#':
                            objects.put(pos, new Wall());
                            break;
                        case 'O':
                            objects.put(pos, new Box(pos));
                            break;
                        case '@':
                            robot_pos = new Vec(pos);
                            objects.put(pos, new Robot(pos));
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
        assert robot_pos != null;

        return new Wearhouse(objects, width, height, robot_pos);
    }

    public static Wearhouse readMapInput_2(String filePath) {
        HashMap<Vec, WearhouseObject> objects = new HashMap<>();
        int width = 0;
        int height = 0;
        Vec robot_pos = null;
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                width = line.length();

                for(int i = 0; i < width; i++) {
                    Vec pos = new Vec(i * 2, height);
                    switch (line.charAt(i)) {
                        case '#':
                            objects.put(pos, new Wall());
                            objects.put(pos.move(RIGHT), new Wall());
                            break;
                        case 'O':
                            WideBox box = new WideBox(pos);
                            objects.put(pos, box);
                            objects.put(pos.move(RIGHT), box);
                            break;
                        case '@':
                            robot_pos = new Vec(pos);
                            objects.put(pos, new Robot(pos));
                            break;
                    }
                }
                height++;
            }
            width *= 2;
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        assert robot_pos != null;

        return new Wearhouse(objects, width, height, robot_pos);
    }

    public static int star(Wearhouse wearhouse, List<Vec> push_directions) {
        for(Vec push_dir : push_directions) {
            wearhouse.moveRobot(push_dir);
        }
        return wearhouse.calcGPS();
    }

    public static void main(String[] args) {
        List<Vec> push_directions = readDirectionInput("./src/main/java/AoC/day15/input_directions.txt");
        Wearhouse wearhouse_star1 = readMapInput("./src/main/java/AoC/day15/input_map.txt");
        int star1 = star(wearhouse_star1, push_directions);
        System.out.println(star1);

        Wearhouse wearhouse_star2 = readMapInput_2("./src/main/java/AoC/day15/input_map.txt");
        int star2 = star(wearhouse_star2, push_directions);
        System.out.println(star2);
        wearhouse_star2.printWearhouse();
    }
}
