package com.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String city;

    // Constructor
    public User(int userId, String username, String password,
                String role, String email, String city) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.city = city;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return "User: " + username + " | Role: " + role +
                " | Email: " + email + " | City: " + city;
    }
}