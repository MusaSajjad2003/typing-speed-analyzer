package com.typingapp;

// TypingApp.java (Main Controller)
public class TypingApp {
    public static void main(String[] args) {
        // Initialize the Leaderboard instance for Member 3
        Leaderboard<Result> globalLeaderboard = new Leaderboard<>();

        // Launch the login screen on application start
        // The LoginScreen will handle user authentication
        // and then pass the authenticated User and the Leaderboard instance
        // to the DashboardScreen.
        new LoginScreen(globalLeaderboard); // Pass the leaderboard to LoginScreen
    }
}