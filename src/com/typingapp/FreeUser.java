package com.typingapp;

public class FreeUser extends User {
    public FreeUser(String username, String password) {
        super(username, password);
    }

    @Override
    public void typingMode() {
        System.out.println("Free user typing mode activated.");
        // This method might be expanded in Module 2 for actual typing test logic
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}