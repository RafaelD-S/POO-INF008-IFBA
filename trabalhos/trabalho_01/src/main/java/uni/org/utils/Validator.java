package uni.org.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public static Boolean name(String string) {
        return true;
    }
}