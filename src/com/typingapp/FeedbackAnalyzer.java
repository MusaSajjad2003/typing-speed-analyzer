package com.typingapp;

public class FeedbackAnalyzer {
    private double wpm;
    private double accuracy;
    private int mistakes;

    public void evaluatePerformance(String originalText, String typedText, double timeTakenSeconds) {
        mistakes = 0;

        int minLength = Math.min(originalText.length(), typedText.length());

        for (int i = 0; i < minLength; i++) {
            if (typedText.charAt(i) != originalText.charAt(i)) {
                mistakes++;
            }
        }

        // Add mistakes for length mismatch
        mistakes += Math.abs(originalText.length() - typedText.length());

        if (typedText.length() == 0) {
            accuracy = 0.0;
        } else {
            accuracy = ((double) (typedText.length() - mistakes) / typedText.length()) * 100;
            if (accuracy < 0) accuracy = 0; // Avoid negative accuracy
        }

        if (timeTakenSeconds == 0) {
            wpm = 0.0;
        } else {
            double minutes = timeTakenSeconds / 60.0;
            wpm = (typedText.length() / 5.0) / minutes;
        }
    }

    public double getWPM() {
        return wpm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getMistakes() {
        return mistakes;
    }
}