package com.velogexpress.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DateTime {
    public static String CURRENTDATE(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String fch = sdf.format(cal.getTime());
        return fch;
    }


    public static String CURRENTDATETIME(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fch = sdf.format(cal.getTime());
        return fch;
    }

    public static String ADDDAYSTODATE(int day){
        SimpleDateFormat sdf = new SimpleDateFormat(" EEEE dd MMM yyyy", Locale.FRANCE);
        Calendar c = Calendar.getInstance();
        // c.setTime(c.getDAte); // Using today's date
        c.add(Calendar.DATE, day); // Adding  days
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String FORMATDATETIMEFRENCH(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMMM yyyy 'à' HH:mm",
                Locale.FRENCH
        );

        return dateTime.format(formatter);
    }

}
