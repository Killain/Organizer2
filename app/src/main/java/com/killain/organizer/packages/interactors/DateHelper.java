package com.killain.organizer.packages.interactors;

import com.killain.organizer.packages.enums.FormatDateOutput;

import org.threeten.bp.LocalDate;

public class DateHelper {

    private String day, month, result, time, date, task_date, minute, hour;
    private int int_hour, int_minute;
    private LocalDate localDate;
    private char zeroChar = 0;

    public DateHelper() {

    }

    private String formatMonth() {
        switch (month) {
            case "01":
                month = "Jan";
                break;

            case "02":
                month = "Feb";
                break;

            case "03":
                month = "Mar";
                break;

            case "04":
                month = "Apr";
                break;

            case  "05":
                month = "May";
                break;

            case "06":
                month = "Jun";
                break;

            case "07":
                month = "Jul";
                break;

            case "08":
                month = "Aug";
                break;

            case "09":
                month = "Sep";
                break;

            case "10":
                month = "Oct";
                break;

            case "11":
                month = "Nov";
                break;

            case "12":
                month = "Dec";
                break;
        }
        return month;
    }

    public String localDateToString(LocalDate localDate, FormatDateOutput formatDateOutput) {
        LocalDate compareLocalDate = LocalDate.now();

        if (compareLocalDate.getYear() == localDate.getYear() &&
                compareLocalDate.getMonthValue() == localDate.getMonthValue() &&
                compareLocalDate.getDayOfMonth() == localDate.getDayOfMonth()) {
            result = "Today";
            return result;
        } else {
            String dayOfWeek_raw = localDate.getDayOfWeek().toString();
            String dow_raw = dayOfWeek_raw.substring(0, 3).toLowerCase();
            char first_letter_dow = Character.toUpperCase(dow_raw.charAt(0));
            String dow = first_letter_dow + dow_raw.substring(1, dow_raw.length());
            String dayOfMonth = Integer.toString(localDate.getDayOfMonth());
            String month_raw = localDate.getMonth().toString();
            String month_raw_2 = month_raw.substring(0, 3).toLowerCase();
            char first_letter = Character.toUpperCase(month_raw_2.charAt(0));
            String month = first_letter + month_raw_2.substring(1, month_raw_2.length());
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
        date = localDateToString(localDate, FormatDateOutput.DEFAULT);
        task_date = localDateToTaskString(localDate);
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
        result = date + " " + time;
        return result;
    }

    public String getTaskDate() {
        return task_date;
    }

    public String getTaskTime() {
        return hour + ":" + minute;
    }

    public String convertStringToLocalDate(String raw_date, FormatDateOutput formatDateOutput) {
        String formatted_sdf_day = raw_date.substring(0, 2);
        String formatted_sdf_month = raw_date.substring(3, 5);
        String formatted_sdf_year = raw_date.substring(6, raw_date.length());

        localDate = LocalDate.of(Integer.parseInt(formatted_sdf_year),
                                           Integer.parseInt(formatted_sdf_month),
                                           Integer.parseInt(formatted_sdf_day));

        return localDateToString(localDate, formatDateOutput);
    }

    public void convertStringToLocalDate(String raw_date, String raw_time) {
        String formatted_sdf_day = raw_date.substring(0, 2);
        String formatted_sdf_month = raw_date.substring(3, 5);
        String formatted_sdf_year = raw_date.substring(6, raw_date.length());

        String formatted_sdf_hour = raw_time.substring(0, 2);
        String formatted_sdf_minute = raw_time.substring(3, 5);

        int_hour = Integer.parseInt(formatted_sdf_hour);
        int_minute = Integer.parseInt(formatted_sdf_minute);

        localDate = LocalDate.of(Integer.parseInt(formatted_sdf_year),
                Integer.parseInt(formatted_sdf_month),
                Integer.parseInt(formatted_sdf_day));
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
}
