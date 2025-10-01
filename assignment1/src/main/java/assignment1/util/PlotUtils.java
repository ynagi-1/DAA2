package assignment1.util;

import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class PlotUtils {


    public static void plotTimeVsN(List<Integer> ns,
                                   List<Double> mergeTimes,
                                   List<Double> quickTimes,
                                   List<Double> selectTimes) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < ns.size(); i++) {
            dataset.addValue(mergeTimes.get(i), "MergeSort", ns.get(i));
            dataset.addValue(quickTimes.get(i), "QuickSort", ns.get(i));
            dataset.addValue(selectTimes.get(i), "DeterministicSelect", ns.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Time vs n",
                "Array size n",
                "Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        showChart(chart);
    }


    public static void plotDepthVsN(List<Integer> ns,
                                    List<Integer> mergeDepths,
                                    List<Integer> quickDepths,
                                    List<Integer> selectDepths) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < ns.size(); i++) {
            dataset.addValue(mergeDepths.get(i), "MergeSort Depth", ns.get(i));
            dataset.addValue(quickDepths.get(i), "QuickSort Depth", ns.get(i));
            dataset.addValue(selectDepths.get(i), "DeterministicSelect Depth", ns.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Recursion Depth vs n",
                "Array size n",
                "Recursion depth",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        showChart(chart);
    }

    private static void showChart(JFreeChart chart) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ChartPanel(chart));
        frame.setVisible(true);
    }
}
