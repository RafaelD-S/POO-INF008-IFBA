package uni.org.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {
    private static final Scanner scanner = new Scanner(System.in);

    public static void log(String message) {
        System.out.println(message);
    }

    public static void log(String message, int endSpace) {
        System.out.println(message);

        for (int i = 0; i < endSpace; i++)
            System.out.println("\n");
    }

    public static void log(String message, int endSpace, int startSpace) {
        for (int i = 0; i < startSpace; i++)
            System.out.println("\n");

        System.out.println(message);

        for (int i = 0; i < endSpace; i++)
            System.out.println("\n");
    }

    public static String ask(String question) {
        log(question, 0, 1);
        
        System.out.print("=> ");
        return scanner.nextLine(); 
    }
    
    public static void list(List<String> messages) {
        for (int i = 0; i < messages.size(); i++) {
            log("[" + (i + 1) + "]: " + messages.get(i));
        }
    }

    public static int listChoice(List<String> messages) {
        list(messages);
        
        System.out.print("=> ");
        while (!scanner.hasNextInt()) {
            warn("Por favor, digite um número.");
            scanner.nextLine();
            System.out.print("=> ");
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public static String listChoice(List<String> messages, Boolean returnItem) {
        int choice = listChoice(messages);
        return messages.get(choice);
    }
    
    public static void warn(String message) {
        log("[WARNING]: " + message, 1, 2);
    }
}