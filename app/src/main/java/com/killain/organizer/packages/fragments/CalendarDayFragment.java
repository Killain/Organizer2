package com.killain.organizer.packages.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.enums.DialogType;
import com.killain.organizer.packages.interactors.RecyclerViewInteractor;
import com.killain.organizer.packages.interactors.UIInteractor;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import org.threeten.bp.LocalDate;

public class CalendarDayFragment extends Fragment implements FragmentUIHandler, View.OnClickListener {

    private ItemTouchHelper mItemTouchHelper;
    private FloatingActionButton fab;
    private RVCardAdapter adapter;
    private LocalDate localDate;
    private RecyclerViewInteractor rvInteractor;
    private BottomNavigationView navigationView;
    private RelativeLayout background;
    public UIInteractor uiInteractor;

    public Context mContext;
    public RecyclerView recyclerView;
    public ScrollView scrollView;

    public CalendarDayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        if (adapter != null) {
            refreshAdapterOnAdd(1, AdapterRefreshType.RELOAD_FROM_DB);
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        if (adapter != null) {
            refreshAdapterOnAdd(1, AdapterRefreshType.RELOAD_FROM_DB);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);

        recyclerView = view.findViewById(R.id.recycler_calendar_fragment);
        fab = getActivity().findViewById(R.id.cal_frg_add_btn);
        background = view.findViewById(R.id.cal_fragment_parent_layout);
        navigationView = getActivity().findViewById(R.id.navigation_cal);
        mContext = getContext();
        scrollView = view.findViewById(R.id.cal_fragment_scroll_view);

        uiInteractor = new UIInteractor(background, fab, navigationView);
        rvInteractor = new RecyclerViewInteractor(recyclerView);
        adapter = new RVCardAdapter (getContext(), rvInteractor.getListener(), this);
        rvInteractor.setAdapter(adapter);
        adapter.loadItemsByDate(localDate.toEpochDay());
        rvInteractor.bind();
        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public View getBackground() {
        return getActivity().findViewById(R.id.cal_frg_wrapper_layout);
    }

    @Override
    public void refreshAdapterOnAdd(int position, AdapterRefreshType adapterRefreshType) {
        adapter.loadItemsByDate(localDate.toEpochDay());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        adapter.notifyItemRemoved(position);
    }

    public void reloadTasksOnDate(long date) {
        adapter.loadItemsByDate(date);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        try {
            AddTaskDialogFragment dialog = new AddTaskDialogFragment();
            dialog.setListener(this);
            dialog.setDialogType(DialogType.ADD_NEW_TASK);
            dialog.setDate(localDate);
            if (getFragmentManager() != null) {
                dialog.show(getFragmentManager(), "dialog");
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setCalendar(LocalDate localDate) {
        this.localDate = localDate;
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