package com.typingapp;

import java.util.Scanner; // Still needed for console input for premium user difficulty selection
import java.util.InputMismatchException;
import javax.swing.JOptionPane; // For JOptionPane

class TypingTest implements TypingTestGUI.TypingTestListener {
    private User user; // Now properly used
    private TextGenerator textGenerator;
    private FeedbackAnalyzer feedbackAnalyzer; // Now properly used
    private double totalTime;
    private String originalText;
    private String typedText;
    private Leaderboard<Result> leaderboard; // Added dependency for storing results

    public TypingTest(User user, Leaderboard<Result> leaderboard) {
        this.user = user;
        this.leaderboard = leaderboard;
        this.textGenerator = new TextGenerator();
        this.feedbackAnalyzer = new FeedbackAnalyzer();
        selectLevelAndGetText(); // Call this to initiate the test
    }

    private void selectLevelAndGetText() {
        int level = 1; // Default to easy for free users, or if selection fails

        if (user instanceof FreeUser) {
            level = 1; // Free users get easy text by default
            JOptionPane.showMessageDialog(null, "Free user: Starting with easy difficulty.", "Difficulty", JOptionPane.INFORMATION_MESSAGE);

        } else if (user instanceof PremiumUser) {
            String inputStr = JOptionPane.showInputDialog(null,
                    "Premium user: Enter the Difficulty level (1:Easy, 2:Medium, 3:Hard):",
                    "Select Difficulty", JOptionPane.QUESTION_MESSAGE);
            try {
                if (inputStr != null && !inputStr.trim().isEmpty()) {
                    int chosenLevel = Integer.parseInt(inputStr.trim());
                    if (chosenLevel >= 1 && chosenLevel <= 3) {
                        level = chosenLevel;
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid input. Defaulting to Easy difficulty (1).", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No input. Defaulting to Easy difficulty (1).", "No Input", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number (1-3). Defaulting to Easy difficulty (1).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
        textGenerator.setDifficulty(level); // Set the difficulty level in TextGenerator
        getText(); // Get the text based on the set difficulty
    }

    public void getText() {
        originalText = textGenerator.getRandomText();
        if (originalText.equals("No text found.")) {
            JOptionPane.showMessageDialog(null, "No text found for the selected difficulty. Please check text files (easy.txt, medium.txt, hard.txt).", "Error", JOptionPane.ERROR_MESSAGE);
            // Handle gracefully, maybe return to dashboard
            new DashboardScreen(user, leaderboard); // Return to dashboard
            return;
        }
        System.out.println("Original Text: " + originalText); // For debugging
        startTypingTest();
    }

    private void startTypingTest() {
        TypingTestGUI gui = new TypingTestGUI();
        gui.showGUI(originalText, this); // 'this' refers to TypingTest instance, which implements TypingTestListener
    }

    @Override // Implementing the interface method from TypingTestGUI.TypingTestListener
    public void onTypingComplete(String typedText, double timeTaken) {
        this.typedText = typedText;
        this.totalTime = timeTaken;

        System.out.println("Typed: " + typedText);
        System.out.println("Time: " + String.format("%.2f", totalTime) + " seconds.");

        feedbackAnalyzer.evaluatePerformance(originalText, typedText, totalTime);

        storeResult(); // Store the result after evaluation
        displayResult(); // Display the result screen
    }

    public void storeResult() {
        double wpm = feedbackAnalyzer.getWPM();
        double accuracy = feedbackAnalyzer.getAccuracy();
        int mistakes = feedbackAnalyzer.getMistakes();

        Result result = new Result(user.getUsername(), wpm, accuracy, mistakes);
        leaderboard.addResult(result); // Add result to the global leaderboard
    }

    public void displayResult() {
        double wpm = feedbackAnalyzer.getWPM();
        double accuracy = feedbackAnalyzer.getAccuracy();
        int mistakes = feedbackAnalyzer.getMistakes();

        // Launch the ResultScreen
        new ResultScreen(wpm, accuracy, mistakes, user, leaderboard);
    }
}