package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Course extends Event {
    private String subject;
    private String teacher;

    public Course(String name, String date, String location, String subject, String teacher, int capacity) {
        super("Lecture", name, date, capacity, location);
        this.subject = subject;
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    @Override
	public void printEvent() {
        super.printEvent();
        Console.log("Subject: " + subject + "\nTeacher: " + teacher);
	}
}