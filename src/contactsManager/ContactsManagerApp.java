package contactsManager;
import java.io.IOException;
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
        List<Integer> choices = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        int userChoice = contactMenu();

        // check if the user enter the correct option
        while (userChoice > 5 || userChoice < 1) {
            userChoice = getInt("Enter an option (1, 2, 3, 4 or 5): ");
        }

        if (userChoice == 5) {
            System.out.println("Thank you for using Contacts Manager :)");
        } else {
            do {
                if (userChoice == 1){
                    printContacts();
                } else if (userChoice == 2) {
                    addContacts();
                    System.out.println();
                    printContacts();
                }else if (userChoice == 3) {
                    searchByName();
                    display();
                } else if (userChoice == 4) {
                    deleteContact();
                    System.out.println();
                    printContacts();
                }
                userChoice = contactMenu();
            } while (choices.contains(userChoice));
        }
    }

    static Path filePath = Paths.get("./data/contacts.txt");

    public static int contactMenu() {
        System.out.println("---------------------------------------------");
        System.out.println("What would you like to do?");
        System.out.println("  1. View contacts");
        System.out.println("  2. Add a new contact");
        System.out.println("  3. Search a contact by name.");
        System.out.println("  4. Delete an existing contact.");
        System.out.println("  5. Exit");
        System.out.print("\nEnter an option (1, 2, 3, 4 or 5): ");

        Scanner myScanner = new Scanner(System.in);
        int userChoice = myScanner.nextInt();
        System.out.println("---------------------------------------------");
        return userChoice;
    }

    public static void printContacts() {
        try {
            System.out.println("Name                 | Phone number         |");
            System.out.println("---------------------------------------------");
            List<String> fileContents = Files.readAllLines(filePath);
            for (int i = 0; i < fileContents.size(); i++) {
                String[] contactsArray = fileContents.get(i).split("\\|", 2);
                System.out.printf("%-20s | %-20s |\n", contactsArray[0].trim(), contactsArray[1].trim());
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

    public static int getInt(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
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

    public static String formatPhoneNumber(String number) {
        // check if the number is 7 or 10 digits which means it's US phone number
        if (number.length() == 7) {
            number = number.substring(0, 3) + "-" + number.substring(3);
        } else if (number.length() == 10) {
            number = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
        } else { // international phone number
            number = number.substring(0, number.length() - 10) + "-" + number.substring(number.length() - 10, number.length() - 7) + "-" + number.substring(number.length() - 7, number.length() - 4)+ "-" + number.substring(number.length() - 4);
        }
        return number;
    }

    public static boolean isPhoneNumberValid(String number) {
        if ((number.length() < 10 && number.length() != 7) || number.length() > 15) {
            System.out.println("The phone number you've entered is NOT a valid phone number :(");
            return false;
        }
        return true;
    }

    public static void addContacts() throws IOException {
        // ask the user for the contact name
        String name = getName();

        List<String> contacts = Files.readAllLines(filePath);
        boolean isNameFound = false;

        try {
            for (String contact : contacts) {
                // check if name is in the contacts list
                while (contact.contains(name)) { // if yes, ask if user wants to update
                    isNameFound = true;
                    System.out.printf("There is ALREADY a contact named %s.\n\n", name);
                    if (yesNo("Would you like to update the contact? [y/n]")) {
                        String newNumber;
                        do {
                            newNumber = getUserInput("Please enter new phone number.");
                        } while (!isPhoneNumberValid(newNumber));

                        // format the phone number
                        newNumber = formatPhoneNumber(newNumber);

                        contacts.set(contacts.indexOf(contact), name + " | " + newNumber);
                        break;
                    } else {
                        name = getName();
                    }
                }
            }
            Files.write(filePath, contacts);

            if (!isNameFound) { // if not, ask the user to enter phone number and add
                String phoneNumber;
                do {
                    phoneNumber = getUserInput("Please enter the phone number.");
                } while (!isPhoneNumberValid(phoneNumber));

                // format the phone number
                phoneNumber = formatPhoneNumber(phoneNumber);
                String userInput = name + " | " + phoneNumber;
                Files.writeString(filePath, userInput + System.lineSeparator(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Cannot find the file according to the given path.");
        }
    }

    static List<String> contactsSearched = new ArrayList<>();
    public static List<String> searchByName() throws IOException {
        int userInput;
        String name;

        System.out.println("Would you like to search by First Name or Last Name?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name\n");

        // check if the user enters the correct option
        do {
            userInput = getInt("Enter an option (1 or 2): ");
        } while(userInput != 1 && userInput != 2);

        // when the user enters the correct option and ask user to enter name
        if(userInput == 1){
            name = getUserInput("Please Enter First Name.");
        } else {
            name = getUserInput("Please Enter Last Name.");
        }

        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        List<String> contacts = Files.readAllLines(filePath);
        for (String contact : contacts) {
            if(contact.contains(name)){
                contactsSearched.add(contact);
            }
        }
        return contactsSearched;
    }

    public static void display() {
        System.out.println("---------------------------------------------");
        for (String contact : contactsSearched) {
            System.out.printf("%s\n", contact);
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
                System.out.println("---------------------------------------------");
                System.out.println(contact + "\n"); // print out the contact that user wants to delete
                continue; // skip adding to newList
            }
            newList.add(contact);
        }

        String input = getUserInput("Are you sure you want to delete this contact? [y/n]");

        // check if the user input equals to y or n
        while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
            input = getUserInput("If you want to delete this contact, please enter y. Otherwise, enter n.");
        }

        // only delete when the user is sure to delete
        if (input.equalsIgnoreCase("y")) {
            if(yesNo("This cannot be undone. Please make sure you want to delete.")){
                try {
                    Files.write(filePath, newList);
                } catch (IOException e) {
                    System.out.println("Cannot delete the contact.");
                }
            }
        }

    }
}
