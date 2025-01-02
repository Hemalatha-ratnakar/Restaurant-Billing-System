
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RestaurantBillingSystem {

    // HashMap to store menu items and their prices
    static HashMap<String, Double> menu = new HashMap<>();
    static List<String> orderedItems = new ArrayList<>();
    static List<Integer> quantities = new ArrayList<>();

    // Set up the menu
    public static void setupMenu() {
        menu.put("Burger", 120.0);
        menu.put("Pizza", 250.0);
        menu.put("Pasta", 150.0);
        menu.put("Fries", 80.0);
        menu.put("Coke", 50.0);
    }

    // Display the menu
    public static void displayMenu() {
        System.out.println("----- MENU -----");
        for (Map.Entry<String, Double> item : menu.entrySet()) {
            System.out.println(item.getKey() + " - \u20B9" + item.getValue());
        }
        System.out.println("----------------");
    }
    

    // Take orders from the user
    public static void takeOrder(Scanner scanner) {
        while (true) {
            System.out.println("Enter item name to order (or type 'done' to finish): ");
            String itemName = scanner.nextLine();

            if (itemName.equalsIgnoreCase("done"))
                break;

            if (menu.containsKey(itemName)) {
                System.out.println("Enter quantity: ");
                int qty = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                orderedItems.add(itemName);
                quantities.add(qty);
            } else {
                System.out.println("Item not available. Please choose from the menu.");
            }
        }
    }

    // Generate the bill
    public static void generateBill() {
        double total = 0;

        System.out.println("----- BILL -----");
        for (int i = 0; i < orderedItems.size(); i++) {
            String item = orderedItems.get(i);
            int qty = quantities.get(i);
            double price = menu.get(item) * qty;

            System.out.println(item + " x " + qty + " = ₹" + price);
            total += price;
        }

        total = applyDiscount(total);
        double totalWithTax = calculateTotalWithTax(total);

        System.out.println("----------------");
        System.out.println("Total with Tax: ₹" + totalWithTax);
        System.out.println("----------------");

        saveReceiptToFile(total, totalWithTax);
    }

    // Apply discounts
    public static double applyDiscount(double total) {
        if (total > 1000) {
            double discount = total * 0.10; // 10% discount
            System.out.println("Discount (10%): ₹" + discount);
            return total - discount;
        }
        return total;
    }

    // Calculate total with tax
    public static double calculateTotalWithTax(double total) {
        final double TAX_RATE = 0.05; // 5% tax
        double tax = total * TAX_RATE;
        System.out.println("Tax (5%): ₹" + tax);
        return total + tax;
    }

    // Save the receipt to a file
    public static void saveReceiptToFile(double total, double totalWithTax) {
        try (FileWriter writer = new FileWriter("receipt.txt")) {
            writer.write("----- RECEIPT -----\n");
            for (int i = 0; i < orderedItems.size(); i++) {
                String item = orderedItems.get(i);
                int qty = quantities.get(i);
                double price = menu.get(item) * qty;
                writer.write(item + " x " + qty + " = ₹" + price + "\n");
            }
            writer.write("--------------------\n");
            writer.write("Total: ₹" + total + "\n");
            writer.write("Tax (5%): ₹" + (totalWithTax - total) + "\n");
            writer.write("Total with Tax: ₹" + totalWithTax + "\n");
            writer.write("--------------------\n");
            writer.write("Thank you for dining with us!\n");
            System.out.println("Receipt saved to 'receipt.txt'.");
        } catch (IOException e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        setupMenu();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Restaurant Billing System!");
        displayMenu();

        takeOrder(scanner);
        generateBill();

        System.out.println("Thank you for dining with us!");
        scanner.close();
    }
}
