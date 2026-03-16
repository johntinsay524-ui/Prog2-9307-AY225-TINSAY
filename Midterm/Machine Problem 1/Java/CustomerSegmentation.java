/**
 * PROGRAMMING 2 – MACHINE PROBLEM
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Computer Science – Data Science
 *
 * CustomerSegmentation.java
 * Reads the VGChartz 2024 CSV dataset and segments video games
 * by total sales (in millions) into Platinum, Gold, Silver, Bronze tiers.
 *
 * CSV columns (0-indexed):
 *   0  img            - box art URL (skipped)
 *   1  title          - game title
 *   2  console        - platform
 *   3  genre          - game genre
 *   4  publisher      - publisher name
 *   5  developer      - developer name (skipped)
 *   6  critic_score   - score (skipped)
 *   7  total_sales    - total global sales in millions  ← used for segmentation
 *   8  na_sales
 *   9  jp_sales
 *   10 pal_sales
 *   11 other_sales
 *   12 release_date
 *   13 last_update
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerSegmentation {

    // ------------------------------------------------------------------ //
    //  ENTRY POINT
    // ------------------------------------------------------------------ //
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        printBanner();

        // Step 1: Auto-detect CSV
        File file = autoDetectCSV(input);

        // Step 2: Load dataset
        List<CustomerRecord> records = loadDataset(file);

        if (records.isEmpty()) {
            System.out.println("\n[!] No valid records found in the file. Exiting.");
            input.close();
            return;
        }

        // Step 3: Segment and display results
        displayResults(records);

        input.close();
    }

    // ------------------------------------------------------------------ //
    //  AUTO-DETECT CSV IN CURRENT DIRECTORY
    // ------------------------------------------------------------------ //
    private static File autoDetectCSV(Scanner input) {

        File currentDir = new File(System.getProperty("user.dir"));
        File[] csvFiles = currentDir.listFiles(
            (dir, name) -> name.toLowerCase().endsWith(".csv")
        );

        // No CSV found → fallback to manual input
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("[!] No CSV files found in: " + currentDir.getAbsolutePath());
            System.out.println("    Please enter the file path manually.\n");
            return getValidFileManually(input);
        }

        // Exactly one CSV found → use automatically
        if (csvFiles.length == 1) {
            System.out.println("[OK] CSV file automatically detected:");
            System.out.println("     " + csvFiles[0].getAbsolutePath());
            System.out.println();
            return csvFiles[0];
        }

        // Multiple CSVs → let user pick
        System.out.println("[!] Multiple CSV files found in: " + currentDir.getAbsolutePath());
        System.out.println();
        for (int i = 0; i < csvFiles.length; i++) {
            System.out.printf("  [%d] %s%n", i + 1, csvFiles[i].getName());
        }

        while (true) {
            System.out.printf("%nSelect a file (1 - %d): ", csvFiles.length);
            String choice = input.nextLine().trim();
            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < csvFiles.length) {
                    System.out.println("[OK] Selected: " + csvFiles[index].getName());
                    System.out.println();
                    return csvFiles[index];
                } else {
                    System.out.println("[Error] Number out of range. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid input. Enter a number.");
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  MANUAL FILE PATH INPUT (fallback)
    // ------------------------------------------------------------------ //
    private static File getValidFileManually(Scanner input) {
        File file;
        while (true) {
            System.out.print("Enter dataset file path: ");
            String path = input.nextLine().trim();
            file = new File(path);

            if (!file.exists()) {
                System.out.println("[Error] File does not exist. Please try again.");
            } else if (!file.isFile()) {
                System.out.println("[Error] Path is not a file. Please try again.");
            } else if (!file.canRead()) {
                System.out.println("[Error] File is not readable. Please try again.");
            } else if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("[Error] File must be a CSV (.csv) file. Please try again.");
            } else {
                System.out.println("[OK] File found: " + file.getAbsolutePath());
                System.out.println();
                break;
            }
        }
        return file;
    }

    // ------------------------------------------------------------------ //
    //  LOAD CSV DATASET  (VGChartz format)
    // ------------------------------------------------------------------ //
    private static List<CustomerRecord> loadDataset(File file) {
        List<CustomerRecord> records = new ArrayList<>();
        int lineNumber = 0;
        int skipped    = 0;

        System.out.println("Loading dataset: " + file.getName() + " ...");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header row
                if (lineNumber == 1) continue;

                // Skip blank lines
                if (line.trim().isEmpty()) continue;

                // Split by comma — handles up to 14 columns
                String[] col = line.split(",", -1);

                // Need at least 8 columns (index 0–7)
                if (col.length < 8) {
                    skipped++;
                    continue;
                }

                // Skip rows with missing total_sales
                String salesStr = col[7].trim();
                if (salesStr.isEmpty()) {
                    skipped++;
                    continue;
                }

                try {
                    String title     = col[1].trim();
                    String console   = col[2].trim();
                    String genre     = col[3].trim();
                    String publisher = col[4].trim();
                    double sales     = Double.parseDouble(salesStr);

                    records.add(new CustomerRecord(title, console, genre, publisher, sales));

                } catch (NumberFormatException e) {
                    skipped++;
                }
            }

        } catch (IOException e) {
            System.out.println("[Error] Could not read file: " + e.getMessage());
        }

        System.out.printf("Records loaded: %,d  |  Rows skipped: %,d%n%n",
                records.size(), skipped);
        return records;
    }

    // ------------------------------------------------------------------ //
    //  DISPLAY SEGMENTATION RESULTS
    // ------------------------------------------------------------------ //
    private static void displayResults(List<CustomerRecord> records) {

        List<CustomerRecord> platinum = new ArrayList<>();
        List<CustomerRecord> gold     = new ArrayList<>();
        List<CustomerRecord> silver   = new ArrayList<>();
        List<CustomerRecord> bronze   = new ArrayList<>();

        double totalSalesAll = 0;

        for (CustomerRecord r : records) {
            totalSalesAll += r.getTotalSales();
            switch (r.getSegment()) {
                case "Platinum": platinum.add(r); break;
                case "Gold":     gold.add(r);     break;
                case "Silver":   silver.add(r);   break;
                default:         bronze.add(r);   break;
            }
        }

        System.out.println("=".repeat(82));
        System.out.println("              VIDEO GAME SALES SEGMENTATION REPORT");
        System.out.println("              Revenue-Based Classification (VGChartz 2024)");
        System.out.println("=".repeat(82));

        // Summary table
        System.out.println("\n[ SEGMENT SUMMARY ]");
        System.out.println("-".repeat(55));
        System.out.printf("  %-10s | %-6s | %-12s | %s%n",
                "Segment", "Tier", "# of Titles", "Criteria (Total Sales)");
        System.out.println("  " + "-".repeat(52));
        System.out.printf("  %-10s | %-6s | %,-12d | > 10 million%n",
                "Platinum", "High", platinum.size());
        System.out.printf("  %-10s | %-6s | %,-12d | 5M – 10M%n",
                "Gold",     "Good", gold.size());
        System.out.printf("  %-10s | %-6s | %,-12d | 1M – 4.99M%n",
                "Silver",   "Mid",  silver.size());
        System.out.printf("  %-10s | %-6s | %,-12d | < 1 million%n",
                "Bronze",   "Low",  bronze.size());
        System.out.println("-".repeat(55));
        System.out.printf("  %-10s | %-6s | %,-12d | Total Sales: %,.2f M%n",
                "TOTAL", "", records.size(), totalSalesAll);

        // Detailed lists
        printSegmentList("PLATINUM", "> 10 million",  platinum);
        printSegmentList("GOLD",     "5M – 10M",      gold);
        printSegmentList("SILVER",   "1M – 4.99M",    silver);
        printSegmentList("BRONZE",   "< 1 million",   bronze);

        System.out.println("=".repeat(82));
        System.out.println("                        END OF REPORT");
        System.out.println("=".repeat(82));
    }

    // ------------------------------------------------------------------ //
    //  HELPER: Print one segment block (shows top 20 to keep output clean)
    // ------------------------------------------------------------------ //
    private static void printSegmentList(String label,
                                         String range,
                                         List<CustomerRecord> list) {
        System.out.println();
        System.out.printf("[ %s ]  Total Sales: %s  |  Count: %,d%n", label, range, list.size());
        System.out.println("-".repeat(82));

        if (list.isEmpty()) {
            System.out.println("  (No games in this segment)");
            return;
        }

        System.out.printf("  %-40s | %-6s | %-20s | %10s%n",
                "Title", "Cons.", "Publisher", "Sales (M)");
        System.out.println("  " + "-".repeat(78));

        // Show top 20 by sales to keep console output readable
        int limit = Math.min(list.size(), 20);
        // Sort descending by sales inline
        list.sort((a, b) -> Double.compare(b.getTotalSales(), a.getTotalSales()));

        for (int i = 0; i < limit; i++) {
            System.out.println(list.get(i));
        }

        if (list.size() > 20) {
            System.out.printf("%n  ... and %,d more titles.%n", list.size() - 20);
        }

        System.out.printf("%n  Total in segment: %,d title(s)%n", list.size());
    }

    // ------------------------------------------------------------------ //
    //  BANNER
    // ------------------------------------------------------------------ //
    private static void printBanner() {
        System.out.println("=".repeat(82));
        System.out.println("  PROGRAMMING 2 - Video Game Sales Segmentation");
        System.out.println("  UPHSD Molino | BS Computer Science - Data Science");
        System.out.println("=".repeat(82));
        System.out.println();
    }
}
