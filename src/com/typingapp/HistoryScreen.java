package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GUI to display a specific user's typing history.
 */
public class HistoryScreen extends JFrame {
    private JTextArea historyArea;
    private Leaderboard<Result> leaderboard;
    private String username;

    public HistoryScreen(Leaderboard<Result> leaderboard, String username) {
        this.leaderboard = leaderboard;
        this.username = username;
        setTitle("Typing History for: " + username);
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        updateHistoryDisplay();

        setVisible(true);
    }

    private void updateHistoryDisplay() {
        List<Result> userHistory = leaderboard.getUserHistory(username);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-10s %-10s%n", "WPM", "Accuracy", "Mistakes"));
        sb.append("-------------------------------------------\n");

        if (userHistory.isEmpty()) {
            sb.append("\nNo typing history found for " + username + ".\n");
        } else {
            // Sort history by WPM (or by date if you add a timestamp to Result)
            userHistory.sort((r1, r2) -> Double.compare(r2.getWpm(), r1.getWpm())); // Sort by WPM for display

            for (Result r : userHistory) {
                sb.append(String.format("%-10.2f %-10.2f %-10d%n", r.getWpm(), r.getAccuracy(), r.getMistakes()));
            }
        }
        historyArea.setText(sb.toString());
    }
}