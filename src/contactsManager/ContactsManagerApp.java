package contactsManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ContactsManagerApp {
    public static void main(String[] args) throws IOException {
        int userChoice = contactMenu();
        if (userChoice == 1){
            printContacts();
        } else if (userChoice == 2) {
            addContacts();
            printContacts();
        }


    }

    static Path filePath = Paths.get("./data/contacts.txt");

    public static int contactMenu() {
        System.out.println("-----------------------------");
        System.out.println("\nWhat would you like to do?");
        System.out.println("  1. View contacts");
        System.out.println("  2. Add a new contact");
        System.out.println("  3. Search a contact by name and/or phone number.");
        System.out.println("  4. Delete an existing contact.");
        System.out.println("  5. Exit");
        System.out.print("\nEnter an option (1, 2, 3, 4 or 5): ");

        Scanner myScanner = new Scanner(System.in);
        int userChoice = myScanner.nextInt();
        System.out.println("-----------------------------");
        return userChoice;
    }

    public static void printContacts() throws IOException{
        try {
            List<String> fileContents = Files.readAllLines(filePath);
            for (int i = 0; i < fileContents.size(); i++) {
                System.out.printf("%s\n", fileContents.get(i));
            }
        } catch (IOException e) {
            System.out.println("Cannot find the file according to the given path.");
        }

    }

    public static void addContacts() throws IOException {
        // ask the user for first name, last name and phone number
        System.out.println("Please enter the first name of the contact.");
        Scanner scanner = new Scanner(System.in);
        String firstName = scanner.nextLine();

        System.out.println("Please enter the last name of the contact.");
        String lastName = scanner.nextLine();

        System.out.println("Please enter the phone number.");
        String phoneNumber = scanner.nextLine();

        // make the name first letter uppercase and then concat name and phone number
        String userInput = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase() + " | " + phoneNumber;
        List<String> contacts = Files.readAllLines(filePath);

        // check if userInput is in the contacts list, if not, then add
        if (!contacts.contains(userInput)) {
            try {
                if (Files.exists(filePath)) {
                    Files.writeString(filePath, userInput + System.lineSeparator(), StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                System.out.println("Cannot find the file according to the given path.");
            }
        }
    }

}
