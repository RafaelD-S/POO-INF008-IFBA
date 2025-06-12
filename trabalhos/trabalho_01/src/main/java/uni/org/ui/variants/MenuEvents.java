package uni.org.ui.variants;

import uni.org.ui.enums.Choices;
import uni.org.utils.Console;
import uni.org.utils.Validator;
import uni.org.controlers.EventControl;
import uni.org.models.event.*;

public class MenuEvents {

    public static void start() {
        while (true) {
            Console.log("Event control. What do you wish? [Type 0 to return]", 0, 1);
            switch (Console.listChoice(Choices.get("events"))) {
                case 0: return;
                case 1: addEvent(); break;
                case 2: deleteEvent(); break;
                case 3: listEvents(); break;
            }
        }
    }

    private static void addEvent() {
        
        Console.log("What's the type of the event?", 1, 1);
        String type;
        while (true) {
            type = Console.listChoice(Choices.get("events_type"), true);
            if (type != null) break;
            Console.warn("Invalid Choice. Try Again.");
        }

        String name = null;
        while (true) {
            name = Console.ask("What's the name of the event?");
            if (Validator.eventName(name)) break;
            Console.warn("Name already used. Try Again");
        }
            
        String date = null;
         while (true) {
            date = Console.ask("Set a date for this event. [format: DD/MM/AAAA]");
            if (Validator.date(date)) break;
            Console.warn("Invalid Date. Try Again");
        }
            
        int capacity = 0;
        while (true) {
            capacity = Console.askInt("How many people are allowed in this event?");
            if (capacity > 0) break;
            Console.warn("Invalid Capacity. Try Again");
        }
            
        Console.log("What's the modality of this event?", 1, 1);
        String modality;
        while (true) {
            modality = Console.listChoice(Choices.get("events_modality"), true);
            if (modality != null) break;
            Console.warn("Invalid Choice. Try Again.");
        }

        String location = null;
        if (modality == "Hybrid" || modality == "Presential") {
            location = Console.ask("Where this event will be located?");
        }
            

        switch (type) {
            case "Course": 
                String lecturerCourse = Console.ask("Who is the lecturer of this course?");
                String subjectCourse = Console.ask("What's the subject of this course?");
                Course course;
                if (location != null) course = new Course(name, date, modality, subjectCourse, lecturerCourse, capacity, location);
                else course = new Course(name, date, modality, subjectCourse, lecturerCourse, capacity);
                
                EventControl.getLocalInstance().add(course);

                break;
            case "Fair":
                String subjectFair = Console.ask("Whats the subject of this fair?");
                Fair fair;

                if (location != null) fair = new Fair(name, date, modality, subjectFair, capacity, location);
                else fair = new Fair(name, date, modality, subjectFair, capacity);

                EventControl.getLocalInstance().add(fair);

                break;
            case "Lecture":
                String lecturerLecture = Console.ask("Who is the lecturer of this course?");
                Lecture lecture;

                if (location != null) lecture = new Lecture(name, date, modality, lecturerLecture, capacity, location);
                else lecture = new Lecture(name, date, modality, lecturerLecture, capacity);

                EventControl.getLocalInstance().add(lecture);
                break;
            case "Workshop":
                String workshopTheme = Console.ask("Whats the theme of this Workshop?");
                Workshop workshop;

                if (location != null) workshop = new Workshop(name, date, modality, workshopTheme, capacity, location);
                else workshop = new Workshop(name, date, modality, workshopTheme, capacity);

                EventControl.getLocalInstance().add(workshop);
                break;
        }
        return;
    }

    private static void deleteEvent() {
        while (true) {
            if (EventControl.getLocalInstance().hasEvent()) {
                Console.log("Event removal. Chose an event bellow to remove. [Type 0 to return]", 1, 1);
    
                int choice = Console.listChoice(EventControl.getLocalInstance().getEventNames());
                switch (choice) {
                    case 0: return;
                    default: EventControl.getLocalInstance().remove(choice - 1);
                }
            } 
            else {
                Console.log("No Events available", 0, 1);
                return;
            }
        }
    }

    private static void listEvents() {
        if (EventControl.getLocalInstance().hasEvent()) {
            Console.log("Here is a list of available events:", 0, 1);
            Console.list(EventControl.getLocalInstance().getEventNames());
        }
        else {
            Console.log("No Events available", 0, 1);
        }
    }
}