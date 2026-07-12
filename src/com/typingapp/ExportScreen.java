package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * GUI to handle exporting user results (primarily for premium users).
 */
public class ExportScreen extends JFrame {
    private Leaderboard<Result> leaderboard;
    private String username;

    public ExportScreen(Leaderboard<Result> leaderboard, String username) {
        this.leaderboard = leaderboard;
        this.username = username;

        setTitle("Export History for " + username);
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel infoLabel = new JLabel("Click to export your typing history to a text file.");
        JButton exportButton = new JButton("Export History (TXT)");

        add(infoLabel);
        add(exportButton);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportData();
            }
        });

        setVisible(true);
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Export File");
        fileChooser.setSelectedFile(new File(username + "_history.txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                List<Result> userHistory = leaderboard.getUserHistory(username);

                writer.append(String.format("%s's Typing History\n", username));
                writer.append("-------------------------------------------\n");
                writer.append(String.format("%-10s %-10s %-10s\n", "WPM", "Accuracy", "Mistakes"));
                writer.append("-------------------------------------------\n");

                if (userHistory.isEmpty()) {
                    writer.append("No typing history found.\n");
                } else {
                    // Sort history by WPM (or by date if available) for consistency
                    userHistory.sort((r1, r2) -> Double.compare(r2.getWpm(), r1.getWpm()));
                    for (Result r : userHistory) {
                        writer.append(String.format("%-10.2f %-10.2f %-10d\n", r.getWpm(), r.getAccuracy(), r.getMistakes()));
                    }
                }
                JOptionPane.showMessageDialog(this, "History exported successfully to:\n" + fileToSave.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting file: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}