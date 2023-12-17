import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {
    private static final String FILE_NAME = "passwords.txt"; // File to store passwords
    private static Map<String, String> passwordStore = new HashMap<>(); // Store passwords here

    public static void main(String[] args) {
        loadPasswordsFromFile(); // Load passwords from file (if exists)

        Scanner scanner = new Scanner(System.in);

        if (passwordStore.isEmpty()) {
            System.out.println("No passwords found or file not found.");
            System.out.println("Creating a new password store.");
            System.out.print("Set a master password: ");
            String masterPassword = scanner.nextLine();

            // Save the master password (in a real application, this should be encrypted)
            saveMasterPassword(masterPassword);
        } else {
            System.out.print("Enter master password: ");
            String masterPassword = scanner.nextLine();

            if (!authenticate(masterPassword)) {
                System.out.println("Authentication failed. Exiting...");
                System.exit(0);
            }
            System.out.println("Authentication successful!");
        }

        while (true) {
            displayOptions();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline after reading int

            switch (choice) {
                case 1:
                    addPassword(scanner);
                    break;
                case 2:
                    retrievePassword(scanner);
                    break;
                case 3:
                    updatePassword(scanner);
                    break;
                case 4:
                    deletePassword(scanner);
                    break;
                case 5:
                    savePasswordsToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayOptions() {
        System.out.println("\n1. Add Password");
        System.out.println("2. Retrieve Password");
        System.out.println("3. Update Password");
        System.out.println("4. Delete Password");
        System.out.println("5. Exit");
    }

    private static void loadPasswordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    passwordStore.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading passwords from file: " + e.getMessage());
        }
    }

    private static void saveMasterPassword(String masterPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("MasterPassword:" + masterPassword);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving master password to file: " + e.getMessage());
        }
    }

    private static boolean authenticate(String masterPassword) {
        return masterPassword.equals("password"); // Hardcoded check (not recommended in production)
    }

    private static void addPassword(Scanner scanner) {
        System.out.print("Enter website/service name: ");
        String website = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        passwordStore.put(website, password);
        System.out.println("Password added successfully for " + website);
    }

    private static void retrievePassword(Scanner scanner) {
        System.out.print("Enter website/service name to retrieve password: ");
        String website = scanner.nextLine();

        if (passwordStore.containsKey(website)) {
            String password = passwordStore.get(website);
            System.out.println("Password for " + website + ": " + password);
        } else {
            System.out.println("Password not found for " + website);
        }
    }

    private static void updatePassword(Scanner scanner) {
        System.out.print("Enter website/service name to update password: ");
        String website = scanner.nextLine();

        if (passwordStore.containsKey(website)) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            passwordStore.put(website, newPassword);
            System.out.println("Password for " + website + " updated successfully");
        } else {
            System.out.println("Password not found for " + website);
        }
    }

    private static void deletePassword(Scanner scanner) {
        System.out.print("Enter website/service name to delete password: ");
        String website = scanner.nextLine();

        if (passwordStore.containsKey(website)) {
            passwordStore.remove(website);
            System.out.println("Password for " + website + " deleted successfully");
        } else {
            System.out.println("Password not found for " + website);
        }
    }

    private static void savePasswordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : passwordStore.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving passwords to file: " + e.getMessage());
        }
    }
}
