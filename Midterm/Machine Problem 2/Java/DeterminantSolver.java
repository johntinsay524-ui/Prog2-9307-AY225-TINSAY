// Student  : TINSAY, JOHN CLEO T.
// Course   : Math 101 – Linear Algebra, UPHSD Molino Campus
// Assignment: Assignment 01 – 3x3 Matrix Determinant Solver
// Date     : 2025
// Description: Computes the determinant of the assigned 3x3 matrix
//              using cofactor expansion along the first row, displayed in a JFrame GUI.

import javax.swing.*;
import java.awt.*;

public class DeterminantSolver extends JFrame {

    static int computeMinor(int a, int b, int c, int d) {
        return (a * d) - (b * c);
    }

    static int solveDeterminant(int[][] M) {
        int minor11 = computeMinor(M[1][1], M[1][2], M[2][1], M[2][2]);
        int minor12 = computeMinor(M[1][0], M[1][2], M[2][0], M[2][2]);
        int minor13 = computeMinor(M[1][0], M[1][1], M[2][0], M[2][1]);
        return M[0][0] * minor11 - M[0][1] * minor12 + M[0][2] * minor13;
    }

    static String getSteps(int[][] M) {
        int minor11 = computeMinor(M[1][1], M[1][2], M[2][1], M[2][2]);
        int minor12 = computeMinor(M[1][0], M[1][2], M[2][0], M[2][2]);
        int minor13 = computeMinor(M[1][0], M[1][1], M[2][0], M[2][1]);

        int c11 =  M[0][0] * minor11;
        int c12 = -M[0][1] * minor12;
        int c13 =  M[0][2] * minor13;
        int det = c11 + c12 + c13;

        return "===================================================\n"
             + "  3x3 MATRIX DETERMINANT SOLVER\n"
             + "  Student: TINSAY, JOHN CLEO T.\n"
             + "===================================================\n"
             + "  |  " + M[0][0] + "   " + M[0][1] + "   " + M[0][2] + "  |\n"
             + "  |  " + M[1][0] + "   " + M[1][1] + "   " + M[1][2] + "  |\n"
             + "  |  " + M[2][0] + "   " + M[2][1] + "   " + M[2][2] + "  |\n"
             + "===================================================\n\n"
             + "Expanding along Row 1:\n\n"
             + "  Step 1 - Minor M11: (" + M[1][1] + "x" + M[2][2] + ") - (" + M[1][2] + "x" + M[2][1] + ") = " + minor11 + "\n"
             + "  Step 2 - Minor M12: (" + M[1][0] + "x" + M[2][2] + ") - (" + M[1][2] + "x" + M[2][0] + ") = " + minor12 + "\n"
             + "  Step 3 - Minor M13: (" + M[1][0] + "x" + M[2][1] + ") - (" + M[1][1] + "x" + M[2][0] + ") = " + minor13 + "\n\n"
             + "  C11 = (+1) x " + M[0][0] + " x " + minor11 + " = " + c11 + "\n"
             + "  C12 = (-1) x " + M[0][1] + " x " + minor12 + " = " + c12 + "\n"
             + "  C13 = (+1) x " + M[0][2] + " x " + minor13 + " = " + c13 + "\n\n"
             + "  det(M) = " + c11 + " + (" + c12 + ") + " + c13 + "\n\n"
             + "===================================================\n"
             + "  DETERMINANT = " + det + "\n"
             + "===================================================" + "\n"
             + (det == 0 ? "  The matrix is SINGULAR - it has no inverse.\n" : "");
    }

    public DeterminantSolver() {
        int[][] matrix = {
            {2, 1, 6},
            {4, 3, 5},
            {6, 2, 4}
        };

        // Window setup
        setTitle("Determinant Solver - Tinsay, John Cleo T.");
        setSize(480, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Text area to display all steps
        JTextArea textArea = new JTextArea(getSteps(matrix));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(30, 30, 30));
        textArea.setForeground(new Color(0, 230, 120));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        add(new JScrollPane(textArea), BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DeterminantSolver();
    }
}
