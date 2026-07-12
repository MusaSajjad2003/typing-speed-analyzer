package com.typingapp;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class TypingTestGUI {
    private long startTime;
    private long endTime;
    private boolean started = false;
    private StringBuilder typedText = new StringBuilder();
    private int currentIndex = 0;
    private Timer timer; // Swing Timer
    private JLabel timerLabel;
    private JTextPane textPane; // Make textPane accessible to update styles

    public interface TypingTestListener {
        void onTypingComplete(String typedText, double timeTaken);
    }

    public void showGUI(String textToType, TypingTestListener listener) {
        JFrame frame = new JFrame("Typing Test");
        // Use DISPOSE_ON_CLOSE so it only closes this window, not the entire application
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null); // Center the frame

        JLabel instructionLabel = new JLabel("Type the text given below and press Enter when done: ");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));


        textPane = new JTextPane(); // Initialize textPane
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 24)); // Larger font for readability
        textPane.setBackground(new Color(240, 240, 240)); // Light gray background
        StyledDocument doc = textPane.getStyledDocument();

        // Define default style for the entire text
        Style defaultStyle = doc.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        StyleConstants.setBackground(defaultStyle, new Color(240, 240, 240)); // Default background

        // Define correct and incorrect styles
        Style correctStyle = doc.addStyle("correct", defaultStyle);
        StyleConstants.setForeground(correctStyle, Color.BLUE); // Correct chars in blue
        StyleConstants.setBold(correctStyle, true);

        Style incorrectStyle = doc.addStyle("incorrect", defaultStyle);
        StyleConstants.setForeground(incorrectStyle, Color.RED); // Incorrect chars in red
        StyleConstants.setBold(incorrectStyle, true);

        try {
            // Insert the original text using the default style
            doc.insertString(doc.getLength(), textToType, defaultStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 20));
        inputField.setBackground(new Color(250, 250, 250)); // Slightly off-white background
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Padding


        timerLabel = new JLabel("Time: 0.0s | WPM: 0 | Mistakes: 0", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));


        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Start timer on first non-control key press
                if (!started && !Character.isISOControl(e.getKeyChar()) && e.getKeyCode() != KeyEvent.VK_SHIFT) {
                    started = true;
                    startTime = System.currentTimeMillis();

                    timer = new Timer(100, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            endTime = System.currentTimeMillis();
                            double elapsed = (endTime - startTime) / 1000.0;
                            // Calculate WPM and mistakes dynamically for display
                            updateStatsDisplay(textToType, typedText.toString(), elapsed);
                        }
                    });
                    timer.start();
                }

                // Handle Backspace
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (currentIndex > 0) {
                        currentIndex--;
                        // Remove last char from typedText
                        if (typedText.length() > 0) {
                            typedText.setLength(typedText.length() - 1);
                        }
                        // Reset style of the character at currentIndex in textPane
                        // This character was the one that was incorrectly typed or backspaced over
                        try {
                            doc.setCharacterAttributes(currentIndex, 1, defaultStyle, true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                // Handle Enter key (for submitting the test manually)
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                     completeTest(textToType, listener, frame);
                }
                // Handle regular character input
                else if (Character.isDefined(e.getKeyChar()) && !e.isControlDown() && currentIndex < textToType.length()) {
                    char userChar = e.getKeyChar();
                    char originalChar = textToType.charAt(currentIndex);

                    Style styleToApply;
                    if (userChar == originalChar) {
                        styleToApply = correctStyle;
                    }
                    else {
                        styleToApply = incorrectStyle;
                    }

                    // Apply style to the original textPane for feedback
                    try {
                        doc.setCharacterAttributes(currentIndex, 1, styleToApply, true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    typedText.append(userChar);
                    currentIndex++;

                    // If all original text has been typed
                    if (currentIndex == textToType.length()) {
                        completeTest(textToType, listener, frame);
                    }
                }
            }
        });

        frame.setLayout(new BorderLayout(10, 10)); // Add gaps
        frame.add(instructionLabel, BorderLayout.NORTH);
        frame.add(new JScrollPane(textPane), BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.add(timerLabel, BorderLayout.PAGE_END);

        frame.setVisible(true);
        inputField.requestFocusInWindow(); // Give focus to the input field
    }

    private void updateStatsDisplay(String originalText, String currentTypedText, double elapsedSeconds) {
        int currentMistakes = 0;
        int minLength = Math.min(originalText.length(), currentTypedText.length());

        for (int i = 0; i < minLength; i++) {
            if (originalText.charAt(i) != currentTypedText.charAt(i)) {
                currentMistakes++;
            }
        }
        // Count extra characters as mistakes
        if (currentTypedText.length() > originalText.length()) {
            currentMistakes += (currentTypedText.length() - originalText.length());
        }

        double currentWPM = 0.0;
        if (elapsedSeconds > 0) {
            currentWPM = (currentTypedText.length() / 5.0) / (elapsedSeconds / 60.0);
        }
        timerLabel.setText(String.format("Time: %.1fs | WPM: %.0f | Mistakes: %d", elapsedSeconds, currentWPM, currentMistakes));
    }

    private void completeTest(String textToType, TypingTestListener listener, JFrame frame) {
        if (timer != null) timer.stop();
        endTime = System.currentTimeMillis(); // Ensure endTime is set
        double timeTaken = (endTime - startTime) / 1000.0;
        frame.dispose(); // Close the typing test GUI
        listener.onTypingComplete(typedText.toString(), timeTaken); // Notify listener
    }
}