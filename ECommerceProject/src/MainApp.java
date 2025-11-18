import java.util.Scanner;
import java.util.List;

import com.dataaccessobject.DatabaseHandler;
import com.model.Product;
import com.model.User;
import com.model.CartItem;
import com.model.Order;

public class MainApp {
    private static Scanner sc = new Scanner(System.in);
    private static DatabaseHandler dbHandler = new DatabaseHandler();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== E-Commerce App ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Exit");                // shifted Exit to 4
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> guestMenu();
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------------- LOGIN ----------------
    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = dbHandler.login(username, password);
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("admin")) {
                adminMenu(user);
            } else {
                customerMenu(user);
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    // ---------------- REGISTER ----------------
    private static void register() {
        System.out.print("New username: ");
        String username = sc.nextLine();
        System.out.print("New password: ");
        String password = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("City: ");
        String city = sc.nextLine();

        User user = new User(0, username, password, "user", email, city);
        dbHandler.insertUser(user); // matches DAO
    }

    // ---------------- ADMIN MENU ----------------
    private static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. List Products");
            System.out.println("2. Add Product");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> listProducts();
                case 2 -> addProduct();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void listProducts() {
        List<Product> list = dbHandler.getAllProductsSorted(); // matches DAO
        if (list == null || list.isEmpty()) {
            System.out.println("No products.");
            return;
        }
        System.out.println("ID | Name | Price | Quantity");
        for (Product p : list) {
            System.out.printf("%d | %s | %.2f | %d%n",
                    p.getProductId(), p.getName(), p.getPrice(), p.getQuantity());
        }
    }

    private static void addProduct() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());

        Product p = new Product(0, name, desc, price, qty);
        dbHandler.insertProduct(p); // matches DAO
    }

    // ---------------- CUSTOMER MENU ----------------
    private static void customerMenu(User user) {
        while (true) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Browse Products");
            System.out.println("2. View Cart");
            System.out.println("3. Add to Cart");
            System.out.println("4. Place Order");
            System.out.println("5. Order History");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> listProducts();
                case 2 -> viewCart(user);
                case 3 -> addToCart(user);
                case 4 -> placeOrder(user);
                case 5 -> orderHistory(user);
                case 6 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewCart(User user) {
        List<CartItem> items = dbHandler.getCartItems(user.getUserId()); // matches DAO
        if (items == null || items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("ProductID | Name | Qty | UnitPrice | Subtotal");
        double total = 0.0;
        for (CartItem ci : items) {
            double sub = ci.getPrice() * ci.getQuantity();
            total += sub;
            System.out.printf("%d | %s | %d | %.2f | %.2f%n",
                    ci.getProductId(), ci.getProductName(), ci.getQuantity(), ci.getPrice(), sub);
        }
        System.out.printf("Total: %.2f%n", total);
    }

    private static void addToCart(User user) {
        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        dbHandler.addToCart(user.getUserId(), pid, qty); // matches DAO
    }

    private static void placeOrder(User user) {
        List<CartItem> items = dbHandler.getCartItems(user.getUserId());
        dbHandler.placeOrder(user.getUserId(), items); // matches DAO
    }

    private static void orderHistory(User user) {
        List<Order> list = dbHandler.getUserOrders(user.getUsername()); // matches DAO
        if (list == null || list.isEmpty()) {
            System.out.println("No orders yet.");
            return;
        }
        System.out.println("OrderID | Date | Total");
        for (Order o : list) {
            System.out.printf("%d | %s | %.2f%n",
                    o.getOrderId(), o.getDate(), o.getTotalAmount());
        }
    }

    // ---------------- GUEST MENU ----------------
    private static void guestMenu() {
        User guest = new User(0, "guest", "", "GUEST", "", "");
        while (true) {
            System.out.println("\n=== Guest Menu ===");
            System.out.println("1. Browse Products");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> listProducts();
                case 2 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}