package com.killain.organizer.packages.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.CalendarDayFragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.util.Calendar;

public class CalendarNavActivity extends AppCompatActivity
        implements OnDateSelectedListener,
        AppBarLayout.OnOffsetChangedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private CalendarDayFragment fragment;
    private MaterialCalendarView main_calendarView, small_calendarView;
    public AppBarLayout appBarLayout;
    public BottomNavigationView bottomNavigationView;
    private Calendar calendar;

    private String mYear;
    private String mMonth;
    private String mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_nav);

        main_calendarView = findViewById(R.id.main_backdrop);
        small_calendarView = findViewById(R.id.small_calendarView);
        bottomNavigationView = findViewById(R.id.navigation_cal);
        bottomNavigationView.setSelectedItemId(R.id.calendar_fragment_bottom_nav);
        appBarLayout = findViewById(R.id.main_appbar);

        //task = 1549411200000

        calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.getTimeInMillis();

        LocalDate localDate = LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        fragment = new CalendarDayFragment();
        fragment.setCalendar(calendar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.cal_frg_frame_layout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        appBarLayout.addOnOffsetChangedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        main_calendarView.setOnDateChangedListener(this);
        small_calendarView.setOnDateChangedListener(this);

        main_calendarView.setCurrentDate(localDate);
        main_calendarView.setSelectedDate(localDate);

        small_calendarView.setCurrentDate(localDate);
        small_calendarView.setSelectedDate(localDate);
        small_calendarView.setVisibility(View.GONE);
        small_calendarView.setAlpha(0.0f);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {
        String date = getConvertedDate(calendarDay);
        calendar.set(Calendar.DAY_OF_MONTH, calendarDay.getDay());
        calendar.set(Calendar.MONTH, calendarDay.getMonth());
        calendar.set(Calendar.YEAR, calendarDay.getYear());
        small_calendarView.setSelectedDate(calendarDay);
        main_calendarView.setSelectedDate(calendarDay);
        fragment.setCalendar(calendar);
        fragment.reloadTasksOnDate(calendar.getTimeInMillis());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragment.setNewAlpha();
            fragment.UISwitch();
        }
        super.onBackPressed();
    }

    private String getConvertedDate(CalendarDay calendarDay) {
        LocalDate localDate = calendarDay.getDate();
        int secondaryDay = localDate.getDayOfMonth();
        int secondaryMonth = localDate.getMonthValue();

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

        return mDay + "/" + mMonth + "/" + mYear;
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tasks_fragment_bottom_nav:
                Intent intent = new Intent(CalendarNavActivity.this, TasksActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                CalendarNavActivity.this.startActivity(intent);
                break;

            case R.id.calendar_fragment_bottom_nav:
                break;
        }
        return false;
    }
}