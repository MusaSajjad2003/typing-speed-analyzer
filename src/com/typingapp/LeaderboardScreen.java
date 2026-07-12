package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * GUI to display top performers.
 */
public class LeaderboardScreen extends JFrame {
    private JTextArea leaderboardArea;
    private Leaderboard<Result> leaderboard;
    private static final int TOP_N = 10; // Display top 10 results

    public LeaderboardScreen(Leaderboard<Result> leaderboard) {
        this.leaderboard = leaderboard;
        setTitle("Top Performers Leaderboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window, not the whole app
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());

        leaderboardArea = new JTextArea();
        leaderboardArea.setEditable(false); // Make it read-only
        leaderboardArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Monospaced for better alignment

        JScrollPane scrollPane = new JScrollPane(leaderboardArea);
        add(scrollPane, BorderLayout.CENTER);

        updateLeaderboardDisplay();

        setVisible(true);
    }

    private void updateLeaderboardDisplay() {
        List<Result> topResults = leaderboard.getTopResults(TOP_N);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-15s %-10s %-10s %-10s%n", "Rank", "Username", "WPM", "Accuracy", "Mistakes"));
        sb.append("-----------------------------------------------------------\n");

        if (topResults.isEmpty()) {
            sb.append("\nNo results yet. Start typing to get on the leaderboard!\n");
        } else {
            for (int i = 0; i < topResults.size(); i++) {
                Result r = topResults.get(i);
                sb.append(String.format("%-5d %-15s %-10.2f %-10.2f %-10d%n",
                        (i + 1), r.getUsername(), r.getWpm(), r.getAccuracy(), r.getMistakes()));
            }
        }
        leaderboardArea.setText(sb.toString());
    }
}