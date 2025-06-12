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

    public static Boolean eventName(String string) {
        List<Event> eventos = EventControl.getLocalInstance().getEvents();
        for (Event evento : eventos) {
            if (evento.getName() == string) return false; 
        }
        return true;    
    }
}