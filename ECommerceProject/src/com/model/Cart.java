package com.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    // Add product to cart
    public void addItem(CartItem item) {
        items.add(item);
        System.out.println(item.getProductName() + " added to cart!");
    }

    // Display cart contents
    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }
        System.out.println("---- Cart Contents ----");
        for (CartItem item : items) {
            System.out.println(item);
        }
        System.out.println("Total Bill: ₹" + calculateTotal());
    }

    // Calculate total bill
    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // Get items
    public List<CartItem> getItems() {
        return items;
    }
}