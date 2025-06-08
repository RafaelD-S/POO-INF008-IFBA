package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Lecture extends Event {
    private String lecturer;

    public Lecture(String name, String date, String location, String lecturer, int capacity) {
        super("Lecture", name, date, capacity, location);
        this.lecturer = lecturer;
    }

    public String getLecturer() {
        return lecturer;
    }

    @Override
	public void printEvent() {
        super.printEvent();
        Console.log("Lecturer: " + lecturer);
	}
}