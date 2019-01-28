package com.killain.organizer.packages.ui_tools;

import org.threeten.bp.LocalDate;

public class DateHelper {

    private String day, month, result, time, date, minute, hour;
    private LocalDate localDate;

    public DateHelper() {

    }

//    public String getFormattedDate() {
//        formatMonth();
//        result = "Today, " + day + " " + formatMonth();
//        return result;
//    }

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

    public String localDateToString(LocalDate localDate) {
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

    public void setDate(int year, int month, int day ) {
        localDate = LocalDate.of(year, month, day);
        date = localDateToString(localDate);
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

    public String convertStringToLocalDate(String raw_date) {
        String formatted_sdf_day = raw_date.substring(0, 2);
        String formatted_sdf_month = raw_date.substring(3, 5);
        String formatted_sdf_year = raw_date.substring(6, raw_date.length());

        localDate = LocalDate.of(Integer.parseInt(formatted_sdf_year),
                                           Integer.parseInt(formatted_sdf_month),
                                           Integer.parseInt(formatted_sdf_day));

        return localDateToString(localDate);
    }
}
