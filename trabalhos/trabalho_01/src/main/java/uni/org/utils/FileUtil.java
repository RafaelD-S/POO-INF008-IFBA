package uni.org.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void createTxt(String path, String name, String text) {
        try {
            File directory = new File(path);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    System.out.println("Failed to create directory: " + path);
                    return;
                }
            }

            FileWriter writer = new FileWriter(new File(directory, name + ".txt"));
            writer.write(text);
            writer.close();
            System.out.println("File created and written successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}