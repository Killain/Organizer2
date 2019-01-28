package com.killain.organizer.packages.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.interactors.RecyclerViewInteractor;
import com.killain.organizer.packages.interactors.UIInteractor;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;
import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.NotificationInteractor;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import com.killain.organizer.packages.ui_tools.DateHelper;
import com.killain.organizer.packages.views.HeaderTextView;

import org.threeten.bp.LocalDate;

public class TasksFragment extends Fragment implements FragmentUIHandler {

    private int oldScrollYPosition = 0;
    public TextView noTaskTxt;
//    public FloatingActionMenu fam;
    public RelativeLayout relative_layout;
    public FrameLayout tasks_frame_layout, dialog_frame_layout;
    public FloatingActionButton fab_simple_task;
    private BottomNavigationView bottomNavigationView;
    public ScrollView scrollView;
//    private ItemTouchHelper mItemTouchHelper;
    public RecyclerView recyclerView;
    public RVCardAdapter adapter;
    public TasksFragment tasksFragmentInstance;
    private UIInteractor uiInteractor;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasksFragmentInstance = TasksFragment.this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        noTaskTxt = RootView.findViewById(R.id.no_task_txt);
//        fam = getActivity().findViewById(R.id.fam_tasks);
        fab_simple_task = getActivity().findViewById(R.id.fab_simple_task);
        bottomNavigationView = getActivity().findViewById(R.id.navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        tasks_frame_layout = getActivity().findViewById(R.id.content_frame_layout);
        dialog_frame_layout = getActivity().findViewById(R.id.dialog_frame_layout);
        scrollView = RootView.findViewById(R.id.tasks_frg_scroll_view);
        relative_layout = RootView.findViewById(R.id.parent_layout_tasks_fragment);
        recyclerView = RootView.findViewById(R.id.recycler_fragment_tasks);
        HeaderTextView today_txt_view = RootView.findViewById(R.id.tasks_frg_today);

//        DateHelper dateHelper = new DateHelper();
//        today_txt_view.setText(dateHelper.getFormattedDate());

        uiInteractor = new UIInteractor(relative_layout, fab_simple_task, bottomNavigationView);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerViewInteractor rvInteractor = new RecyclerViewInteractor(recyclerView);
        adapter = new RVCardAdapter (getContext(), rvInteractor.getListener(), this);
        rvInteractor.setAdapter(adapter);
        adapter.loadItemsByState();
//        new NotificationInteractor(getContext());
        rvInteractor.bind();

          //TODO: Testing zone
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setAdapter(adapter);

        fab_simple_task.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setNewAlpha();
            }
            UISwitch();
            Fragment dialog = AddTaskDialogFragment.newInstance();
            ((AddTaskDialogFragment) dialog).setListener(this);
            ((AddTaskDialogFragment) dialog).setDate(LocalDate.now());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
            transaction.replace(R.id.dialog_frame_layout, dialog);
            transaction.addToBackStack("child");
            transaction.commit();
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

    @Override
    public void refreshAdapterOnDelete(int position) {
        adapter.notifyItemRemoved(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setNewAlpha() {
        //TODO: Testing
//        if (relative_layout.getAlpha() == 1f) {
//            relative_layout.setAlpha(0.3f);
//        } else {
//            relative_layout.setAlpha(1f);
//        }
        uiInteractor.setNewAlpha();
    }

    public void UISwitch() {
//        if (bottomNavigationView.isShown()) {
//            fam.setVisibility(View.GONE);
//            fam.hideMenu(false);
//            bottomNavigationView.setVisibility(View.GONE);
//        } else if (!bottomNavigationView.isShown()) {
//            fam.setVisibility(View.VISIBLE);
//            fam.showMenu(true);
//            bottomNavigationView.setVisibility(View.VISIBLE);
//        }
        uiInteractor.UISwitch();
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