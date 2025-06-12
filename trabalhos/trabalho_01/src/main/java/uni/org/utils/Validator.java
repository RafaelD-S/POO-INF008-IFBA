package uni.org.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uni.org.controlers.EventControl;
import uni.org.models.event.Event;

public class Validator {
    public static Boolean date(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        
        try {
            sdf.parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Boolean cpf(String string) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        return string != null && string.matches(regex);
    }

    public static Boolean email(String string) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return string != null && string.matches(regex);
    }

    public static Boolean eventName(String string) {
        List<Event> events = EventControl.getLocalInstance().getEvents();
        for (Event event : events) {
            if (event.getName() == string) return false; 
        }
        return true;    
    }

    public static Boolean participantName(String string) {
        List<Event> participants = EventControl.getLocalInstance().getEvents();
        for (Event participant : participants) {
            if (participant.getName() == string) return false; 
        }
        return true;    
    }
}