package contactsManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
        }else if (userChoice == 3) {
            searchName();
        } else if (userChoice == 4) {
            deleteContact();
            printContacts();
        } else {
            System.out.println("Thank you for using Contacts Manager :)");
        }


    }

    static Path filePath = Paths.get("./data/contacts.txt");

    public static int contactMenu() {
        System.out.println("-----------------------------");
        System.out.println("\nWhat would you like to do?");
        System.out.println("  1. View contacts");
        System.out.println("  2. Add a new contact");
        System.out.println("  3. Search a contact by name.");
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

    public static String getUserInput(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static boolean yesNo(String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    public static String getName() {
        // ask the user for first name, last name and phone number
        String firstName = getUserInput("Please enter the first name of the contact.");

        String lastName = getUserInput("Please enter the last name of the contact.");

        // make the name first letter uppercase and then concat name and phone number
        String name = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase() + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        return name;
    }
    public static void addContacts() throws IOException {
        // ask the user for the contact name
        String name = getName();

        List<String> contacts = Files.readAllLines(filePath);
        boolean nameFound = false;

        try {
            // check if name is in the contacts list, if not, then add
            for (String contact : contacts) {
                if (contact.contains(name)) {
                    nameFound = true;
                    if (yesNo("Would you like to update the contact? [y/n]")) {
                        String newNumber = getUserInput("Please enter new phone number.");
                        contacts.set(contacts.indexOf(contact), name + " | " + newNumber);
                        continue;
                    }
                }
            }
            Files.write(filePath, contacts);

            if (!nameFound) {
                String phoneNumber = getUserInput("Please enter the phone number.");
                String userInput = name + " | " + phoneNumber;
                Files.writeString(filePath, userInput + System.lineSeparator(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Cannot find the file according to the given path.");
        }
    }

    public static void searchName() throws IOException {
        String userInput = getUserInput("Would you like to search by First Name or Last Name?");
        if(userInput.equalsIgnoreCase("First Name")){
            userInput = getUserInput("Please Enter First Name.");
        }else{
            userInput = getUserInput("Please Enter Last Name.");
        }
        userInput = userInput.substring(0, 1).toUpperCase() + userInput.substring(1).toLowerCase();
        try{
            List<String> contacts = Files.readAllLines(filePath);
            for (int i = 0; i < contacts.size(); i++) {
                if(contacts.get(i).contains(userInput)){
                    System.out.printf("%s\n", contacts.get(i));
                }
            }
        } catch (IOException e) {
        System.out.println("Cannot find the file according to the given path.");
        }
    }

    public static void deleteContact() throws IOException {
        // display all the contacts in the console
        printContacts();

        List<String> contacts = Files.readAllLines(filePath);
        List<String> newList = new ArrayList<>();

        // ask the user for the name that they want to delete
        String name = getName();

        for (String contact : contacts) {
            if (contact.contains(name)) {
                continue; // skip adding to newList
            }
            newList.add(contact);
        }

        try {
            Files.write(filePath, newList);
        } catch (IOException e) {
            System.out.println("Cannot delete the contact.");
        }


    }
}
