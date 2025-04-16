

package Midterm_Requirement;

import java.io.*;
import java.util.*;

public class Restaurant_Ordering_System {
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "C:\\Users\\dacip\\Desktop\\Midterm Requirement.txt";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Fast Food Ordering System ===");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                if (loginUser()) {
                    placeOrder();
                    break;
                }
            
            } else if (choice.equals("2")) {
                registerUser();
            } else {
                System.out.println("Invalid option.");
            }
        }
    }


    static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String encryptedPassword = encrypt(password);

        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + encryptedPassword);
            System.out.println("Registered successfully! Please log in.");
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }


    static boolean loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUsername = parts[0];
                    String storedEncryptedPassword = parts[1];

                    if (storedUsername.equals(username)) {
                        String decryptedPassword = decrypt(storedEncryptedPassword);
                        if (password.equals(decryptedPassword)) {
                            System.out.println("Login successful! Welcome, " + username + "!");
                            return true;
                        } else {
                            System.out.println("Incorrect password.");
                            return false;
                        }
                    }
                }
            }
            System.out.println("Username not found.");
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }

        return false;
    }

    static String encrypt(String password) {
        StringBuilder result = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + 3) % 26 + base);
            }
            result.append(c);
        }
        return result.toString();
    }


    static String decrypt(String password) {
        StringBuilder result = new StringBuilder();
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base - 3 + 26) % 26 + base);
            }
            result.append(c);
        }
        return result.toString();
    }

  
    static void placeOrder() {
        String[] menu = {"Burger", "Fries", "Drink"};
        int[] prices = {50, 30, 20};
        int[] quantities = new int[3];

        while (true) {
            System.out.println("\n=== Menu ===");
            for (int i = 0; i < 3; i++) {
                System.out.println((i + 1) + ". " + menu[i] + " - P" + prices[i]);
            }
            System.out.println("4. Exit and Show Order Summary");
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            if (input.equals("4")) break;

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    System.out.print("Enter quantity for " + menu[choice - 1] + ": ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    if (qty > 0) {
                        quantities[choice - 1] += qty;
                    } else {
                        System.out.println("Please enter a positive number.");
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }

    
        System.out.println("\n=== Order Summary ===");
        int total = 0;
        for (int i = 0; i < 3; i++) {
            if (quantities[i] > 0) {
                int cost = quantities[i] * prices[i];
                System.out.println(menu[i] + " x" + quantities[i] + " = P" + cost);
                total += cost;
            }
        }
        System.out.println("Total: P" + total);
    }
}