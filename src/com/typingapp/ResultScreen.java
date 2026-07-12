package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI to display the results of a single typing test.
 */
public class ResultScreen extends JFrame {

    public ResultScreen(double wpm, double accuracy, int mistakes, User user, Leaderboard<Result> leaderboard) {
        setTitle("Your Typing Test Results");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Test Complete!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);

        panel.add(new JLabel("Words Per Minute (WPM): " + String.format("%.2f", wpm), SwingConstants.CENTER));
        panel.add(new JLabel("Accuracy: " + String.format("%.2f", accuracy) + "%", SwingConstants.CENTER));
        panel.add(new JLabel("Mistakes: " + mistakes, SwingConstants.CENTER));

        JButton backToDashboardButton = new JButton("Back to Dashboard");
        panel.add(backToDashboardButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);

        // Action listener for "Back to Dashboard" button
        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this result screen
                // Re-open DashboardScreen, passing current user and leaderboard
                // This assumes DashboardScreen should always be accessible.
                // For simplicity, we'll create a new one.
                new DashboardScreen(user, leaderboard);
            }
        });

        // Store the result
        leaderboard.addResult(new Result(user.getUsername(), wpm, accuracy, mistakes));
        JOptionPane.showMessageDialog(this, "Your result has been saved to the leaderboard!", "Result Saved", JOptionPane.INFORMATION_MESSAGE);
    }
}