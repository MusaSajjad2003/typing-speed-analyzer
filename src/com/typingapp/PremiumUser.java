package com.typingapp;

public class PremiumUser extends User {
    public PremiumUser(String username, String password) {
        super(username, password);
    }

    @Override
    public void typingMode() {
        System.out.println("Premium user typing mode activated.");
        // This method might be expanded in Module 2 for actual typing test logic
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}