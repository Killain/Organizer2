package com.killain.organizer.packages.ui_tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    String day, month, result;

    public DateHelper() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_month = new SimpleDateFormat("MM");
        day = sdf_day.format(date);
        month = sdf_month.format(date);
    }

    public String getFormattedDate() {
        formatMonth();
        result = "Today, " + day + " " + formatMonth();
        return result;
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
}
