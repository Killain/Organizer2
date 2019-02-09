package com.killain.organizer.packages.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.services.NotificationService;

public class TasksActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TasksFragment fragment;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Thread t = new Thread(() -> {
            //  Initialize SharedPreferences
            SharedPreferences getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            //  Create a new boolean and preference and set it to true
            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
            //  If the activity has never started before...
            if (isFirstStart) {
                //  Launch app intro
                final Intent i = new Intent(TasksActivity.this, IntroActivity.class);
                runOnUiThread(() -> startActivity(i));
                //  Make a new preferences editor
                SharedPreferences.Editor e = getPrefs.edit();
                //  Edit preference to make it false because we don't want this to run again
                e.putBoolean("firstStart", false);
                //  Apply changes
                e.apply();
            }
        });

        // Start the thread
        t.start();

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.tasks_fragment_bottom_nav);

        fragment = new TasksFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame_layout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onResume() {
        navigationView.setSelectedItemId(R.id.tasks_fragment_bottom_nav);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
//        startService(new Intent(this, NotificationService.class));
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar_fragment_bottom_nav:
                Intent intent = new Intent(TasksActivity.this, CalendarNavActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                TasksActivity.this.startActivity(intent);
                break;

                default: break;
        }
        return true;
    }
}
