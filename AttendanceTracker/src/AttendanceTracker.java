import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.swing.*;

/**
 * AttendanceTracker - A Beautiful Java Swing application for tracking attendance
 * Features modern UI design with gradients, shadows, and smooth animations
 */
public class AttendanceTracker extends JFrame {
    // UI Components
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private JButton submitButton;
    private JButton clearButton;

    // Color Scheme - Modern blue gradient theme
    private final Color PRIMARY_COLOR = new Color(79, 70, 229); // Indigo
    private final Color SECONDARY_COLOR = new Color(99, 102, 241); // Light Indigo
    private final Color ACCENT_COLOR = new Color(234, 88, 12); // Orange
    private final Color SUCCESS_COLOR = new Color(34, 197, 94); // Green
    private final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Light gray
    private final Color TEXT_COLOR = new Color(51, 65, 85); // Dark gray
    private final Color FIELD_BG = Color.WHITE;

    /**
     * Constructor - Initializes the beautiful attendance tracker window
     */
    public AttendanceTracker() {
        // Set up the main frame
        setTitle("Attendance Tracker Pro");
        setSize(700, 650);
        setMinimumSize(new Dimension(700, 650)); // Prevent resizing too small
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen

        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize components
        initializeComponents();

        // Make the frame visible
        setVisible(true);
    }

    /**
     * Initialize all UI components with beautiful modern design
     */
    private void initializeComponents() {
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create header panel with gradient effect
        JPanel headerPanel = createHeaderPanel();

        // Create form panel for input fields
        JPanel formPanel = createFormPanel();

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        // ========================================
        // FOOTER: Create footer panel with branding
        // ========================================
        JPanel footerPanel = createFooterPanel();

        // Add all panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create container with footer
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_COLOR);
        container.add(mainPanel, BorderLayout.CENTER);
        container.add(footerPanel, BorderLayout.SOUTH);

        // Add container to frame
        add(container);
    }

    /**
     * Create beautiful header panel with icon and title
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Title with icon
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        titleContainer.setBackground(BACKGROUND_COLOR);

        // Create a custom icon panel with colored circle and checkmark
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillOval(5, 5, 50, 50);

                // Draw checkmark
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(18, 30, 26, 38);
                g2d.drawLine(26, 38, 42, 18);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(60, 60);
            }
        };
        iconPanel.setBackground(BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Attendance Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(TEXT_COLOR);

        titleContainer.add(iconPanel);
        titleContainer.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Track your attendance with ease");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleContainer, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    /**
     * Create beautiful form panel with styled input fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Attendance Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formPanel.add(createFieldLabel("👤 Full Name", "Enter your complete name"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameField = createStyledTextField("e.g., Juan Dela Cruz");
        formPanel.add(nameField, gbc);

        // Row 1: Course/Year
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(createFieldLabel("📚 Course/Year", "Your current course and year"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        courseField = createStyledTextField("e.g., BSCS 3rd Year");
        formPanel.add(courseField, gbc);

        // Row 2: Time In
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(createFieldLabel("🕐 Time In", "Auto-generated timestamp"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        timeInField = createStyledTextField("Click submit to generate");
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(241, 245, 249));
        timeInField.setForeground(new Color(100, 116, 139));
        formPanel.add(timeInField, gbc);

        // Row 3: E-Signature
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(createFieldLabel("🔐 E-Signature", "Unique digital signature"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        eSignatureField = createStyledTextField("Auto-generated");
        eSignatureField.setEditable(false);
        eSignatureField.setBackground(new Color(241, 245, 249));
        eSignatureField.setForeground(new Color(100, 116, 139));
        eSignatureField.setFont(new Font("Consolas", Font.PLAIN, 13));
        formPanel.add(eSignatureField, gbc);

        return formPanel;
    }

    /**
     * Create a styled label with icon and tooltip
     */
    private JPanel createFieldLabel(String text, String tooltip) {
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(TEXT_COLOR);
        label.setToolTipText(tooltip);

        labelPanel.add(label, BorderLayout.WEST);
        return labelPanel;
    }

    /**
     * Create a beautifully styled text field
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(TEXT_COLOR);
        field.setBackground(FIELD_BG);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));

        // Add focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(12, 18, 12, 18)
                ));
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                    BorderFactory.createEmptyBorder(12, 18, 12, 18)
                ));
            }
        });

        return field;
    }

    /**
     * Create button panel with beautiful styled buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Submit button with gradient effect
        submitButton = createStyledButton("✓ Submit Attendance", PRIMARY_COLOR, SECONDARY_COLOR);
        submitButton.addActionListener(e -> submitAttendance());

        // Clear button
        clearButton = createStyledButton("↻ Clear Form", new Color(239, 68, 68), new Color(220, 38, 38));
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        return buttonPanel;
    }

    /**
     * Create a modern styled button
     */
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(220, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // ========================================
    // FOOTER: Display copyright and branding information at the bottom of the application
    // ========================================
    /**
     * Create footer panel with copyright information and branding
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(241, 245, 249));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel footerLabel = new JLabel("© 2026 Created By: John Cleo Tinsay");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        footerLabel.setForeground(new Color(100, 116, 139));

        footerPanel.add(footerLabel);
        return footerPanel;
    }

    /**
     * Submit attendance - generates time stamp and e-signature with beautiful feedback
     */
    private void submitAttendance() {
        // Validate input fields
        if (nameField.getText().trim().isEmpty()) {
            showStyledMessage(
                "Please enter your name!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            nameField.requestFocus();
            return;
        }

        if (courseField.getText().trim().isEmpty()) {
            showStyledMessage(
                "Please enter your course/year!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            courseField.requestFocus();
            return;
        }

        // Generate current date and time with beautiful format
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy • hh:mm:ss a");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);

        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString().toUpperCase();
        eSignatureField.setText(eSignature);

        // Show beautiful success message
        String message = String.format(
            "<html>" +
            "<div style='padding: 15px; font-family: Segoe UI;'>" +
            "<div style='font-size: 18px; font-weight: bold; color: #22c55e; margin-bottom: 10px;'>✓ Success!</div>" +
            "<div style='font-size: 14px; color: #64748b; margin-bottom: 15px;'>Attendance recorded successfully</div>" +
            "<hr style='border: 1px solid #e2e8f0;'>" +
            "<div style='margin-top: 15px; font-size: 13px;'>" +
            "<div style='margin: 5px 0;'><b>Name:</b> %s</div>" +
            "<div style='margin: 5px 0;'><b>Course:</b> %s</div>" +
            "<div style='margin: 5px 0;'><b>Time:</b> %s</div>" +
            "</div>" +
            "</html>",
            nameField.getText(),
            courseField.getText(),
            timeIn
        );

        JOptionPane.showMessageDialog(
            this,
            message,
            "Attendance Recorded",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Show styled message dialog
     */
    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * Clear all form fields with smooth reset
     */
    private void clearForm() {
        nameField.setText("");
        courseField.setText("");
        timeInField.setText("Click submit to generate");
        eSignatureField.setText("Auto-generated");
        nameField.requestFocus();

        // Show confirmation
        JOptionPane.showMessageDialog(
            this,
            "Form cleared successfully!",
            "Form Reset",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new AttendanceTracker();
        });
    }
}