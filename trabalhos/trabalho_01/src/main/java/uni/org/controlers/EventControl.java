package uni.org.controlers;

import java.util.List;
import java.util.ArrayList;

import uni.org.models.event.Event;
import uni.org.utils.Console;

public class EventControl {
    private static final EventControl instance = new EventControl();
    private final List<Event> eventList = new ArrayList<Event>();

    private EventControl() {}

    public static EventControl getLocalInstance() {
       return instance;
    }

    public List<Event> getEvents() {
        return new ArrayList<>(eventList);
    }

    public Boolean hasEvent() {
        return eventList.size() > 0;
    }

    public void add(Event item) {
        eventList.add(item);
    }

    public void remove(int index) {
        eventList.remove(index);
    }

    public List<String> getEventNames() {
        List<String> names = new ArrayList<>();
        for (Event event : eventList) {
            names.add(event.getName());
        }
        return names;
    }

    public void listEvents() {
        for (Event event : eventList) {
            Console.log(event.getName());
            Console.log(event.getType());
            Console.log(event.getDate(), 1);
        }
    }
}