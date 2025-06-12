package uni.org.services;

import uni.org.models.event.Event;
import uni.org.models.participant.Participant;
import uni.org.utils.FileUtil;

public class Certificate {
    public static void generate(Participant participant, Event event) {
        String sanitizedDate = event.getDate().replace("/", "-");

        String fileName = (participant.getName() + "_certificate_" + event.getType() + "_" + sanitizedDate);
        String message = (participant.getName() + ", cpf " + participant.getCpf() + ", Has finished the " + event.getType() + " " + event.getName() + " on the day " + sanitizedDate + ".");

        FileUtil.createTxt("./certificates", fileName, message);
        Console.log("Your certificate has been generated!", 1, 1);
    }
}