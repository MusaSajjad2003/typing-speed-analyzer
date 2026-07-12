package com.typingapp;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User implements TypingBehavior, Serializable {
    private static final long serialVersionUID = 1L;

    protected String username;
    protected String passwordHash; // SHA-256 hash; plaintext password is never stored

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String input) {
        return passwordHash.equals(hashPassword(input));
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available on every standard JVM
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public abstract boolean isPremium();
}