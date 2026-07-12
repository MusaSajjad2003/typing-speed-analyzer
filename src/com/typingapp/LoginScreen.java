package com.typingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*; // For saving/loading users

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    // Use a static list of users for simplicity in this example
    // In a real application, you'd have a User database/persistence layer
    private static ArrayList<User> users = new ArrayList<>();
    private final String usersFileName = "users.dat"; // File to save user data

    private Leaderboard<Result> globalLeaderboard; // Reference to Member 3's leaderboard

    public LoginScreen(Leaderboard<Result> leaderboard) { // Corrected constructor
        this.globalLeaderboard = leaderboard; // Store the leaderboard instance
        loadUsers(); // Load users on startup

        setTitle("Typing App Login");
        setSize(300, 250); // Slightly larger
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); // Flexible rows, 1 column, with gaps
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // For buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(buttonPanel);
        add(panel);
        setVisible(true);

        // Event handling for login
        loginButton.addActionListener(e -> login());

        // Event handling for registration
        registerButton.addActionListener(e -> register());
    }

    private void login() {
        String uname = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (uname.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (User user : users) {
            if (user.getUsername().equals(uname) && user.checkPassword(pass)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Close login screen
                // Pass the authenticated user AND the globalLeaderboard instance to DashboardScreen
                new DashboardScreen(user, globalLeaderboard); // This is where the error occurred
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void register() {
        String uname = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (uname.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Registration Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(uname)) {
                JOptionPane.showMessageDialog(this, "Username already taken. Please choose another.", "Registration Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        String[] options = {"Free", "Premium"};
        int type = JOptionPane.showOptionDialog(this, "Select Account Type:", "Registration",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        User newUser;
        if (type == 0) { // Free
            newUser = new FreeUser(uname, pass);
        } else { // Premium
            newUser = new PremiumUser(uname, pass);
        }

        users.add(newUser);
        saveUsers(); // Save users after registration
        JOptionPane.showMessageDialog(this, "User '" + uname + "' registered successfully as " + options[type] + "!");
        // Clear fields after successful registration
        usernameField.setText("");
        passwordField.setText("");
    }

    // Methods for user persistence
    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(usersFileName))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user data.", "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(usersFileName))) {
            users = (ArrayList<User>) in.readObject();
        } catch (FileNotFoundException e) {
            // File not found, no users saved yet, start with an empty list
            System.out.println("No user data found. Starting with empty user list.");
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data. Starting fresh.", "Load Error", JOptionPane.ERROR_MESSAGE);
            users = new ArrayList<>(); // Fallback to empty list on error
        }
    }
}