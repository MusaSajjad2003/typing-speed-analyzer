package com.typingapp;

import java.util.*;
import java.io.*;

/**
 * Leaderboard handles top results and user history using generics and ArrayList.
 */
public class Leaderboard<T extends Result> {
    private ArrayList<T> results;
    private final String fileName = "results.dat"; // File to store leaderboard data

    public Leaderboard() {
        results = new ArrayList<>();
        loadResults(); // Load existing results on startup
    }

    public void addResult(T result) {
        results.add(result);
        saveResults(); // Save results immediately after adding
    }

    public List<T> getTopResults(int topN) {
        // Sort results by WPM in descending order
        results.sort((r1, r2) -> Double.compare(r2.getWpm(), r1.getWpm()));
        // Return the top N results, or fewer if there aren't enough
        return results.subList(0, Math.min(topN, results.size()));
    }

    public List<T> getUserHistory(String username) {
        List<T> history = new ArrayList<>();
        for (T r : results) {
            if (r.getUsername().equalsIgnoreCase(username)) {
                history.add(r);
            }
        }
        return history;
    }

    // Saves the current list of results to a file
    private void saveResults() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(results);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving leaderboard results: " + e.getMessage());
        }
    }

    // Loads results from a file
    private void loadResults() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            results = (ArrayList<T>) in.readObject();
        } catch (FileNotFoundException e) {
            // File not found, implies no results saved yet, so start with an empty list
            System.out.println("No existing leaderboard data found. Starting with empty leaderboard.");
            results = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error loading leaderboard results: " + e.getMessage());
            results = new ArrayList<>(); // Fallback to empty list on error
        }
    }
}