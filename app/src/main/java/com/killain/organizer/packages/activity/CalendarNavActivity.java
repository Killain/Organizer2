package com.killain.organizer.packages.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.CalendarDayFragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarNavActivity extends AppCompatActivity implements OnDateSelectedListener, AppBarLayout.OnOffsetChangedListener {

    private CalendarDayFragment fragment;
    private MaterialCalendarView main_calendarView, small_calendarView;
    public AppBarLayout appBarLayout;

    private String mYear;
    private String mMonth;
    private String mDay;

    private long long_date;
    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String newdate;
    private int secondaryMonth;
    private int secondaryDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_calendar_nav);

//        Toolbar toolbar = findViewById(R.id.toolbar);

        main_calendarView = findViewById(R.id.main_backdrop);
        small_calendarView = findViewById(R.id.small_calendarView);

        appBarLayout = findViewById(R.id.main_appbar);

        fragment = new CalendarDayFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.cal_frg_frame_layout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        appBarLayout.addOnOffsetChangedListener(this);

        main_calendarView.setOnDateChangedListener(this);
        LocalDate localDate = LocalDate.now();
        main_calendarView.setCurrentDate(localDate);
        main_calendarView.setSelectedDate(localDate);
        small_calendarView.setVisibility(View.GONE);
        small_calendarView.setAlpha(0.0f);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

//    @Override
//    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//
//        mYear = Integer.toString(year);
//        secondaryMonth = month + 1;
//        secondaryDay = dayOfMonth;
//        mMonth = Integer.toString(secondaryMonth);
//        mDay = Integer.toString(dayOfMonth);
////
//        if (secondaryMonth <= 9) {
//            mMonth = "0" + secondaryMonth;
//        }
//
//        if (dayOfMonth <= 9) {
//            mDay = "0" + secondaryDay;
//        }
//
//        newdate = mDay + "/" + mMonth + "/" + mYear;
//
//        fragment.reloadTasksOnDate(newdate);
//    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {
        newdate = getConvertedDate(calendarDay);
        fragment.reloadTasksOnDate(newdate);
    }

    private String getConvertedDate(CalendarDay calendarDay) {
        LocalDate localDate = calendarDay.getDate();
        secondaryDay = localDate.getDayOfMonth();
        secondaryMonth = localDate.getMonthValue();

        if (secondaryMonth <= 9) {
            mMonth = "0" + secondaryMonth;
        } else {
            mMonth = Integer.toString(localDate.getMonthValue());
        }

        if (secondaryDay <= 9) {
            mDay = "0" + secondaryDay;
        } else {
            mDay = Integer.toString(localDate.getDayOfMonth());
        }

        mYear = Integer.toString(localDate.getYear());

        String converted = mDay + "/" + mMonth + "/" + mYear;
        return converted;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
        {
            //  Collapsed
            small_calendarView.setVisibility(View.VISIBLE);
            small_calendarView.animate().alpha(1.0f);
        }
        else
        {
            //Expanded
            small_calendarView.setVisibility(View.GONE);
            small_calendarView.animate().alpha(0.0f);

        }

    }
}
