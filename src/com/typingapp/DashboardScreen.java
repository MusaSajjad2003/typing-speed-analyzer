package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardScreen extends JFrame {
    private User currentUser; // Store the logged-in user
    private Leaderboard<Result> leaderboard; // Member 3's leaderboard instance

    // Constructor should now accept the logged-in User and a Leaderboard instance
    public DashboardScreen(User user, Leaderboard<Result> leaderboard) { // Corrected constructor
        this.currentUser = user;
        this.leaderboard = leaderboard; // Initialize Member 3's core logic here

        setTitle("Dashboard - " + user.getUsername());
        setSize(400, 250); // Increased size to accommodate more buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10)); // Grid layout for buttons

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername(), SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton startTest = new JButton("Start Typing Test"); // Member 2
        JButton viewLeaderboard = new JButton("View Global Leaderboard"); // Member 3
        JButton viewHistory = new JButton("View My History"); // Member 3
        JButton exportHistory = new JButton("Export My History"); // Member 3 (Premium Only)

        panel.add(startTest);
        panel.add(viewLeaderboard);
        panel.add(viewHistory);

        // Only add export button if the user is premium
        if (currentUser.isPremium()) {
            panel.add(exportHistory);
        } else {
            // Optionally, you can add a disabled button or a message for free users
            JLabel premiumFeatureLabel = new JLabel("Unlock Export with Premium!", SwingConstants.CENTER);
            premiumFeatureLabel.setForeground(Color.GRAY);
            panel.add(premiumFeatureLabel);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);

        // Event listeners to navigate to next modules
        startTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch Member 2's TypingTest
                // Close the DashboardScreen first, or make it invisible
                DashboardScreen.this.dispose(); // Close dashboard
                new TypingTest(currentUser, leaderboard); // Pass current user and leaderboard
            }
        });

        viewLeaderboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LeaderboardScreen(leaderboard); // Launch Member 3's LeaderboardScreen
            }
        });

        viewHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HistoryScreen(leaderboard, currentUser.getUsername()); // Launch Member 3's HistoryScreen
            }
        });

        if (currentUser.isPremium()) {
            exportHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ExportScreen(leaderboard, currentUser.getUsername()); // Launch Member 3's ExportScreen
                }
            });
        }
    }
}