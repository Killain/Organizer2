package com.killain.organizer.packages.interactors;

import com.killain.organizer.packages.enums.FormatDateOutput;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private String day, month, result, time, date, task_date, minute, hour;
    private int int_hour, int_minute;
    private LocalDate localDate;
    private char zeroChar = 0;

    public DateHelper() {
        localDate = LocalDate.now();
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

    public String localDateToString(LocalDate localDate, FormatDateOutput formatDateOutput) {
        LocalDate compare = LocalDate.now();

        if (compare.getYear() == localDate.getYear() &&
                compare.getMonthValue() == localDate.getMonthValue() &&
                compare.getDayOfMonth() == localDate.getDayOfMonth() &&
                formatDateOutput == FormatDateOutput.FORMAT_DATE_OUTPUT) {
            result = "Today";
            return result;
        } else {
            String dow = convertLocalDateDayOfWeek(localDate.getDayOfWeek());
            String dayOfMonth = Integer.toString(localDate.getDayOfMonth());
            String month = formatMonth(localDate.getMonthValue());
            String year = Integer.toString(localDate.getYear());
            result = dow + ", " + month + " " + dayOfMonth + ", " + year;
            return result;
        }
    }

    public String localDateToTaskString(LocalDate localDate) {
            String year = Integer.toString(localDate.getYear());
            String month = Integer.toString(localDate.getMonthValue());
            String day = Integer.toString(localDate.getDayOfMonth());
            task_date = day + "/" + month + "/" + year;
            return task_date;
    }

    public void setDate(int year, int month, int day) {
        localDate = LocalDate.of(year, month, day);
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
        date = localDateToString(localDate, FormatDateOutput.DEFAULT);
        result = date + " " + time;
        return result;
    }

    public int getYear() {
        return localDate.getYear();
    }

    public int getMonth() {
        return localDate.getMonthValue();
    }

    public int getDayOfMonth() {
        return localDate.getDayOfMonth();
    }

    public String getConvertedDateFromLong(long long_date) {
        localDate = LocalDate.ofEpochDay(long_date);
        return localDateToString(localDate, FormatDateOutput.FORMAT_DATE_OUTPUT);
    }

    private String convertLocalDateDayOfWeek(DayOfWeek dayOfWeek) {

        String converted_dow = null;

        switch (dayOfWeek) {

            case MONDAY:
                converted_dow = "Mon";
                break;

            case TUESDAY:
                converted_dow = "Tue";
                break;

            case WEDNESDAY:
                converted_dow = "Wed";
                break;

            case THURSDAY:
                converted_dow =  "Thu";
                break;

            case FRIDAY:
                converted_dow = "Fri";
                break;

            case SATURDAY:
                converted_dow = "Sat";
                break;
            case SUNDAY:
                converted_dow = "Sun";
                break;

                default: break;
        }
        return converted_dow;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public long getLong() {
        return localDate.toEpochDay();
    }
}
