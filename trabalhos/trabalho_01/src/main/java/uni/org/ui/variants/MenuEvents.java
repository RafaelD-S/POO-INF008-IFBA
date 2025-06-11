package uni.org.ui.variants;

import uni.org.ui.enums.Choices;
import uni.org.utils.Console;
import uni.org.utils.Validator;

public class MenuEvents {

    public static void start() {
        while (true) {
            Console.log("Event control. What do you wish? [Type 0 to return]", 0, 1);
            switch (Console.listChoice(Choices.get("events"))) {
                case 0: return;
                case 1: addEvent();
            }
        }
    }

    private static void addEvent() {
        while (true) {
            String type = Console.listChoice(Choices.get("events_type"), true);
            String name = Console.ask("What's the name of the event?");
            
            while (true) {
                String date = Console.ask("Set a date for this event. [format: DD/MM/AAAA]");
                if (Validator.date(date)) break;
                Console.warn("Invalid Date. Try Again");
            }
            
    
            String capacity = Console.ask("How many people are allowed in this event?");
            String modality = Console.listChoice(Choices.get("events_type"), true);
        }
    }
}