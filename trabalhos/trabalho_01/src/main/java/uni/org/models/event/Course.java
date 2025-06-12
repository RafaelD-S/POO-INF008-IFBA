package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Course extends Event {
    private String subject;
    private String lecturer;

    public Course(String name, String date, String modality, String subject, String lecturer, int capacity) {
        super("Lecture", name, date, capacity, modality);
        this.subject = subject;
        this.lecturer = lecturer;
    }

    public Course(String name, String date, String modality, String subject, String lecturer, int capacity, String location) {
        super("Lecture", name, date, capacity, modality, location);
        this.subject = subject;
        this.lecturer = lecturer;
    }

    public String getSubject() {
        return subject;
    }

    public String getLecturer() {
        return lecturer;
    }

    @Override
	public void printEvent() {
        super.printEvent();
        Console.log("Subject: " + subject + "\nLecturer: " + lecturer);
	}
}