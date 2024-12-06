package AoC.day5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {
    public static HashMap<Integer, HashSet<Integer>> readOrderInput(String filePath) {
        HashMap<Integer, HashSet<Integer>> order = new LinkedHashMap<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                String[] splitNums = newLine.split("\\|");
                int page = Integer.parseInt(splitNums[0]);
                int before = Integer.parseInt(splitNums[1]);
                if(!order.containsKey(page)){
                    order.put(page, new HashSet<>());
                }
                order.get(page).add(before);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return order;
    }

    public static List<List<Integer>> readPagesInput(String filePath) {
        List<List<Integer>> pages_list = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                List<Integer> pages_line = Arrays.stream(newLine.split(",")).map(Integer::valueOf).collect(Collectors.toList());
                pages_list.add(pages_line);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return pages_list;
    }

    public static int isBefore(List<Integer> pages, int beforeNum, int afterNum) {
        int loc_before = pages.indexOf(beforeNum);
        int loc_after = pages.indexOf(afterNum);

        if(loc_before < 0 || loc_after < 0) {
            // either value does not exist
            // so can't be ordered
            // assume its in the right spot
            return -1;
        }

        return loc_before - loc_after;
    }

    public static boolean isInRightSpot(List<Integer> pages, int beforeNum, HashSet<Integer> afterNums) {
        if (afterNums == null) {
            // Last number can't have any ordering rules,
            // so we'll assume its in the right spot
            return true;
        }
        return afterNums.stream().allMatch(afterNum -> isBefore(pages, beforeNum, afterNum) < 0);
    }

    public static boolean inOrder(List<Integer> pages, HashMap<Integer, HashSet<Integer>> order) {
        return pages.stream().allMatch(val -> isInRightSpot(pages, val, order.get(val)));
    }

    public static int findMiddleNum(List<Integer> pages) {
        return pages.get(pages.size() / 2);
    }

    public static int myComparator(int beforeNum, int afterNum, HashMap<Integer, HashSet<Integer>> order) {
        if(order.containsKey(beforeNum)) {
            final int compare = order.get(beforeNum).contains(afterNum) ? -1 : 1;
            return compare;
        }
        return 1;
    }

    public static List<Integer> sort(List<Integer> pages, HashMap<Integer, HashSet<Integer>> order_rules) {
        List<Integer> sorted = new ArrayList<>(pages);
        sorted.sort((p1, p2) -> myComparator(p1, p2, order_rules));
        return sorted;
    }

    public static int star1(List<List<Integer>> pages, HashMap<Integer, HashSet<Integer>> order) {
        return pages.stream().filter(p -> inOrder(p, order)).map(Day5::findMiddleNum).mapToInt(Integer::intValue).sum();
    }

    public static int star2(List<List<Integer>> pages, HashMap<Integer, HashSet<Integer>> order) {
        return pages.stream().filter(p -> !inOrder(p, order)).map(p -> sort(p, order)).map(Day5::findMiddleNum).mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) {
        List<List<Integer>> pages_list = Day5.readPagesInput("./src/main/java/AoC/Day5/inputPages.txt");
        HashMap<Integer, HashSet<Integer>> ordering = Day5.readOrderInput("./src/main/java/AoC/Day5/inputOrder.txt");

        int star1 = Day5.star1(pages_list, ordering);
        System.out.println("Star1: " + star1);

        int star2 = Day5.star2(pages_list, ordering);
        System.out.println("Star2: " + star2);
    }

}
