package com.killain.organizer.packages.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNewAlpha();
            fragment.UISwitch();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
//        startService(new Intent(this, NotificationService.class));
        super.onPause();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setNewAlpha() {
        if (fragment.relative_layout.getAlpha() == 1f) {
            //TODO: refers to public variable
            fragment.relative_layout.setAlpha(0.3f);
        } else {
            fragment.relative_layout.setAlpha(1f);
        }
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
