package uni.org.utils;

import java.util.ArrayList;

public class Console {

    public static void log(String message) {
        System.out.println(message);
    }
    
    public static void log(ArrayList<String> itemList) {
        for (String item : itemList) {
            System.out.println(item);
        }
    }
    
    public static void log(ArrayList<String> itemList, String message) {
        for (String item : itemList) {
            System.out.println(message + item);
        }
    }
    
    public static void warn(String message) {
        System.out.println("[WARNING]: " + message);
    }
}