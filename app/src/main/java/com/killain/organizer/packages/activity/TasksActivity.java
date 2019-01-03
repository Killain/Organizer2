package com.killain.organizer.packages.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.killain.organizer.R;
import com.killain.organizer.packages.ToolbarEnum;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.interfaces.TaskDAO;

public class TasksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar, custom_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment1 = new Fragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment1, fragment1);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
                setNewToolbar(ToolbarEnum.NAVIGATION_TOOLBAR);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
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
            Intent intent = new Intent(TasksActivity.this, CalendarNavActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_task) {
            drawer.closeDrawer(GravityCompat.START);

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

    public void setNewToolbar(ToolbarEnum toolbarEnum) {

        switch (toolbarEnum) {

            case TASK_DIALOG_TOOLBAR:
                toolbar.setVisibility(View.GONE);
                custom_toolbar = findViewById(R.id.custom_toolbar);
                custom_toolbar.setVisibility(View.VISIBLE);
                break;

            case NAVIGATION_TOOLBAR:
                custom_toolbar.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }
}
