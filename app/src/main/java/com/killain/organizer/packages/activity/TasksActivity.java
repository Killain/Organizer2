package com.killain.organizer.packages.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.services.NotificationService;

public class TasksActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private Toolbar toolbar, custom_toolbar;
    private RelativeLayout relativeLayout;
    private boolean isAlphaSet = false;
    private TasksFragment tasksFragmentInstance, fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        fragment1 = new TasksFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame_layout, fragment1);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        startService(new Intent(this, NotificationService.class));
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNewAlpha();
            fragment1.UISwitch();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        startService(new Intent(this, NotificationService.class));
        super.onPause();
    }

//    public void setTasksRL(RelativeLayout relativeLayout) {
//        this.relativeLayout = relativeLayout;
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setNewAlpha() {
        if (!isAlphaSet) {
            //TODO: refers to public variable
            fragment1.relative_layout.setAlpha(0.3f);
            isAlphaSet = true;
        } else {
            fragment1.relative_layout.setAlpha(1f);
            isAlphaSet = false;
        }
    }

//    public void setTasksFragmentInstance(TasksFragment tasksFragmentInstance) {
//        this.tasksFragmentInstance = tasksFragmentInstance;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar_fragment_bottom_nav:
                Intent intent = new Intent(TasksActivity.this, CalendarNavActivity.class);
                TasksActivity.this.startActivity(intent);
                break;

                default: break;
        }

        return true;
    }
}
