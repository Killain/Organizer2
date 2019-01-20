package com.killain.organizer.packages.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    public FrameLayout tasks_frame_layout, dialog_frame_layout;
    public FloatingActionButton fab_simple_task, fab_big_task;
    private BottomNavigationView bottomNavigationView;
    public ScrollView scrollView;
    private ItemTouchHelper mItemTouchHelper;
    public RecyclerView recyclerView;
    public RVCardAdapter RVCardAdapter;
    public TasksFragment tasksFragmentInstance;
    private Context context;
    private boolean isAlphaSet = false;
    private View fragment_wrapper;
    private TasksActivity activity;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
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

        fam = getActivity().findViewById(R.id.fam_tasks);

        fab_simple_task = getActivity().findViewById(R.id.fab_simple_task);
        fab_big_task = getActivity().findViewById(R.id.fab_big_task);
        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        tasks_frame_layout = getActivity().findViewById(R.id.content_frame_layout);
        dialog_frame_layout = getActivity().findViewById(R.id.dialog_frame_layout);

        HeaderTextView today_txt_view = RootView.findViewById(R.id.tasks_frg_today);

        DateHelper dateHelper = new DateHelper();
        today_txt_view.setText(dateHelper.getFormattedDate());

        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);

        activity = (TasksActivity) getContext();
        if (activity != null) {
//            activity.setTasksRL(relative_layout);
//            activity.setTasksFragmentInstance(tasksFragmentInstance);
        }

        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);
        recyclerView.setNestedScrollingEnabled(false);

        RVCardAdapter = com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter.newInstance
                (context, this, tasksFragmentInstance);
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
            UISwitch();
            Fragment dialog = AddTaskDialogFragment.newInstance();
            ((AddTaskDialogFragment) dialog).setListener(this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.dialog_frame_layout, dialog);
            transaction.addToBackStack("child");
            transaction.commit();
        });

        fab_big_task.setOnClickListener(v -> {
            Fragment newFragment = new AddBigTaskFragment();
            ((AddBigTaskFragment) newFragment).setListener(this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.content_frame_layout, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            fam.close(true);
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollView.getScrollY() > oldScrollYPosition) {
                fam.hideMenu(true);
            } else if (scrollView.getScrollY() < oldScrollYPosition || scrollView.getScrollY() <= 0) {
                fam.showMenu(true);
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
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        RVCardAdapter.notifyItemRemoved(position);
    }

    public void setNewAlphaInActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.setNewAlpha();
        }
    }

    public void UISwitch() {
        if (bottomNavigationView.isShown()) {
            fam.setVisibility(View.GONE);
            fam.hideMenu(false);
            bottomNavigationView.setVisibility(View.GONE);
        } else if (!bottomNavigationView.isShown()) {
            fam.setVisibility(View.VISIBLE);
            fam.showMenu(true);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}