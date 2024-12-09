package AoC.day09;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 {
    public static ArrayList<Character> getInputLine(String filepath) {
        ArrayList<Character> diskMap = new ArrayList<>();
        try {
            File inputFile = new File(filepath);
            Scanner reader = new Scanner(inputFile);
            diskMap = reader.nextLine().chars().map(c -> c - '0').mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));

            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return diskMap;
    }

    public static ArrayList<Short> toFullMap(final ArrayList<Character> diskMap) {
        ArrayList<Short> disk = new ArrayList<>(countDiskSpace(diskMap));
        boolean is_empty = false;
        short file_id = 0;
        for(char c : diskMap){
            if(is_empty) {
                disk.addAll(Collections.nCopies(c, (short) -1));
            } else {
                disk.addAll(Collections.nCopies(c, file_id));
                file_id++;
            }
            is_empty = !is_empty;
        }
        return disk;
    }

    public static void backFill(ArrayList<Short> map){
        int head_p = 0;
        int tail_p = map.size() -1;
        while(head_p < tail_p) {
            if(map.get(head_p) == -1) {
                map.set(head_p, map.get(tail_p));
                map.set(tail_p, (short)-1);
                do {
                    tail_p--;
                } while(map.get(tail_p) == -1);
            }
            head_p++;
        }
    }

    // only really works for the test input
    public static String toString(ArrayList<Short> map) {
        StringBuilder str = new StringBuilder();
        for(short s : map) {
            String putChar = s < 0 ? ".": String.valueOf((int)(s % 10));
            str.append(putChar);
        }
        return str.toString();
    }

    public static long calcChecksum(final ArrayList<Short> map) {
        long checksum = 0;
        for(int i = 0; i < map.size(); i++) {
            if(map.get(i) == -1) {
                continue;
            }
            checksum += i * map.get(i);
        }
        return checksum;
    }

    public static int countDiskSpace(final ArrayList<Character> diskMap) {
        return diskMap.stream().mapToInt(Character::valueOf).sum();
    }

    public static long countFiles(final ArrayList<Character> diskMap) {
        return diskMap.stream().count() / 2;
    }

    public static long calcChecksum(int start, int stop, int file_id) {
        return IntStream.range(start, stop).map(i -> i * file_id).sum();
    }

    public static int findSpan(int start, ArrayList<Short> map, boolean forwards) {
        short val = map.get(start);
        int next = forwards ? 1 : -1;
        int span = 0;
        while(val == map.get(start + span)) {
            if((start + span) < 0 || (start + span) >= map.size()) {
                break;
            }
            span += next;
        }
        return Math.abs(span);
    }

    @Value
    @AllArgsConstructor
    public static class FileSpan {
        short file_id;
        char file_span;
    }

    public static ArrayList<FileSpan> toSpans(ArrayList<Character> diskMap) {
        ArrayList<FileSpan> spans = new ArrayList<>(countDiskSpace(diskMap));
        boolean is_empty = false;
        short file_id = 0;
        for(char c : diskMap){
            if(is_empty) {
                spans.add(new FileSpan((short) -1, c));
            } else {
                spans.add(new FileSpan(file_id, c));
                file_id++;
            }
            is_empty = !is_empty;
        }
        return spans;
    }

    public static int findFirstFreeSpanIndex(ArrayList<FileSpan> map, char size) {
        int i = 0;
        for(i = 0; i < map.size(); i++) {
            FileSpan file = map.get(i);
            if(file.file_id == -1 && file.file_span >= size) {
                break;
            }
        }
        if(i == map.size()) {
            return -1;
        }
        return i;
    }

    public static void backfillSpans(ArrayList<FileSpan> map) {
        ArrayList<FileSpan> map_copy = new ArrayList<>(map);
        for(int i = map_copy.size() -1; i >= 0; i--) {
            FileSpan file = map_copy.get(i);
            if(file.file_id == -1) {
                continue;
            }
            int freeIndex = findFirstFreeSpanIndex(map, file.file_span);
            int fileIndex = map.indexOf(file);

            if(freeIndex > fileIndex || freeIndex < 0)
                continue;

            FileSpan freeSpan = map.get(freeIndex);

            map.set(fileIndex, new FileSpan((short) -1, file.file_span));
            map.set(freeIndex, file);
            if(freeSpan.file_span != file.file_span) {
                char remaining_space = (char) (freeSpan.file_span - file.file_span);
                map.add(freeIndex + 1, new FileSpan((short) -1, remaining_space));
            }
        }
    }

    public static ArrayList<Short> SpanToFullMap(final ArrayList<FileSpan> diskMap) {
        ArrayList<Short> disk = new ArrayList<>();
        for(FileSpan f : diskMap){
            disk.addAll(Collections.nCopies(f.file_span, f.file_id));
        }
        return disk;
    }

    // only really works for the test input
    public static String SpantoString(ArrayList<FileSpan> map) {
        StringBuilder str = new StringBuilder();
        for(FileSpan s : map) {
            String putChar = s.file_id < 0 ? ".": String.valueOf((int)(s.file_id % 10));
            str.append(putChar.repeat(s.file_span));
        }
        return str.toString();
    }

    public static long star1(ArrayList<Character> diskMap) {
        ArrayList<Short> fullDisk = toFullMap(diskMap);
        backFill(fullDisk);
        return calcChecksum(fullDisk);
    }

    public static long star2(ArrayList<Character> diskMap) {
        ArrayList<FileSpan> fullDisk = toSpans(diskMap);
        backfillSpans(fullDisk);
        ArrayList<Short> fullMap = SpanToFullMap(fullDisk);
        return calcChecksum(fullMap);
    }


    public static void main(String[] args) {
        ArrayList<Character> diskMap = getInputLine("./src/main/java/AoC/day09/input.txt");
        long star1 = star1(diskMap);
        System.out.println(star1);

        ArrayList<Character> diskMap2 = getInputLine("./src/main/java/AoC/day09/input.txt");
        long star2 = star2(diskMap2);
        System.out.println(star2);
    }
}
