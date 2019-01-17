package com.killain.organizer.packages.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.killain.organizer.packages.activity.TasksActivity;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;
import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.NotificationInteractor;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.ui_tools.DateHelper;
import com.killain.organizer.packages.views.HeaderTextView;

public class TasksFragment extends Fragment implements OnStartDragListener, IAdapterRefresher {

    private int oldScrollYPosition = 0;
    public TextView noTaskTxt;
    public FloatingActionMenu fam;
    public RelativeLayout relative_layout;
    public FloatingActionButton fab_simple_task, fab_big_task;
    public ScrollView scrollView;
    private ItemTouchHelper mItemTouchHelper;
    public RecyclerView recyclerView;
    public RVCardAdapter RVCardAdapter;
    public TasksFragment tasksFragmentInstance;
    private Context context;
    private boolean isAlphaSet = false;
    private HeaderTextView today_txt_view;
    private View vertical_line;
    private TasksActivity a;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        TasksFragment tasksFragment = new TasksFragment();
        return tasksFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        tasksFragmentInstance = TasksFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        noTaskTxt = RootView.findViewById(R.id.no_task_txt);

        FloatingActionMenu fam = RootView.findViewById(R.id.fam_tasks);

        fab_simple_task = RootView.findViewById(R.id.fab_simple_task);
        fab_big_task = RootView.findViewById(R.id.fab_big_task);
        vertical_line = RootView.findViewById(R.id.tasks_frg_vertical_line);

        today_txt_view = RootView.findViewById(R.id.tasks_frg_today);

        DateHelper dateHelper = new DateHelper();
        today_txt_view.setText(dateHelper.getFormattedDate());

        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);

        a = (TasksActivity) getContext();
        if (a != null) {
            a.setTasksRL(relative_layout);
        }

        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.bringToFront();

        RVCardAdapter = RVCardAdapter.newInstance(context, this, tasksFragmentInstance);
        RVCardAdapter.loadItemsByState();
        new NotificationInteractor(RVCardAdapter.getArrayList(), getContext());

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(RVCardAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(RVCardAdapter);

        fab_simple_task.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setNewAlphaInActivity();
            }

            Fragment dialog = AddTaskDialogFragment.newInstance();
            ((AddTaskDialogFragment) dialog).setListener(this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.tasks_frg, dialog);
            transaction.addToBackStack("child");
            transaction.commit();
            fam.close(true);
        });

        fab_big_task.setOnClickListener(v -> {
            Fragment newFragment = new AddBigTaskFragment();
            ((AddBigTaskFragment) newFragment).setListener(this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.tasks_frg, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            fam.close(true);
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollView.getScrollY() > oldScrollYPosition) {
                fam.hideMenu(true);
//                    fam.showMenu(true);
            } else if (scrollView.getScrollY() < oldScrollYPosition || scrollView.getScrollY() <= 0) {
                fam.showMenu(true);
//                    fam.hideMenu(true);
            }
            oldScrollYPosition = scrollView.getScrollY();
        });

        return RootView;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void refreshAdapterOnAdd(int position) {
        RVCardAdapter.loadItemsByState();
//        new NotificationInteractor(RVCardAdapter.getArrayList(), getContext());
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        RVCardAdapter.notifyItemRemoved(position);
    }

//    public void changeToolbar(ToolbarEnum toolbarEnum) {
//        Activity activity = (TasksActivity) getContext();
//        ((TasksActivity) activity).setNewToolbar(toolbarEnum);
//    }

    public void setNewAlphaInActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            a.setNewAlpha();
        }
    }
}