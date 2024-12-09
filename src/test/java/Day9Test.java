import AoC.day9.Day9;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day9Test {

    String getInputLine(String filepath) {
        String returnLine = "";
        try {
            File inputFile = new File(filepath);
            Scanner reader = new Scanner(inputFile);
            returnLine = reader.nextLine();

            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        return returnLine;
    }

    String getInputString() {
        return getInputLine("./src/main/java/AoC/day9/input.txt");
    }

    String getTestInputString() {
        return getInputLine("./src/main/java/AoC/day9/testInput.txt");
    }

    @Test
    void CountTotalDiskSpace() {
        int diskSpace = getInputString().chars().map(c -> c - '0').reduce(0, Integer::sum);
        System.out.println("number of blocks on disk: " + diskSpace);
        Assertions.assertTrue(diskSpace > 0);
    }

    @Test
    void CountNumFiles() {
        long fileCount = getInputString().chars().count() / 2 + 1;
        System.out.println("number of files on disk: " + fileCount);
        Assertions.assertTrue(fileCount > 0L);
    }

    @Test
    void testDiskFullMap() {
        ArrayList<Short> fullDisk = Day9.toFullMap(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        String expected = "00...111...2...333.44.5555.6666.777.888899";
        String str = Day9.toString(fullDisk);
        Assertions.assertEquals(expected, str);
    }

    @Test
    void testBackFill() {
        ArrayList<Short> fullDisk = Day9.toFullMap(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        Day9.backFill(fullDisk);
        String expected = "0099811188827773336446555566..............";
        String str = Day9.toString(fullDisk);
        Assertions.assertEquals(expected, str);
    }

    @Test
    void testCheckSum() {
        ArrayList<Short> fullDisk = Day9.toFullMap(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        Day9.backFill(fullDisk);
        long checksum = Day9.calcChecksum(fullDisk);
        Assertions.assertEquals(1928, checksum);
    }

    @Test
    void testSpanList() {
        ArrayList<Day9.FileSpan> fullDisk = Day9.toSpans(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        String expected = "00...111...2...333.44.5555.6666.777.888899";
        String str = Day9.SpantoString(fullDisk);
        Assertions.assertEquals(expected, str);
    }

    @Test
    void testSpanBackFill() {
        ArrayList<Day9.FileSpan> fullDisk = Day9.toSpans(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        Day9.backfillSpans(fullDisk);
        ArrayList<Short> fullMap = Day9.SpanToFullMap(fullDisk);
        String expected = "00992111777.44.333....5555.6666.....8888..";
        String str = Day9.toString(fullMap);
        Assertions.assertEquals(expected, str);
    }

    @Test
    void star2Test() {
        ArrayList<Day9.FileSpan> fullDisk = Day9.toSpans(Day9.getInputLine("./src/main/java/AoC/day9/testInput.txt"));
        Day9.backfillSpans(fullDisk);
        ArrayList<Short> fullMap = Day9.SpanToFullMap(fullDisk);
        long checksum = Day9.calcChecksum(fullMap);
        Assertions.assertEquals(2858, checksum);
    }
}