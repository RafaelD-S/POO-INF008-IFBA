package uni.org.models.event;

import uni.org.utils.Console;
import uni.org.models.event.Event;

public class Workshop extends Event {
    private String theme;

    public Workshop(String name, String date, String modality, String theme, int capacity) {
        super("Lecture", name, date, capacity, modality);
        this.theme = theme;
    }

    public Workshop(String name, String date, String modality, String theme, int capacity, String location) {
        super("Lecture", name, date, capacity, modality, location);
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