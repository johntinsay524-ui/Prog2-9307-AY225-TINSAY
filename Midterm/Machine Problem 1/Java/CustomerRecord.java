/**
 * PROGRAMMING 2 – MACHINE PROBLEM
 * University of Perpetual Help System DALTA – Molino Campus
 * BS Computer Science – Data Science
 *
 * CustomerRecord.java
 * Data class representing a single video game entry from the VGChartz CSV dataset.
 */

public class CustomerRecord {

    private String title;
    private String console;
    private String genre;
    private String publisher;
    private double totalSales;   // in millions
    private String segment;

    // Constructor
    public CustomerRecord(String title, String console, String genre,
                          String publisher, double totalSales) {
        this.title      = title.trim();
        this.console    = console.trim();
        this.genre      = genre.trim();
        this.publisher  = publisher.trim();
        this.totalSales = totalSales;
        this.segment    = classifySegment(totalSales);
    }

    // Segment classification based on total sales (millions)
    // Mapped to match the original tier thresholds scaled to millions:
    //   Platinum  > 10M
    //   Gold      5M – 10M
    //   Silver    1M – 4.99M
    //   Bronze    < 1M
    private String classifySegment(double sales) {
        if (sales > 10.0) {
            return "Platinum";
        } else if (sales >= 5.0) {
            return "Gold";
        } else if (sales >= 1.0) {
            return "Silver";
        } else {
            return "Bronze";
        }
    }

    // Getters
    public String getTitle()      { return title; }
    public String getConsole()    { return console; }
    public String getGenre()      { return genre; }
    public String getPublisher()  { return publisher; }
    public double getTotalSales() { return totalSales; }
    public String getSegment()    { return segment; }

    @Override
    public String toString() {
        return String.format("  %-40s | %-6s | %-20s | %8.2f M",
                truncate(title, 40), console, truncate(publisher, 20), totalSales);
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
