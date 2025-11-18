package com.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantity;

    // Constructor
    public Product(int productId, String name, String description, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return productId + " | " + name + " | " + description +
                " | ₹" + price + " | Qty: " + quantity;
    }
}