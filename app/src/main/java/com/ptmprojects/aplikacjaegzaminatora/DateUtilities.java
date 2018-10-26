package com.ptmprojects.aplikacjaegzaminatora;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtilities {
    public static final SimpleDateFormat DATE_FORMAT_USED_IN_DATABASE = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DATE_FORMAT_USED_ON_THE_BUTTONS = new SimpleDateFormat("dd' 'MMMM' 'yyyy");
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat TWO_DIGIT_MONTH_FORMAT = new SimpleDateFormat("MM");
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyyMM");

    public static int convertCalendarToIntUsedInDatabase(Calendar calendar) {
        return Integer.valueOf(DATE_FORMAT_USED_IN_DATABASE.format(calendar.getTime()));
    }



    public static Calendar convertIntUsedInDatabaseToCalendar(int dateStoredInDatabase) {
        Calendar resultCalendar = Calendar.getInstance();
        try {
            resultCalendar.setTime(DATE_FORMAT_USED_IN_DATABASE.parse(String.valueOf(dateStoredInDatabase)));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultCalendar;
    }



//    public static int convertCalendarToIntUsedInDatabase(Calendar calendar) {
//        String calendarAsString = DateUtilities.convertCalendarToStringUsedInDatabase(calendar);
//        int calendarAsStringConvertedToInt = Integer.valueOf(calendarAsString);
//        return  calendarAsStringConvertedToInt;
//    }
//
//    public static Calendar convertDateIntFromDatabaseToCalendar(int dateAsIntFromDatabase) {
//        String dateAsIntToString = String.valueOf(dateAsIntFromDatabase);
//        int yearPart = Integer.valueOf(dateAsIntToString.substring(0,4));
//        int monthPart = Integer.valueOf(dateAsIntToString.substring(4, 6));
//        int monthPartNeededInCalendar = monthPart - 1; // January is 01 in database, but 0 in Calendar;
//        int dayOfMonthPart = Integer.valueOf(dateAsIntToString.substring(6, 8));
//        Calendar calendarSetToDateFromDatabase = Calendar.getInstance();
//        calendarSetToDateFromDatabase.set(yearPart, monthPartNeededInCalendar, dayOfMonthPart);
//        return  calendarSetToDateFromDatabase;
//    }
}
