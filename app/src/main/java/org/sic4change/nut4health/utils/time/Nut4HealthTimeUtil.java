package org.sic4change.nut4health.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Nut4HealthTimeUtil {

    public static long convertDateToTimeMilis(String dateString) {
        long dateInTimeMilis = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            dateInTimeMilis = formatter.parse(dateString).getTime();
        } catch (ParseException e3) {
            e3.printStackTrace();
        }
        return dateInTimeMilis;
    }

    public static long convertDateSimpleToTimeMilis(String dateString) {
        long dateInTimeMilis = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH);
        try {
            dateInTimeMilis = formatter.parse(dateString).getTime();
        } catch (ParseException e3) {
            e3.printStackTrace();
        }
        return dateInTimeMilis;
    }
}
