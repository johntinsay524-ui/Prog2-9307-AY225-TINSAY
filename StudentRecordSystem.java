import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentRecordSystem extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField txtID, txtFirstName, txtLastName, txtLab1, txtLab2, txtLab3, txtPrelim, txtAttendance;
    final String FILE_NAME = "class_records.csv";

    public StudentRecordSystem() {
        setTitle("Student Record System");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(
                new String[]{"StudentID", "First Name", "Last Name", "LAB WORK 1", "LAB WORK 2", "LAB WORK 3", "PRELIM EXAM", "ATTENDANCE GRADE"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input panel
        JPanel panel = new JPanel(new GridLayout(2, 9));

        txtID = new JTextField();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtLab1 = new JTextField();
        txtLab2 = new JTextField();
        txtLab3 = new JTextField();
        txtPrelim = new JTextField();
        txtAttendance = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");

        // Labels
        panel.add(new JLabel("StudentID"));
        panel.add(new JLabel("First Name"));
        panel.add(new JLabel("Last Name"));
        panel.add(new JLabel("LAB WORK 1"));
        panel.add(new JLabel("LAB WORK 2"));
        panel.add(new JLabel("LAB WORK 3"));
        panel.add(new JLabel("PRELIM EXAM"));
        panel.add(new JLabel("ATTENDANCE GRADE"));
        panel.add(new JLabel("")); // Empty cell for layout

        // Text fields and add button
        panel.add(txtID);
        panel.add(txtFirstName);
        panel.add(txtLastName);
        panel.add(txtLab1);
        panel.add(txtLab2);
        panel.add(txtLab3);
        panel.add(txtPrelim);
        panel.add(txtAttendance);
        panel.add(btnAdd);

        add(panel, BorderLayout.SOUTH);
        add(btnDelete, BorderLayout.NORTH);

        loadCSV();

        // ADD → update CSV
        btnAdd.addActionListener(e -> {
            model.addRow(new Object[]{
                    txtID.getText(),
                    txtFirstName.getText(),
                    txtLastName.getText(),
                    txtLab1.getText(),
                    txtLab2.getText(),
                    txtLab3.getText(),
                    txtPrelim.getText(),
                    txtAttendance.getText()
            });
            saveCSV();
            clearFields();
        });

        // DELETE → update CSV
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
                saveCSV();
            }
        });
    }

    // Load CSV
    private void loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading CSV file.");
        }
    }

    // SAVE CSV (rewrite file)
    private void saveCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {

            // Header
            pw.println("StudentID,First Name,Last Name,LAB WORK 1,LAB WORK 2,LAB WORK 3,PRELIM EXAM,ATTENDANCE GRADE");

            // Data
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(
                        model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "," +
                        model.getValueAt(i, 3) + "," +
                        model.getValueAt(i, 4) + "," +
                        model.getValueAt(i, 5) + "," +
                        model.getValueAt(i, 6) + "," +
                        model.getValueAt(i, 7)
                );
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving CSV file.");
        }
    }

    private void clearFields() {
        txtID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLab1.setText("");
        txtLab2.setText("");
        txtLab3.setText("");
        txtPrelim.setText("");
        txtAttendance.setText("");
    }

    public static void main(String[] args) {
        new StudentRecordSystem().setVisible(true);
    }
}
