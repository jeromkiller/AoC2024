package AoC.day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {

    public static List<String> readInputTowels(String filePath) {
        List<String> towels = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            if(reader.hasNext()) {
                String line = reader.nextLine();
                String[] splitLine = line.split(", ");
                towels.addAll(Arrays.stream(splitLine).collect(Collectors.toList()));
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return towels;
    }

    public static List<String> readInputDesigns(String filePath) {
        List<String> patterns = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            reader.nextLine();
            reader.nextLine();
            while(reader.hasNext()) {
                String line = reader.nextLine();
                patterns.add(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        return patterns;
    }

    // remove any strings from the list that can be created from substrings
    public static List<String> removeProducableTowels(List<String> input) {
        List<String> unproducable = new ArrayList<>();
        for(String towel : input) {
            List<String> towels = new ArrayList<>(input);
            towels.remove(towel);
            if(!canProduce("", towels, towel)) {
                unproducable.add(towel);
            }
        }
        return unproducable;
    }

    public static boolean canProduce(final String current, final List<String> towels, final String goal) {
        for(String towel : towels) {
            String combined = current + towel;
            if(goal.equals(combined)) {
                return true;
            }

            if(goal.indexOf(combined) == 0) {
                if(canProduce(combined, towels, goal)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long countPermutationsCached(final String current, final List<String> towels, final String goal, HashMap<String, Long> producableCache) {
        long total = 0;
        for (String towel : towels) {
            String combined = current + towel;
            if(producableCache.containsKey(combined)) {
                total += producableCache.get(combined);
                continue;
            } else {
                producableCache.put(combined, 0L);
            }

            if (goal.equals(combined)) {
                total += 1;
                producableCache.put(combined, total);
                continue;
            }

            if (goal.indexOf(combined) == 0) {
                long count = countPermutationsCached(combined, towels, goal, producableCache);
                total += count;
                if (count > 0) {
                    producableCache.put(combined, count);
                    continue;
                }
            }
        }
        return total;

    }
    public static List<String> getPossiblePatterns(List<String> towels, List<String> patterns) {
        return patterns.stream().filter(pattern -> canProduce("", towels, pattern)).collect(Collectors.toList());
    }

    public static int star1(List<String> towels, List<String> patterns) {
        List<String> filtered_towels = removeProducableTowels(towels);
        return getPossiblePatterns(filtered_towels, patterns).size();
    }

    public static long star2(List<String> towels, List<String> patterns) {
        List<Long> permutationCount = patterns.stream().map(pattern -> countPermutationsCached("", towels, pattern, new HashMap<>())).collect(Collectors.toList());
        return permutationCount.stream().mapToLong(Long::longValue).sum();
    }

    public static void main(String[] args) {
        List<String> towels = readInputTowels("./src/main/java/AoC/day19/input.txt");
        List<String> designs = readInputDesigns("./src/main/java/AoC/day19/input.txt");
        int star1 = star1(towels, designs);
        System.out.println("Star1: " + star1);

        long star2 = star2(towels, designs);
        System.out.println("Star2: " + star2);
    }
}
