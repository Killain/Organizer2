package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.enums.DialogType;
import com.killain.organizer.packages.interactors.RecyclerViewInteractor;
import com.killain.organizer.packages.interactors.UIInteractor;
import com.killain.organizer.packages.models.Task;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;
import com.killain.organizer.R;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import org.threeten.bp.LocalDate;

import java.util.Calendar;

public class TasksFragment extends Fragment implements FragmentUIHandler {

    private int oldScrollYPosition = 0;
    public TextView noTaskTxt;
    public RelativeLayout relative_layout;
    public FrameLayout tasks_frame_layout, dialog_frame_layout;
    public FloatingActionButton fab_simple_task;
    private BottomNavigationView bottomNavigationView;
    public ScrollView scrollView;
    public RecyclerView recyclerView;
    public RVCardAdapter adapter;
    public TasksFragment tasksFragmentInstance;
    private UIInteractor uiInteractor;
    private LocalDate localDate;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasksFragmentInstance = TasksFragment.this;
        localDate = LocalDate.now();
    }

    @Override
    public void onStart() {
        if (adapter != null) {
            refreshAdapterOnAdd(0, AdapterRefreshType.RELOAD_FROM_DB);
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (adapter != null) {
            refreshAdapterOnAdd(0, AdapterRefreshType.RELOAD_FROM_DB);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        noTaskTxt = RootView.findViewById(R.id.no_task_txt);
        fab_simple_task = getActivity().findViewById(R.id.fab_simple_task);
        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        tasks_frame_layout = getActivity().findViewById(R.id.content_frame_layout);
        dialog_frame_layout = getActivity().findViewById(R.id.dialog_frame_layout);
        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);
        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);

        uiInteractor = new UIInteractor(relative_layout, fab_simple_task, bottomNavigationView);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerViewInteractor rvInteractor = new RecyclerViewInteractor(recyclerView);
        adapter = new RVCardAdapter (getContext(), rvInteractor.getListener(), this);
        rvInteractor.setAdapter(adapter);
        adapter.loadItemsByState();
        adapter.setParentFragment(tasksFragmentInstance);
        rvInteractor.bind();

        fab_simple_task.setOnClickListener(v -> {
            AddTaskDialogFragment dialog = new AddTaskDialogFragment();
            dialog.setDialogType(DialogType.ADD_NEW_TASK);
            dialog.setListener(this);
            dialog.setDate(localDate);
            if (getFragmentManager() != null) {
                dialog.show(getFragmentManager(), "dialog");
            }
            tasksFragmentInstance.onPause();
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollView.getScrollY() > oldScrollYPosition) {
                fab_simple_task.hide(true);
            } else if (scrollView.getScrollY() < oldScrollYPosition || scrollView.getScrollY() <= 0) {
                fab_simple_task.show(true);
            }
            oldScrollYPosition = scrollView.getScrollY();
        });

        return RootView;
    }

    @Override
    public View getBackground() {
        return relative_layout;
    }

    @Override
    public void refreshAdapterOnAdd(int position, AdapterRefreshType refreshType) {
        if (refreshType == AdapterRefreshType.RELOAD_FROM_DB) {
            adapter.loadItemsByState();
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemInserted(position);
        }
    }

    public void callDialogFragment(DialogType dialogType,
                                   FragmentUIHandler fragmentUIHandler,
                                   long date,
                                   Task task) {

        AddTaskDialogFragment dialog = new AddTaskDialogFragment();
        dialog.setDialogType(dialogType);
        dialog.setTaskAndDate(task, date);
        dialog.setListener(fragmentUIHandler);
        if (getFragmentManager() != null) {
            dialog.show(getFragmentManager(), "dialog");
        }
        tasksFragmentInstance.onPause();
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onDestroy() {
        adapter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        adapter.onDestroy();
        super.onPause();
    }
}