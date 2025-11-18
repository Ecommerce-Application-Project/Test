package com.service;

public class RoleManager {
    public static boolean isAdmin(String role) {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public static boolean isUser(String role) {
        return "USER".equalsIgnoreCase(role);
    }

    public static boolean isGuest(String role) {
        return "GUEST".equalsIgnoreCase(role);
    }
}