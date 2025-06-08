package uni.org.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import uni.org.utils.Console;

public class FileUtil {

    public static void createTxt(String path, String name, String text) {
        try {
            File directory = new File(path);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Console.log("Failed to create directory: " + path);
                    return;
                }
            }

            FileWriter writer = new FileWriter(new File(directory, name + ".txt"));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            Console.log("An error occurred.");
            e.printStackTrace();
        }
    }
}