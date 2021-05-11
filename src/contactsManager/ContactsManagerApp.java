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

    public static void main(String[] args) {
        int userChoice = contactMenu();
        if (userChoice == 1){
            printContacts();
        }


    }

    String filePath = "/Users/mono/IdeaProjects/contacts-manager/data/contacts.txt";

    public  static  void printContacts() throws IOException{
        try {
            List<String> fileContents = Files.readAllLines(filePath);
            for (int i = 0; i < fileContents.size(); i++) {
                System.out.printf("%d: %s\n", i + 1, fileContents.get(i));
            }
        } catch (IOException e) {
            System.out.println("Cannot find the file according to the given path.");
        }

    }

}
