package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Workshop extends Event {
    private String theme;

    public Workshop(String name, String date, String location, String theme, int capacity) {
        super("Lecture", name, date, capacity, location);
        this.theme = theme;
    }

    public String getTheme() {
        return theme;
    }

    @Override
	public void printEvent() {
        super.printEvent();
        Console.log("Theme: " + theme);
	}
}