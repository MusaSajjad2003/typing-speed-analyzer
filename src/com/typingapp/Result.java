package com.typingapp;

import java.io.Serializable;

/**
 * Represents the result of a typing test.
 * Implements Serializable for binary file storage.
 */
public class Result implements Serializable {
    private String username;
    private double wpm;
    private double accuracy;
    private int mistakes;

    public Result(String username, double wpm, double accuracy, int mistakes) {
        this.username = username;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.mistakes = mistakes;
    }

    public String getUsername() {
        return username;
    }

    public double getWpm() {
        return wpm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getMistakes() {
        return mistakes;
    }

    @Override
    public String toString() {
        return username + " - WPM: " + wpm + ", Accuracy: " + accuracy + "%" + ", Mistakes: " + mistakes;
    }
}