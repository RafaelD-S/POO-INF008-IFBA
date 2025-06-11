package uni.org.ui;

import uni.org.ui.enums.Choices;
import uni.org.utils.Console;
import uni.org.ui.variants.*;

public class Menu {

    public static void start() {
        Console.log("Welcome to the program controler of the university events", 1, 1);
        while (true) {
            Console.log("What do you wish to do? [Type 0 to finish]");
            switch (Console.listChoice(Choices.get("main"))) {
                case 0: return;
                case 1: MenuEvents.start(); break;
                case 2: MenuParticipants.start(); break;
                case 3: MenuServices.start(); break;
            }
        }
    }
}