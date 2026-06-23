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
            Date birthdate = sdf.parse(date);

            Calendar currentDate = Calendar.getInstance();

            Calendar birthdateCalendar = Calendar.getInstance();
            birthdateCalendar.setTime(birthdate);

            int yearData = currentDate.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);

            int monthsData = currentDate.get(Calendar.MONTH) - birthdateCalendar.get(Calendar.MONTH);

            if (monthsData < 0) {
                monthsData += 12;
                yearData--;
            }

            return("≈" + yearData + " " + yearsAnd + " " + monthsData + " " + months + ".");

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int calculateMonths(String dateOfBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dobDate = sdf.parse(dateOfBirth);
            Calendar currentDate = Calendar.getInstance();
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dobDate);
            int monthDifference = 0;
            while (!dobCalendar.after(currentDate)) {
                dobCalendar.add(Calendar.MONTH, 1);
                monthDifference++;
            }
            return monthDifference - 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
