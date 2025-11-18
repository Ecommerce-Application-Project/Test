package com.model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private double totalAmount;
    private Timestamp date;
    private List<CartItem> items; // line items from OrderDescription

    public Order(int orderId, double totalAmount, Timestamp date, List<CartItem> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.date = date;
        this.items = items;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    public Timestamp getDate() { return date; }
    public List<CartItem> getItems() { return items; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId)
                .append(" | Total: ₹").append(totalAmount)
                .append(" | Date: ").append(date).append("\n");
        if (items != null && !items.isEmpty()) {
            sb.append("Items:\n");
            for (CartItem item : items) {
                sb.append("   ").append(item).append("\n");
            }
        }
        return sb.toString();
    }
}