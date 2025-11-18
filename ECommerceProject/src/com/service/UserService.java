package com.service;

import com.model.User;

public class UserService {
    public void showMenu(User user) {
        String role = user.getRole().toUpperCase();
        System.out.println("\nWelcome, " + user.getUsername() + " (" + role + ")");
        switch (role) {
            case "ADMIN":
                System.out.println("1. Manage Products");
                System.out.println("2. View Users");
                System.out.println("3. View Order History");
                break;
            case "USER":
                System.out.println("1. View Products");
                System.out.println("2. Add to Cart");
                System.out.println("3. Checkout");
                break;
            case "GUEST":
                System.out.println("1. View Products");
                System.out.println("2. Register / Login");
                break;
            default:
                System.out.println("Invalid role. Access denied.");
        }
    }
}