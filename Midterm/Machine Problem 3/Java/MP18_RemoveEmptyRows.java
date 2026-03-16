import java.io.*;
import java.util.*;
import java.util.stream.*;

public class MP18_RemoveEmptyRows {

    static final int[] REQUIRED = {0, 1, 3, 4, 5, 6, 7, 8};

    public static void main(String[] args) throws Exception {
        String filePath = args.length > 0 ? args[0] : "C:\\Users\\Cleo\\Documents\\Midterm\\Prog2-9307-AY225-TINSAY\\Midterm\\Machine Problem 3\\Java\\MachineProblem3.csv";
        List<String[]> rows = loadCSV(filePath);
        System.out.println("Loaded " + rows.size() + " data rows.\n");
        mp18_removeEmptyRows(rows);
    }

    static List<String[]> loadCSV(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            String classDir = MP18_RemoveEmptyRows.class
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

    static List<String[]> mp18_removeEmptyRows(List<String[]> rows) {
        System.out.println("============================================================");
        System.out.println("MP18 - REMOVE ROWS WITH EMPTY FIELDS");
        System.out.println("============================================================");

        List<String[]> clean = rows.stream()
            .filter(r -> {
                for (int idx : REQUIRED)
                    if (r[idx].isBlank()) return false;
                return true;
            })
            .collect(Collectors.toList());

        int removed = rows.size() - clean.size();
        System.out.println("  Rows before : " + rows.size());
        System.out.println("  Rows removed: " + removed);
        System.out.println("  Rows after  : " + clean.size());

        if (removed > 0) {
            System.out.println("\n  Removed rows:");
            rows.stream()
                .filter(r -> {
                    for (int idx : REQUIRED)
                        if (r[idx].isBlank()) return true;
                    return false;
                })
                .forEach(r -> System.out.println("    -> " + r[0]));
        } else {
            System.out.println("  (All rows are complete - nothing removed.)");
        }
        return clean;
    }
}
