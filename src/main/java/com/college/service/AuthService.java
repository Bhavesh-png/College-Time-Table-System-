package com.college.service;

import com.college.model.User;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private static final String USER_FILE = "users.txt";
    private static final Map<String, User> userCache = new ConcurrentHashMap<>();

    static {
        loadUsers();
    }

    private static synchronized void loadUsers() {
        File f = new File(USER_FILE);
        if (!f.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    userCache.put(parts[0], new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public static synchronized boolean login(String username, String password) {
        if (username == null || password == null) return false;
        User user = userCache.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public static synchronized boolean register(String username, String password, String secret) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) return false;
        if (userCache.containsKey(username)) return false;

        User newUser = new User(username, password, secret);
        userCache.put(username, newUser);
        return appendUserToFile(newUser);
    }

    public static synchronized boolean resetPassword(String username, String secret, String newPassword) {
        User user = userCache.get(username);
        // Case-insensitive secret answer check for better UX
        if (user != null && user.getSecret().equalsIgnoreCase(secret)) {
            user.setPassword(newPassword);
            return saveAllUsers();
        }
        return false;
    }

    private static boolean appendUserToFile(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE, true))) {
            writer.println(user.getUsername() + "," + user.getPassword() + "," + user.getSecret());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean saveAllUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : userCache.values()) {
                writer.println(user.getUsername() + "," + user.getPassword() + "," + user.getSecret());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
