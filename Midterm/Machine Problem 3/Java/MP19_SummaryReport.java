import java.io.*;
import java.util.*;
import java.util.stream.*;

public class MP19_SummaryReport {

    static final int[] REQUIRED = {0, 1, 3, 4, 5, 6, 7, 8};

    public static void main(String[] args) throws Exception {
        String filePath = args.length > 0 ? args[0] : "C:\\Users\\Cleo\\Documents\\Midterm\\Prog2-9307-AY225-TINSAY\\Midterm\\Machine Problem 3\\Java\\MachineProblem3.csv";
        List<String[]> rows = loadCSV(filePath);
        System.out.println("Loaded " + rows.size() + " data rows.\n");
        mp19_summaryReport(rows);
    }

    static List<String[]> loadCSV(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            String classDir = MP19_SummaryReport.class
                .getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            file = new File(new File(classDir).getParentFile(), filePath);
        }
        System.out.println("Reading: " + file.getAbsolutePath());

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        }

        List<String[]> rows = new ArrayList<>();
        for (int i = 7; i < lines.size(); i++) {
            String[] fields = parseCsvLine(lines.get(i));
            fields = Arrays.copyOf(fields, 9);
            for (int j = 0; j < fields.length; j++)
                if (fields[j] == null) fields[j] = "";
            boolean hasData = false;
            for (int idx : REQUIRED)
                if (!fields[idx].isBlank()) { hasData = true; break; }
            if (hasData) rows.add(fields);
        }
        return rows;
    }

    static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                fields.add(current.toString().strip());
                current = new StringBuilder();
            } else current.append(c);
        }
        fields.add(current.toString().strip());
        return fields.toArray(new String[0]);
    }

    static void mp19_summaryReport(List<String[]> rows) {
        System.out.println("============================================================");
        System.out.println("MP19 - DATASET SUMMARY REPORT");
        System.out.println("============================================================");

        Map<String, Long> typeCounts = rows.stream()
            .collect(Collectors.groupingBy(r -> r[1], Collectors.counting()));

        Map<String, Long> examCounts = rows.stream()
            .collect(Collectors.groupingBy(r -> r[3], Collectors.counting()));

        long passCount = rows.stream().filter(r -> r[7].equals("PASS")).count();
        long failCount = rows.stream().filter(r -> r[7].equals("FAIL")).count();
        double passRate = (double) passCount / rows.size() * 100;

        IntSummaryStatistics stats = rows.stream()
            .mapToInt(r -> {
                try { return Integer.parseInt(r[6]); }
                catch (NumberFormatException e) { return 0; }
            })
            .summaryStatistics();

        System.out.println("\n  Total Records : " + rows.size());
        System.out.println("  Unique Exams  : " + examCounts.size());

        System.out.println("\n  By Candidate Type:");
        typeCounts.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.printf("    %-10s : %d%n", e.getKey(), e.getValue()));

        System.out.println("\n  Results:");
        System.out.println("    PASS : " + passCount);
        System.out.println("    FAIL : " + failCount);
        System.out.printf("    Pass Rate : %.1f%%%n", passRate);

        System.out.println("\n  Score Statistics:");
        System.out.println("    Min : " + stats.getMin());
        System.out.println("    Max : " + stats.getMax());
        System.out.printf("    Avg : %.1f%n", stats.getAverage());

        System.out.println("\n  Top 5 Exams by Enrollment:");
        examCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .forEach(e -> System.out.printf("    %-50s : %d%n", e.getKey(), e.getValue()));

        System.out.println("\n  Exam Pass Rates (sorted by rate desc):");
        rows.stream()
            .collect(Collectors.groupingBy(r -> r[3]))
            .entrySet().stream()
            .map(e -> {
                long p = e.getValue().stream().filter(r -> r[7].equals("PASS")).count();
                double rate = (double) p / e.getValue().size() * 100;
                return new AbstractMap.SimpleEntry<>(e.getKey(), rate);
            })
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .forEach(e -> System.out.printf("    %-50s : %5.1f%%%n", e.getKey(), e.getValue()));

        System.out.println("\n============================================================");
        System.out.println("END OF REPORT");
        System.out.println("============================================================");
    }
}
