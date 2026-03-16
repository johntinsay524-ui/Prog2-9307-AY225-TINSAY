import java.io.*;
import java.util.*;

public class MP17_LongestEntry {

    static final String[] COLUMNS = {
        "Candidate", "Type", "Column1", "Exam", "Language",
        "Exam Date", "Score", "Result", "Time Used"
    };

    static final int[] REQUIRED = {0, 1, 3, 4, 5, 6, 7, 8};

    public static void main(String[] args) throws Exception {
        String filePath;

        if (args.length > 0) {
            filePath = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter dataset file path: ");
            filePath = scanner.nextLine().trim();
        }

        List<String[]> rows = loadCSV(filePath);
        System.out.println("Loaded " + rows.size() + " data rows.\n");
        mp17_longestTextEntry(rows);
    }

    static List<String[]> loadCSV(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            String classDir = MP17_LongestEntry.class
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

    static void mp17_longestTextEntry(List<String[]> rows) {
        System.out.println("============================================================");
        System.out.println("MP17 - LONGEST TEXT ENTRY PER COLUMN");
        System.out.println("============================================================");

        String overallLongest = "";
        String overallCol = "";

        for (int col = 0; col < COLUMNS.length; col++) {
            final int c = col;
            String longest = rows.stream()
                .map(r -> r[c])
                .max(Comparator.comparingInt(String::length))
                .orElse("");

            System.out.printf("  %-12s : '%s' (%d chars)%n",
                COLUMNS[col], longest, longest.length());

            if (longest.length() > overallLongest.length()) {
                overallLongest = longest;
                overallCol = COLUMNS[col];
            }
        }

        System.out.printf("%n  Overall longest: '%s' in [%s] at %d chars%n",
            overallLongest, overallCol, overallLongest.length());
    }
}