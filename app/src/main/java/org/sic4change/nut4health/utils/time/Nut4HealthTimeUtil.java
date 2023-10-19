package org.sic4change.nut4health.utils.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

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

    public static String yearsAndMonthCalculator(String date, String yearsAnd, String months) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date fechaNacimientoDate = sdf.parse(date);

            Calendar fechaActual = Calendar.getInstance();

            Calendar fechaNacimientoCalendar = Calendar.getInstance();
            fechaNacimientoCalendar.setTime(fechaNacimientoDate);

            int edad = fechaActual.get(Calendar.YEAR) - fechaNacimientoCalendar.get(Calendar.YEAR);

            int meses = fechaActual.get(Calendar.MONTH) - fechaNacimientoCalendar.get(Calendar.MONTH);

            if (meses < 0) {
                meses += 12;
                edad--;
            }

            return("≈" + edad + " " + yearsAnd + " " + meses + " " + months + ".");

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
