import java.util.*;

// Interface
interface Discountable {
    default double applyDiscount(double price) {
        return Math.round(price * 0.90 * 100.0) / 100.0;
    }

    double getDiscountedPrice();
}

// Custom Exception
class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}

// Abstract Class
abstract class MenuItem implements Discountable {
    protected final String name;
    protected final double price;
    protected final String description;
    protected static int totalMenuItems = 0;

    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        totalMenuItems++;
    }

    public MenuItem(String name, double price) {
        this(name, price, "No description available");
    }

    public abstract double calculatePrice();

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public double getDiscountedPrice() {
        return applyDiscount(calculatePrice());
    }
}

// Food Item
class FoodItem extends MenuItem {

    public FoodItem(String name, double price, String description) {
        super(name, price, description);
    }

    @Override
    public double calculatePrice() {
        return Math.round(price * 1.05 * 100.0) / 100.0;
    }
}

// Drink Item
class DrinkItem extends MenuItem {

    public DrinkItem(String name, double price, String description) {
        super(name, price, description);
    }

    @Override
    public double calculatePrice() {
        return Math.round(price * 1.10 * 100.0) / 100.0;
    }
}

// Customer
class Customer {
    private final String name;
    private final String phone;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
}

// Order
class Order {
    private static int orderCounter = 1000;
    private final int orderId;
    private final Customer customer;
    private final ArrayList<OrderItem> items = new ArrayList<>();

    public Order(Customer customer) {
        this.customer = customer;
        this.orderId = orderCounter++;
    }

    static class OrderItem {
        private final MenuItem item;
        private final int quantity;

        public OrderItem(MenuItem item, int quantity) throws InvalidOrderException {
            if (quantity <= 0) {
                throw new InvalidOrderException("Quantity must be greater than 0!");
            }
            this.item = item;
            this.quantity = quantity;
        }

        public double getSubtotal() {
            return item.getDiscountedPrice() * quantity;
        }

        public String getName() {
            return item.getName();
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public void addItem(MenuItem item, int quantity) throws InvalidOrderException {
        items.add(new OrderItem(item, quantity));
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem oi : items) {
            total += oi.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    public void printOrder() {
        System.out.println("\n=== Order #" + orderId + " for " + customer.getName() + " ===");
        for (OrderItem oi : items) {
            System.out.printf("%-22s x %-3d = Rs. %.2f%n",
                    oi.getName(), oi.getQuantity(), oi.getSubtotal());
        }
        System.out.println("-------------------------------------");
        System.out.printf("Total (after discount): Rs. %.2f%n", calculateTotal());
    }
}

// Singleton Restaurant
class Restaurant {
    private static final Restaurant instance = new Restaurant();
    private final ArrayList<MenuItem> menu = new ArrayList<>();

    private Restaurant() {
        menu.add(new FoodItem("Chicken Biryani", 450, "Spicy rice with chicken"));
        menu.add(new FoodItem("Beef Burger", 320, "Classic beef patty"));
        menu.add(new DrinkItem("Mango Lassi", 150, "Fresh mango yogurt drink"));
        menu.add(new DrinkItem("Cold Coffee", 180, "Iced coffee with cream"));
        menu.add(new FoodItem("Pizza Margherita", 650, "Cheese and tomato"));
    }

    public static Restaurant getInstance() {
        return instance;
    }

    public void showMenu() {
        System.out.println("\n=== JAVA BISTRO MENU ===");
        System.out.println("ID  Item Name                Price     Type");
        System.out.println("------------------------------------------------");

        for (int i = 0; i < menu.size(); i++) {
            MenuItem m = menu.get(i);
            String type = (m instanceof FoodItem) ? "Food" : "Drink";

            System.out.printf("%-3d %-24s Rs. %-6.0f %s%n",
                    i + 1, m.getName(), m.getPrice(), type);
        }
    }

    public MenuItem findItemById(int id) {
        if (id < 1 || id > menu.size()) return null;

        MenuItem item = menu.get(id - 1);

        if (item instanceof DrinkItem) {
            System.out.println("   (Drink item - 10% tax applied)");
        }

        return item;
    }
}

// Bill
class Bill {
    private final Order order;

    public Bill(Order order) {
        this.order = order;
    }

    public void generateBill() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("              JAVA BISTRO - OFFICIAL BILL");
        System.out.println("=".repeat(55));

        order.printOrder();

        System.out.println("\nThank you for visiting JavaBistro!");
        System.out.println("=".repeat(55));
    }
}

// MAIN CLASS
public class Main {
    public static void main(String[] args) {

        Restaurant restaurant = Restaurant.getInstance();
        Scanner scanner = new Scanner(System.in);

        Customer customer = new Customer("Muhammad Omar", "0312-3456789");
        Order currentOrder = new Order(customer);

        System.out.println("=====================================");
        System.out.println("  WELCOME TO JAVA BISTRO");
        System.out.println("=====================================\n");

        boolean running = true;

        while (running) {
            try {
                System.out.println("1. Show Menu");
                System.out.println("2. Add Item");
                System.out.println("3. View Order");
                System.out.println("4. Generate Bill & Exit");
                System.out.println("5. Exit");
                System.out.print("Choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> restaurant.showMenu();

                    case 2 -> {
                        restaurant.showMenu();
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();

                        MenuItem item = restaurant.findItemById(id);

                        if (item != null) {
                            System.out.print("Quantity: ");
                            int qty = scanner.nextInt();
                            currentOrder.addItem(item, qty);
                            System.out.println("Added!");
                        } else {
                            System.out.println("Invalid ID!");
                        }
                    }

                    case 3 -> currentOrder.printOrder();

                    case 4 -> {
                        new Bill(currentOrder).generateBill();
                        running = false;
                    }

                    case 5 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }

                    default -> System.out.println("Invalid choice!");
                }

            } catch (InvalidOrderException e) {
                System.out.println("Wrong" + e.getMessage());
            } catch (Exception e) {
                System.out.println(" Invalid input!");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}