package com.typingapp;

import java.io.*;
import java.util.*;

public class TextGenerator {
    private ArrayList<String> easy;
    private ArrayList<String> medium; // Corrected typo from 'mediam'
    private ArrayList<String> hard;
    private String difficultyLevel;

    public TextGenerator() {
        easy = new ArrayList<>();
        medium = new ArrayList<>(); // Corrected typo
        hard = new ArrayList<>();
        difficultyLevel = "easy"; // Default difficulty
        loadText();
    }

    // Loading Text from files to ArrayLists
    public void loadText() {
        try {
            loadFromFile("resources/easy.txt", easy);
            loadFromFile("resources/medium.txt", medium); // Corrected typo
            loadFromFile("resources/hard.txt", hard);
        } catch (IOException e) {
            System.err.println("Error while loading texts: " + e.getMessage());
            // Optionally, provide default texts if files are missing/error
            if (easy.isEmpty()) easy.add("The quick brown fox jumps over the lazy dog.");
            if (medium.isEmpty()) medium.add("A journey of a thousand miles begins with a single step. Success is not final, failure is not fatal: it is the courage to continue that counts.");
            if (hard.isEmpty()) hard.add("The greatest glory in living lies not in never falling, but in rising every time we fall. The future belongs to those who believe in the beauty of their dreams.");
        }
    }

    // Reading From the text files
    public void loadFromFile(String fileName, ArrayList<String> list) throws IOException {
        // Use try-with-resources for automatic closing
        try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = read.readLine()) != null) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) { // Only add non-empty lines
                    list.add(trimmedLine);
                }
            }
        }
    }

    public void setDifficulty(int levelNumber) { // Changed parameter name for clarity
        switch (levelNumber) {
            case 1:
                difficultyLevel = "easy";
                break;
            case 2:
                difficultyLevel = "medium"; // Corrected typo
                break;
            case 3:
                difficultyLevel = "hard";
                break;
            default:
                difficultyLevel = "easy"; // Default to easy if invalid level
        }
    }

    // returning random index by first finding a random index then returning text from arraylist of that index
    public String getRandomText() {
        ArrayList<String> selectedList;

        switch (difficultyLevel) {
            case "medium": // Corrected typo
                selectedList = medium;
                break;
            case "hard":
                selectedList = hard;
                break;
            default: // "easy" or any invalid difficulty
                selectedList = easy;
        }

        if (!selectedList.isEmpty()) {
            // Generating random index
            int randomIndex = (int) (Math.random() * selectedList.size());
            return selectedList.get(randomIndex);
        } else {
            return "No text found."; // This should ideally not happen if loadText has defaults
        }
    }
}