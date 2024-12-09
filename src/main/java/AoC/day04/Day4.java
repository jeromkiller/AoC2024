package AoC.day04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day4 {
    public static List<Character> strToCharList(String str) {
        return str.chars().mapToObj(c -> (char)c).collect(Collectors.toList());
    }

    public static class CrossWord {
        public static char border_char = '=';

        public List<List<Character>> data;
        public HashSet<List<Integer>> foundLetters;
        public int numFound;

        public CrossWord(List<String> input) {
            numFound = 0;
            foundLetters = new HashSet<>();
            data = new ArrayList<>();
            for(String line: input) {
                List<Character> row = strToCharList(line);
                data.add(row);
            }
        }

        public char getChar(int x, int y) {
            return data.get(y).get(x);
        }

        public int width() {
            return data.get(0).size();
        }

        public int height() {
            return data.size();
        }

        public void addFoundLetters(HashSet<List<Integer>> foundLetters) {
            this.foundLetters.addAll(foundLetters);
        }

        public void incNumFound() {
            this.numFound++;
        }

        public void addBorder(int width) {
            //add padding to the sides first
            for(List<Character> row : data) {
                List<Character> edge = Collections.nCopies(width, border_char);
                row.addAll(0, edge);
                row.addAll(row.size(), edge);
            }

            final int length = data.get(0).size();
            List<Character> edge_colum = Collections.nCopies(length, border_char);
            List<List<Character>> edge = new ArrayList<>(width);
            for(int i = 0; i < width; i++) {
                edge.add(edge_colum);
            }
            data.addAll(0, edge);
            data.addAll(data.size(), edge);
        }

        public String toString() {
            return toString(false);
        }

        public String toString(boolean filterFound) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int y = 0; y < height(); y++) {
                StringBuilder line = new StringBuilder();
                for(int x = 0; x < width(); x++) {
                    Character c = getChar(x, y);
                    if(c == border_char) {
                        continue;
                    }
                    if(filterFound) {
                        if(foundLetters.contains(List.of(x, y))) {
                            line.append(c);
                        } else {
                            line.append('.');
                        }
                    } else {
                        line.append(c);
                    }
                }
                if(!line.toString().isEmpty()) {
                    line.append("\n");
                    stringBuilder.append(line.toString());
                }
            }
            return stringBuilder.toString();
        }
    }

    public static List<String> readInput(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNext()) {
                String newLine = reader.nextLine();
                lines.add(newLine);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean findLetters(CrossWord cw, int x_start, int y_start, int dx, int dy, int i, List<Character> findWord, HashSet<List<Integer>> foundLetters) {
        // skip if we're going nowhere
        if(dx == 0 && dy == 0)
            return false;

        int x = x_start + (dx * i);
        int y = y_start + (dy * i);

        // is this the right letter?
        if(cw.getChar(x, y) != findWord.get(i)) {
            return false;
        }

        // add the letter to the found letters list
        foundLetters.add(List.of(x, y));

        // is this the full word?
        i++;
        if(i == findWord.size()) {

            return true;
        }

        // try to find the next letter
        return findLetters(cw, x_start, y_start, dx, dy, i, findWord, foundLetters);
    }

    public static void findWords(CrossWord cw, int x_start, int y_start, List<Character> findWord) {
        if(cw.getChar(x_start, y_start) != findWord.get(0)) {
            return;
        }

        int totalFound = 0;
        for(int y = -1; y < 2; y++) {
            for(int x = -1; x < 2; x++){
                HashSet<List<Integer>> letterLocs = new HashSet<>();
                boolean found = Day4.findLetters(cw, x_start, y_start, y, x, 0, findWord, letterLocs);
                if(found) {
                    cw.addFoundLetters(letterLocs);
                    cw.incNumFound();
                }
            }
        }
    }

    public static void star01(CrossWord cw, String findWord) {
        List<Character> findChars = strToCharList(findWord);
        cw.addBorder(findChars.size());

        for(int y = 0; y < cw.height(); y++) {
            for(int x = 0; x < cw.width(); x ++) {
                //find the first letter in the sequence
                findWords(cw, x, y, findChars);
            }
        }
    }

    public static boolean findS(CrossWord cw, int x_start, int y_start, int dx, int dy, HashSet<List<Integer>> foundLetters) {
        final int x = x_start - dx;
        final int y = y_start - dy;
        if(cw.getChar(x, y) != 'S') {
            return false;
        }

        foundLetters.add(List.of(x, y));
        return true;
    }

    public static boolean findM(CrossWord cw, int x_start, int y_start, int dx, int dy, HashSet<List<Integer>> foundLetters) {
        final int x = x_start + dx;
        final int y = y_start + dy;
        if(cw.getChar(x, y) != 'M') {
            return false;
        }

        if(!findS(cw, x_start, y_start, dx, dy, foundLetters)) {
            return false;
        }

        foundLetters.add(List.of(x, y));
        return true;
    }

    public static void findA(CrossWord cw, int x_start, int y_start) {
        List<HashSet<List<Integer>>> totalFound = new ArrayList<>();
        if(cw.getChar(x_start, y_start) == 'A') {
            // look for the other other letters in the corners
            for(int y = -1; y < 2; y += 2) {
                for(int x = -1; x < 2; x += 2) {
                    HashSet<List<Integer>> foundLetters = new HashSet<List<Integer>>();
                    if(findM(cw, x_start, y_start, x, y, foundLetters)) {
                        totalFound.add(foundLetters);
                    }
                }
            }
        }
        if(totalFound.size() != 2) {
            return;
        }

        for(HashSet<List<Integer>> foundLetters: totalFound) {
            foundLetters.add(List.of(x_start, y_start));
            cw.addFoundLetters(foundLetters);
        }
        cw.incNumFound();
    }

    public static void star02(CrossWord cw) {
        cw.addBorder(1);

        for(int y = 0; y < cw.height(); y++) {
            for(int x = 0; x < cw.width(); x ++) {
                //find the first letter in the sequence
                findA(cw, x, y);
            }
        }
    }

    public static void main(String[] args) {
        List<String> input = readInput("./src/main/java/AoC/day4/input.txt");
        CrossWord star1Crossword = new CrossWord(input);
        star01(star1Crossword, "XMAS");
        System.out.println("Star1: " + star1Crossword.numFound);


        CrossWord star2Crossword = new CrossWord(input);
        star02(star2Crossword);
        System.out.println("Star2: " + star2Crossword.numFound);
    }
}
