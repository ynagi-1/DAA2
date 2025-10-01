package assignment1.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class BenchPlotter {

    static class BenchEntry {
        String alg;
        int size;
        double time;
        int comp;
        int alloc;
        int maxDepth;

        BenchEntry(String[] tokens) {
            String[] parts = tokens[0].split("\\|");
            this.alg = parts[0].trim();             
            this.size = Integer.parseInt(tokens[1].trim());
            this.time = Double.parseDouble(tokens[2].trim());
            this.comp = Integer.parseInt(tokens[3].trim());
            this.alloc = Integer.parseInt(tokens[4].trim());
            this.maxDepth = Integer.parseInt(tokens[5].trim());
        }
    }

    public static List<BenchEntry> readCSV(String path) throws Exception {
        List<BenchEntry> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                data.add(new BenchEntry(tokens));
            }
        }
        System.out.println("Loaded " + data.size() + " benchmark entries.");
        return data;
    }

    private static Map<Integer, Double> averageTime(List<BenchEntry> entries) {
        Map<Integer, List<Double>> temp = new HashMap<>();
        for (BenchEntry e : entries) {
            temp.computeIfAbsent(e.size, k -> new ArrayList<>()).add(e.time);
        }
        Map<Integer, Double> avg = new TreeMap<>();
        for (Map.Entry<Integer, List<Double>> e : temp.entrySet()) {
            double mean = e.getValue().stream().mapToDouble(d -> d).average().orElse(0);
            avg.put(e.getKey(), mean);
        }
        return avg;
    }

    private static Map<Integer, Integer> maxDepth(List<BenchEntry> entries) {
        Map<Integer, Integer> temp = new HashMap<>();
        for (BenchEntry e : entries) {
            temp.merge(e.size, e.maxDepth, Math::max);
        }
        return new TreeMap<>(temp);
    }

    public static void plotTimeVsN(List<BenchEntry> data) {
        XYSeries mergeSeries = new XYSeries("MergeSort");
        XYSeries quickSeries = new XYSeries("QuickSort");

        Map<Integer, Double> mergeAvg = averageTime(filterByAlg(data, "MergeSort"));
        Map<Integer, Double> quickAvg = averageTime(filterByAlg(data, "QuickSort"));

        mergeAvg.forEach(mergeSeries::add);
        quickAvg.forEach(quickSeries::add);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(mergeSeries);
        dataset.addSeries(quickSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Time vs N",
                "Array Size",
                "Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        displayChart(chart, "Time vs N");
    }

    public static void plotDepthVsN(List<BenchEntry> data) {
        XYSeries mergeSeries = new XYSeries("MergeSort");
        XYSeries quickSeries = new XYSeries("QuickSort");

        Map<Integer, Integer> mergeMax = maxDepth(filterByAlg(data, "MergeSort"));
        Map<Integer, Integer> quickMax = maxDepth(filterByAlg(data, "QuickSort"));

        mergeMax.forEach(mergeSeries::add);
        quickMax.forEach(quickSeries::add);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(mergeSeries);
        dataset.addSeries(quickSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Max Depth vs N",
                "Array Size",
                "Max Depth",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        displayChart(chart, "Max Depth vs N");
    }

    private static List<BenchEntry> filterByAlg(List<BenchEntry> data, String alg) {
        List<BenchEntry> filtered = new ArrayList<>();
        for (BenchEntry e : data) {
            if (e.alg.equalsIgnoreCase(alg)) filtered.add(e);
        }
        return filtered;
    }

    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {

        List<BenchEntry> data = readCSV("/Users/nifflyar/vscode/DAA/daa_assignment1/assignment1/src/main/java/assignment1/bench/bench-results.csv");

        plotTimeVsN(data);
        plotDepthVsN(data);
    }
}
