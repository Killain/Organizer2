package com.killain.organizer.packages.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.CalendarDayFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarNavActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, NavigationView.OnNavigationItemSelectedListener {

    CalendarDayFragment fragment;
    private CalendarView calendarView;

    String xyear;
    String xmonth;
    String xday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CalendarView calendarView = findViewById(R.id.main_backdrop);

        fragment = (CalendarDayFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_cal_day);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        calendarView.setOnDateChangeListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer, menu); //calendar_nav
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_task) {
            Intent intent = new Intent(CalendarNavActivity.this, TasksActivity.class);
            startActivity(intent);

//        } else if (id == R.id.nav_contacts) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

        xyear = Integer.toString(year);
        xmonth = Integer.toString(month);
        xday = Integer.toString(dayOfMonth);

        long date = view.getDate();
        Date date1 = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        fragment = CalendarDayFragment.newInstance(sdf.format(date1));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_cal_day, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
