package com.killain.organizer.packages.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.killain.organizer.packages.ToolbarEnum;
import com.killain.organizer.packages.activity.TasksActivity;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.card.CardAdapter;
import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.NotificationInteractor;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.interfaces.OnStartDragListener;

public class TasksFragment extends Fragment implements OnStartDragListener, IAdapterRefresher {

    private int oldScrollYPosition = 0;
    public TextView noTaskTxt;
    public FloatingActionMenu fam;
    public RelativeLayout relative_layout;
    public FloatingActionButton fab_simple_task, fab_big_task;
    public ScrollView scrollView;
    private ItemTouchHelper mItemTouchHelper;
    public RecyclerView recyclerView;
    public CardAdapter cardAdapter;
    public TasksFragment tasksFragmentInstance;
    private Context context;

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

        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);
        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);

        cardAdapter = CardAdapter.newInstance(context, this, tasksFragmentInstance);
        cardAdapter.loadItemsByState();
        new NotificationInteractor(cardAdapter.getArrayList(), getContext());

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(cardAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(cardAdapter);

        fab_simple_task.setOnClickListener(v -> {
            Fragment dialog = new AddTaskDialogFragment();
            ((AddTaskDialogFragment) dialog).setListener(this);
            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.fragment1, dialog);
            transaction.addToBackStack("child");
            transaction.commit();
            fam.close(true);
        });

        fab_big_task.setOnClickListener(v -> {
            Fragment newFragment = new AddBigTaskFragment();
            ((AddBigTaskFragment) newFragment).setListener(this);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.fragment1, newFragment);
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
    public void refreshAdapterOnAdd() {
        cardAdapter.loadItemsByState();
        new NotificationInteractor(cardAdapter.getArrayList(), getContext());
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        cardAdapter.notifyItemRemoved(position);
    }

    public void changeToolbar(ToolbarEnum toolbarEnum) {
        Activity activity = (TasksActivity) getContext();
        ((TasksActivity) activity).setNewToolbar(toolbarEnum);
    }
}