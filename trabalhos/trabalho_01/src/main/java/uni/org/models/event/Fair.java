package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Fair extends Event {
    private String subject;

    public Fair(String name, String date, String location, String subject, int capacity) {
        super("Lecture", name, date, capacity, location);
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