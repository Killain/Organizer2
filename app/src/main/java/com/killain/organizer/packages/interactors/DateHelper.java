package com.killain.organizer.packages.interactors;

import com.killain.organizer.packages.enums.FormatDateOutput;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private String day, month, result, time, date, task_date, minute, hour;
    private int int_hour, int_minute;
    private LocalDate localDate;
    private char zeroChar = 0;
    private Calendar local_calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DateHelper() {
        local_calendar = Calendar.getInstance();
    }

    private String formatMonth(int month) {
        String convertedMonth = null;
        switch (month) {
            case 1:
                convertedMonth = "Jan";
                break;

            case 2:
                convertedMonth = "Feb";
                break;

            case 3:
                convertedMonth = "Mar";
                break;

            case 4:
                convertedMonth = "Apr";
                break;

            case  5:
                convertedMonth = "May";
                break;

            case 6:
                convertedMonth = "Jun";
                break;

            case 7:
                convertedMonth = "Jul";
                break;

            case 8:
                convertedMonth = "Aug";
                break;

            case 9:
                convertedMonth = "Sep";
                break;

            case 10:
                convertedMonth = "Oct";
                break;

            case 11:
                convertedMonth = "Nov";
                break;

            case 12:
                convertedMonth = "Dec";
                break;
        }
        return convertedMonth;
    }

    public String calendarToString(Calendar calendar, FormatDateOutput formatDateOutput) {
        Calendar compareCalendar = Calendar.getInstance();
        local_calendar = calendar;
        if (compareCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                compareCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                compareCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            result = "Today";
            return result;
        } else {
//            String dayOfWeek_raw = String.valueOf(local_calendar.get(Calendar.DAY_OF_WEEK));
//            String dow_raw = dayOfWeek_raw.substring(0, 3).toLowerCase();
//            String dow_raw = dayOfWeek_raw.substring(0, 2).toLowerCase();
//            char first_letter_dow = Character.toUpperCase(dow_raw.charAt(0));
//            String dow = first_letter_dow + dow_raw.substring(1, dow_raw.length());
            String dow = convertCalendarDOW(local_calendar.get(Calendar.DAY_OF_WEEK));
            String dayOfMonth = Integer.toString(local_calendar.get(Calendar.DAY_OF_MONTH));
//            String month_raw = Integer.toString(local_calendar.get(Calendar.MONTH));
//            String month_raw_2 = month_raw.substring(0, 3).toLowerCase();
//            char first_letter = Character.toUpperCase(month_raw_2.charAt(0));
//            String month = first_letter + month_raw_2.substring(1, month_raw_2.length());
            String month = formatMonth(local_calendar.get(Calendar.MONTH));
            String year = Integer.toString(local_calendar.get(Calendar.YEAR));
            result = dow + ", " + month + " " + dayOfMonth + ", " + year;
            return result;
        }
    }

    public long convertCalendarToLong(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    public String localDateToTaskString(LocalDate localDate) {
            String year = Integer.toString(localDate.getYear());
            String month = Integer.toString(localDate.getMonthValue());
            String day = Integer.toString(localDate.getDayOfMonth());
            task_date = day + "/" + month + "/" + year;
            return task_date;
    }

    public void setDate(int year, int month, int day) {
        local_calendar.set(Calendar.YEAR, year);
        local_calendar.set(Calendar.MONTH, month);
        local_calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public void setTime(int hour, int minute) {
        this.hour = Integer.toString(hour);
        this.minute = Integer.toString(minute);

        if (minute < 9) {
            this.minute = "0" + Integer.toString(minute);
        }

        if (hour < 9) {
            this.hour = "0" + Integer.toString(hour);
        }

        time = this.hour + ":" + this.minute;
    }

    public String getFullDateWithTime() {
        date = calendarToString(local_calendar, FormatDateOutput.DEFAULT);
        result = date + " " + time;
        return result;
    }

    public long getTaskDate() {
        return local_calendar.getTimeInMillis();
    }

    public String getTaskTime() {
        return hour + ":" + minute;
    }

    public void setDateAndTime(long long_date, String raw_time) {
        String formatted_sdf_hour = raw_time.substring(0, 2);
        String formatted_sdf_minute = raw_time.substring(3, 5);

        int_hour = Integer.parseInt(formatted_sdf_hour);
        int_minute = Integer.parseInt(formatted_sdf_minute);

        local_calendar.setTimeInMillis(long_date);
    }

    public int getYear() {
        return local_calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return local_calendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return local_calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getInt_hour() {
        return int_hour;
    }

    public int getInt_minute() {
        return int_minute;
    }

    private void convertToDecimal(String day, String month) {
        if (day.charAt(0) == zeroChar) {
            StringBuilder sb = new StringBuilder(day);
            sb.deleteCharAt(0);
            day = sb.toString();
        }

        if (month.charAt(0) == zeroChar) {
            StringBuilder stringBuilder = new StringBuilder(month);
            stringBuilder.deleteCharAt(0);
            month = stringBuilder.toString();
        }
    }

    public String longToString(long long_date) {
        local_calendar.setTimeInMillis(long_date);
        Date date = new Date();
        date.setTime(local_calendar.getTimeInMillis());
        return sdf.format(date.getTime());
    }

    public String getConvertedDateFromLong(long long_date) {
        local_calendar.setTimeInMillis(long_date);
        return calendarToString(local_calendar, FormatDateOutput.FORMAT_DATE_OUTPUT);
    }

    private String convertCalendarDOW(int dow) {

        String converted_dow = null;

        switch (dow) {

            case 1:
                converted_dow = "Mon";
                break;

            case 2:
                converted_dow = "Tue";
                break;

            case 3:
                converted_dow = "Wed";
                break;

            case 4:
                converted_dow =  "Thu";
                break;

            case 5:
                converted_dow = "Fri";
                break;

            case 6:
                converted_dow = "Sat";
                break;
            case 7:
                converted_dow = "Sun";
                break;

                default: break;
        }
        return converted_dow;
    }
}
