import AoC.day12.Day12;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Day12Test {
    @Test
    void PlotCountTest() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/testInput.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        Assertions.assertEquals(11, plots.size());
    }

    @Test
    void FenceSizeTest() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/donutFence.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        Day12.Plot A_plot = null;
        for(Day12.Plot plot : plots) {
            if(plot.getPlotChar() == 'A') {
                A_plot = plot;
                break;
            }
        }
        assert A_plot != null;
        Assertions.assertEquals(40, Day12.getFenceSize(A_plot));
    }

    @Test
    void countEdgeInsideCorners() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/donutFence.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        Day12.Plot A_plot = null;
        for(Day12.Plot plot : plots) {
            if(plot.getPlotChar() == 'A') {
                A_plot = plot;
                break;
            }
        }
        assert A_plot != null;
    }

    @Test
    void countEdgesTest() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/donutFence.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        Day12.Plot A_plot = null;
        for(Day12.Plot plot : plots) {
            if(plot.getPlotChar() == 'A') {
                A_plot = plot;
                break;
            }
        }
        assert A_plot != null;
        Assertions.assertEquals(24, Day12.countEdges(A_plot));
    }

    @Test
    void Star1Test() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/testInput.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        int star1 = Day12.star1(plots);
        Assertions.assertEquals(1930, star1);
    }

    @Test
    void Star2Test() {
        Day12.GardenMap map = Day12.readInput("./src/main/java/AoC/day12/testInput.txt");
        List<Day12.Plot> plots = Day12.findPlots(map);
        int star1 = Day12.star2(plots);
        Assertions.assertEquals(1206, star1);
    }
}
