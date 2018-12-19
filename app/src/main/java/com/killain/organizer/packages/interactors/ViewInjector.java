package com.killain.organizer.packages.interactors;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.killain.organizer.packages.card.CardAdapter;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.task_watcher.TaskWatcher;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;

public class ViewInjector {
    private TextView noTaskTxt;
    private FloatingActionMenu fam;
    public RelativeLayout relative_layout;
    private FloatingActionButton fab_simple_task, fab_big_task;
    private ScrollView scrollView;
    public RecyclerView recyclerView;
}
