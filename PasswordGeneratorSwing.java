package com.sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.SecureRandom;

public class PasswordGeneratorSwing {

    // Character sets for password generation
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>?";

    public static void main(String[] args) {
        // Ensure the GUI runs on the Swing event-dispatch thread
        SwingUtilities.invokeLater(PasswordGeneratorSwing::createAndShowGUI);
    }

    // Create and display the main UI
    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Password Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 280);
        frame.setLocationRelativeTo(null); // Center the window

        // Create the main panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Text field to input password length
        JTextField lengthField = new JTextField(10);
        lengthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lengthField.setToolTipText("Enter password length");

        // Checkboxes for character type preferences
        JCheckBox upperBox = new JCheckBox("Include Uppercase");
        JCheckBox lowerBox = new JCheckBox("Include Lowercase");
        JCheckBox numberBox = new JCheckBox("Include Numbers");
        JCheckBox specialBox = new JCheckBox("Include Special Characters");

        // Button to trigger password generation
        JButton generateButton = new JButton("Generate Password");

        // Add UI components to the panel
        panel.add(new JLabel("Password Length:"));
        panel.add(lengthField);
        panel.add(upperBox);
        panel.add(lowerBox);
        panel.add(numberBox);
        panel.add(specialBox);
        panel.add(generateButton);

        // Action listener for the "Generate Password" button
        generateButton.addActionListener((ActionEvent e) -> {
            try {
                // Parse the password length from user input
                int length = Integer.parseInt(lengthField.getText());

                // Retrieve user selections
                boolean useUpper = upperBox.isSelected();
                boolean useLower = lowerBox.isSelected();
                boolean useNumbers = numberBox.isSelected();
                boolean useSpecial = specialBox.isSelected();

                // Generate password using selected criteria
                String password = generatePassword(length, useUpper, useLower, useNumbers, useSpecial);

                // Display password in a separate popup dialog
                showPasswordDialog(frame, password);

            } catch (NumberFormatException ex) {
                // Handle non-integer input
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for password length.");
            } catch (IllegalArgumentException ex) {
                // Handle invalid options (e.g., no checkbox selected)
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Set up the frame and make it visible
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    // Method to generate password based on user criteria
    private static String generatePassword(int length, boolean useUpper, boolean useLower, boolean useNumbers, boolean useSpecial) {
        StringBuilder charPool = new StringBuilder();

        // Build character pool from selected types
        if (useUpper) charPool.append(UPPERCASE);
        if (useLower) charPool.append(LOWERCASE);
        if (useNumbers) charPool.append(NUMBERS);
        if (useSpecial) charPool.append(SPECIAL_CHARS);

        // Validate user selections
        if (charPool.length() == 0 || length <= 0) {
            throw new IllegalArgumentException("Select at least one character type and set a valid length.");
        }

        // Secure random generator for stronger randomness
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Randomly build password from character pool
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        return password.toString();
    }

    // Display the generated password in a separate dialog
    private static void showPasswordDialog(JFrame parent, String password) {
        // Create a modal dialog
        JDialog dialog = new JDialog(parent, "Generated Password", true);
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(parent); // Center relative to parent

        // Set up dialog content layout
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout(10, 10));

        // Label and text field to display the password
        JLabel passwordLabel = new JLabel("Your password:");
        JTextField passwordField = new JTextField(password);
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 14));
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        // Close button to dismiss the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        // Add components to the dialog panel
        dialogPanel.add(passwordLabel, BorderLayout.NORTH);
        dialogPanel.add(passwordField, BorderLayout.CENTER);
        dialogPanel.add(closeButton, BorderLayout.SOUTH);

        // Display the dialog
        dialog.setContentPane(dialogPanel);
        dialog.setVisible(true);
    }
}

