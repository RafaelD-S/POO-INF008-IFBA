package uni.org.ui.variants;

import uni.org.ui.enums.Choices;
import uni.org.utils.Console;
import uni.org.utils.Validator;
import uni.org.controlers.ParticipantControl;
import uni.org.models.participant.*;

public class MenuParticipants {

public static void start() {
        while (true) {
            Console.log("Participant control. What do you wish? [Type 0 to return]", 0, 1);
            switch (Console.listChoice(Choices.get("PARTICIPANTS"))) {
                case 0: return;
                case 1: addParticipant(); break;
                case 2: deleteParticipant(); break;
                case 3: listParticipants(); break;
            }
        }
    }

    private static void addParticipant() {
        Console.log("What are you?", 1, 1);
        String type;
        while (true) {
            type = Console.listChoice(Choices.get("PARTICIPANTS_TYPE"), true);
            if (type != null) break;
            Console.warn("Invalid Choice. Try Again.");
        }

        String name = null;
        while (true) {
            name = Console.ask("What's your name?");
            if (Validator.eventName(name)) break;
            Console.warn("Name already used. Try Again");
        }     
            
        String cpf = null;
         while (true) {
            cpf = Console.ask("What's your cpf? [format: 000.000.000-00]");
            if (Validator.cpf(cpf)) break;
            Console.warn("Invalid CPF. Try Again");
        }
            
        String email = null;
        while (true) {
            email = Console.ask("What's your email? [format: text@company.com]");
            if (Validator.email(email)) break;
            Console.warn("Invalid email. Try Again");
        }
            

        switch (type) {
            case "Student": 
                String studentId = Console.ask("What's your student ID?");
                Student student = new Student(name, cpf, email, studentId);
                
                ParticipantControl.getLocalInstance().add(student);
                break;
            case "Teacher":
                String teacherId = Console.ask("What's your teaching ID?");
                Teacher teacher = new Teacher(name, cpf, email, teacherId);
                
                ParticipantControl.getLocalInstance().add(teacher);
                break;
            case "External Person":
                String externalInstituition = Console.ask("What instituition are you from? [if you do not want to inform, write: none]");

                ExternalPerson external;
                if (externalInstituition.toLowerCase() == "none") external = new ExternalPerson(name, cpf, email);
                else external = new ExternalPerson(name, cpf, email, externalInstituition);
                
                ParticipantControl.getLocalInstance().add(external);
                break;
        }
        Console.log("Created " + type + " Successfully", 1, 1);
        return;
    }

    private static void deleteParticipant() {
        while (true) {
            if (ParticipantControl.getLocalInstance().hasParticipant()) {
                Console.log("Participant removal. Chose an person bellow to remove. [Type 0 to return]", 1, 1);
    
                int choice = Console.listChoice(ParticipantControl.getLocalInstance().getParticipantsNames());
                switch (choice) {
                    case 0: return;
                    default: ParticipantControl.getLocalInstance().remove(choice - 1);
                }
            } 
            else {
                Console.log("No Participants available", 0, 1);
                return;
            }
        }
    }

    private static void listParticipants() {
        if (ParticipantControl.getLocalInstance().hasParticipant()) {
            Console.log("Here is a list of available participants:", 0, 1);
            Console.list(ParticipantControl.getLocalInstance().getParticipantsNames());
        }
        else {
            Console.log("No participants available", 0, 1);
        }
    }
}