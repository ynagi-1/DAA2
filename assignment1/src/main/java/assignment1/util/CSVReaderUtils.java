package assignment1.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtils {

    public static List<Double> readColumn(String filePath, int columnIndex) throws Exception {
        List<Double> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                result.add(Double.parseDouble(parts[columnIndex]));
            }
        }
        return result;
    }

    public static List<Integer> readColumnInt(String filePath, int columnIndex) throws Exception {
        List<Integer> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                result.add(Integer.parseInt(parts[columnIndex]));
            }
        }
        return result;
    }
}
