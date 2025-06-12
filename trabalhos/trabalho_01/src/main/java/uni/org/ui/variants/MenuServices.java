package uni.org.ui.variants;

import uni.org.controlers.EventControl;
import uni.org.models.event.Event;
import uni.org.models.participant.Participant;
import uni.org.services.Certificate;
import uni.org.ui.enums.Choices;
import uni.org.utils.Console;

public class MenuServices {

    public static void start() {
        while (true) {
            Console.log("Services control. What do you wish? [Type 0 to return]", 0, 1);
            switch (Console.listChoice(Choices.get("services"))) {
                case 0: return;
                case 1: generateCertificate(); break;
            }
        }
    }

    private static void generateCertificate() {
        if (!EventControl.getLocalInstance().hasEvent()) {
            Console.log("There is no events available", 1, 1);
        } else {
            Console.log("Choose an event to generate a certificate for");
            int eventIndex;
            while (true) {
                eventIndex = Console.listChoice(EventControl.getLocalInstance().getEventNames());
                if (EventControl.getLocalInstance().getEventByIndex(eventIndex - 1) != null) break;
                Console.warn("Invalid number. Try again.");
            }

            Event event = EventControl.getLocalInstance().getEventByIndex(eventIndex - 1);

            if (event.getParticipantsLeght() > 0) {
                int choice = Console.listChoice(event.getParticipantsNames());
                Participant participant = event.getParticipantByIndex(choice - 1);

                Certificate.generate(participant, event);
            }  
            else {
                Console.warn("There is no available participants");
            }
        }
    }
}