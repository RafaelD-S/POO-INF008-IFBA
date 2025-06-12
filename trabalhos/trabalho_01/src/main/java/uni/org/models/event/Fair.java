package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Fair extends Event {
    private String subject;

    public Fair(String name, String date, String modality, String subject, int capacity) {
        super("Lecture", name, date, capacity, modality);
        this.subject = subject;
    }

    public Fair(String name, String date, String modality, String subject, int capacity, String location) {
        super("Lecture", name, date, capacity, modality, location);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
	public void printEvent() {
        super.printEvent();
        Console.log("Subject: " + subject);
	}
}