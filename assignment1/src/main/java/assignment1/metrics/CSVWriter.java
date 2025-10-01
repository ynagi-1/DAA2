package assignment1.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {

    private final String filename;

    public CSVWriter(String filename) {
        this.filename = filename;
        ensureHeader();
    }


    private void ensureHeader() {
        File file = new File(filename);
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
                pw.println("alg,size,time_ms,comp,alloc,maxdepth");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void writeMetrics(String algorithm, int size, Metrics metrics, double time_ms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            pw.printf("%s,%d,%f,%d,%d,%d%n",
                    algorithm, size,
                    time_ms,
                    metrics.comparisons,
                    metrics.allocations,
                    metrics.maxDepth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}