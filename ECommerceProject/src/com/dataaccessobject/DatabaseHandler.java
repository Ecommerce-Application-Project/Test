package com.dataaccessobject;

import java.sql.*;
import java.util.*;
import com.model.Product;
import com.model.User;
import com.model.CartItem;
import com.model.Order;

public class DatabaseHandler {

    // Connection setup
    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ecommerce", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // ---------------- Product Methods ----------------
    public void insertProduct(Product p) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO Products(name, description, price, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getQuantity());
            ps.executeUpdate();
            System.out.println("Product inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(int id) {
        Product product = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM Products WHERE product_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> getAllProductsSorted() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM Products ORDER BY name ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // ---------------- User Methods ----------------
    public void insertUser(User u) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO Users(username, password, role, email, city) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getCity());
            ps.executeUpdate();
            System.out.println("User inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password) {
        User user = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("city")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // ---------------- Cart Methods ----------------
    // Helper: update product quantity
    private boolean updateProductQuantity(int productId, int purchasedQty) {
        String sql = "UPDATE Products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, purchasedQty);
            ps.setInt(2, productId);
            ps.setInt(3, purchasedQty);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // true if update succeeded
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addToCart(int userId, int productId, int qty) {
        if (updateProductQuantity(productId, qty)) {
            try (Connection conn = getConnection()) {
                String sql = "INSERT INTO Cart(user_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setInt(3, qty);
                ps.executeUpdate();
                System.out.println("Item added to cart and stock updated!");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not enough stock available!");
        }
        return false;
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT c.cart_id, p.product_id, p.name, p.price, c.quantity " +
                    "FROM Cart c JOIN Products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new CartItem(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // ---------------- Order Methods ----------------
    public void placeOrder(int userId, List<CartItem> items) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();

            String orderSql = "INSERT INTO Orders(user_id, total_amount) VALUES (?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, userId);
            psOrder.setDouble(2, total);
            psOrder.executeUpdate();

            ResultSet keys = psOrder.getGeneratedKeys();
            if (keys.next()) {
                int orderId = keys.getInt(1);

                String detailSql = "INSERT INTO OrderDescription(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                PreparedStatement psDetail = conn.prepareStatement(detailSql);
                for (CartItem item : items) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getProductId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setDouble(4, item.getPrice());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            conn.commit();
            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getUserOrders(String username) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String orderSql = "SELECT o.order_id, o.total_amount, o.date " +
                    "FROM Orders o JOIN Users u ON o.user_id = u.user_id " +
                    "WHERE u.username = ?";
            PreparedStatement psOrder = conn.prepareStatement(orderSql);
            psOrder.setString(1, username);
            ResultSet rsOrder = psOrder.executeQuery();

            while (rsOrder.next()) {
                int orderId = rsOrder.getInt("order_id");
                double totalAmount = rsOrder.getDouble("total_amount");
                Timestamp date = rsOrder.getTimestamp("date");

                // Fetch line items for this order
                List<CartItem> items = new ArrayList<>();
                String detailSql = "SELECT od.product_id, p.name, od.price, od.quantity " +
                        "FROM OrderDescription od JOIN Products p ON od.product_id = p.product_id " +
                        "WHERE od.order_id = ?";
                PreparedStatement psDetail = conn.prepareStatement(detailSql);
                psDetail.setInt(1, orderId);
                ResultSet rsDetail = psDetail.executeQuery();

                while (rsDetail.next()) {
                    items.add(new CartItem(
                            rsDetail.getInt("product_id"),
                            rsDetail.getString("name"),
                            rsDetail.getDouble("price"),
                            rsDetail.getInt("quantity")
                    ));
                }

                orders.add(new Order(orderId, totalAmount, date, items));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}