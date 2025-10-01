package assignment1.bench;

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

public class SelectClosestPlotter {

    static class BenchEntry {
        String alg;
        int size;
        double timeMs;

        BenchEntry(String[] tokens, boolean isBench) {
            if (isBench) {
                String[] parts = tokens[0].split("\\|");
                this.alg = parts[0].trim();
                this.size = Integer.parseInt(tokens[1].trim());
                this.timeMs = Double.parseDouble(tokens[2].trim()) / 1_000_00;
            } else {
                this.alg = "closest";
                this.size = Integer.parseInt(tokens[0].trim());
                this.timeMs = Double.parseDouble(tokens[1].trim());
            }
        }
    }

    public static List<BenchEntry> readCSV(String path, boolean isBench) throws Exception {
        List<BenchEntry> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] tokens = line.split(",");
                data.add(new BenchEntry(tokens, isBench));
            }
        }
        return data;
    }

    private static List<BenchEntry> filterByAlgAndSize(List<BenchEntry> data, String alg, int minSize, int maxSize) {
        List<BenchEntry> filtered = new ArrayList<>();
        for (BenchEntry e : data) {
            if (e.alg.equalsIgnoreCase(alg) && e.size >= minSize && e.size <= maxSize) {
                filtered.add(e);
            }
        }
        return filtered;
    }

    private static Map<Integer, Double> averageTime(List<BenchEntry> entries) {
        Map<Integer, List<Double>> temp = new HashMap<>();
        for (BenchEntry e : entries) {
            temp.computeIfAbsent(e.size, k -> new ArrayList<>()).add(e.timeMs);
        }
        Map<Integer, Double> avg = new TreeMap<>();
        for (Map.Entry<Integer, List<Double>> e : temp.entrySet()) {
            double mean = e.getValue().stream().mapToDouble(d -> d).average().orElse(0);
            avg.put(e.getKey(), mean);
        }
        return avg;
    }

    public static void plotTime(List<BenchEntry> data, int minSize, int maxSize) {
        XYSeries selectSeries = new XYSeries("Select (MOM)");
        XYSeries closestSeries = new XYSeries("ClosestPair");

        averageTime(filterByAlgAndSize(data, "select_mom", minSize, maxSize)).forEach(selectSeries::add);
        averageTime(filterByAlgAndSize(data, "closest", minSize, maxSize)).forEach(closestSeries::add);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(selectSeries);
        dataset.addSeries(closestSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Time vs N (Select & Closest)",
                "Input Size",
                "Time (ms)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        displayChart(chart, "Time vs N (Select & Closest)");
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
        List<BenchEntry> data = new ArrayList<>();
        data.addAll(readCSV("/Users/nifflyar/vscode/DAA/daa_assignment1/assignment1/src/main/java/assignment1/bench/bench-results.csv", true));
        data.addAll(readCSV("/Users/nifflyar/vscode/DAA/daa_assignment1/assignment1/src/main/java/assignment1/bench/closest-results.csv", false));

        plotTime(data, 100, 1000);
    }
}
