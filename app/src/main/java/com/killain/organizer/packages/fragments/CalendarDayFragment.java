package com.killain.organizer.packages.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.interactors.RecyclerViewInteractor;
import com.killain.organizer.packages.interactors.UIInteractor;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import com.killain.organizer.packages.interfaces.OnStartDragListener;

import org.threeten.bp.LocalDate;

public class CalendarDayFragment extends Fragment implements FragmentUIHandler, View.OnClickListener {

    private ItemTouchHelper mItemTouchHelper;
    private FloatingActionButton fab;
    private RVCardAdapter adapter;
    private String todayString;
    private LocalDate localDate;
    private String mMonth;
    private String mDay;
    private String mYear;
    private RecyclerViewInteractor rvInteractor;
    private BottomNavigationView navigationView;
    private View background;
    private UIInteractor uiInteractor;

    public CalendarDayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_calendar_fragment);
        fab = getActivity().findViewById(R.id.cal_frg_add_btn);
        background = getActivity().findViewById(R.id.cal_frg_base_layout);
        navigationView = getActivity().findViewById(R.id.navigation_cal);

//        adapter = new adapter(getContext(), this, this);
//        adapter.loadItemsByDate(todayString);
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setAdapter(adapter);

        uiInteractor = new UIInteractor(background, fab, navigationView);
        rvInteractor = new RecyclerViewInteractor(recyclerView);
        adapter = new RVCardAdapter (getContext(), rvInteractor.getListener(), this);
        rvInteractor.setAdapter(adapter);
        adapter.loadItemsByDate(todayString);
        rvInteractor.bind();

        fab.setOnClickListener(this);

        return view;
    }

//    @Override
//    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
//    }

    @Override
    public View getBackground() {
        return null;
    }

    @Override
    public void refreshAdapterOnAdd(int position, AdapterRefreshType adapterRefreshType) {
        adapter.loadItemsByDate(todayString);
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void setNewAlpha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiInteractor.setNewAlpha();
        }
    }

    @Override
    public void UISwitch() {
        uiInteractor.UISwitch();
    }

    public void reloadTasksOnDate(String date) {
        adapter.loadItemsByDate(date);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setNewAlpha();
        }
        UISwitch();
        Fragment dialog = AddTaskDialogFragment.newInstance();
        ((AddTaskDialogFragment) dialog).setListener(this);
        setSelectedDate(localDate);
        ((AddTaskDialogFragment) dialog).setDate(localDate);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_out);
        transaction.replace(R.id.cal_dialog_frame_layout, dialog);
        transaction.addToBackStack("child");
        transaction.commit();
    }

    public void setSelectedDate(LocalDate localDate) {
        this.localDate = localDate;
        todayString = getConvertedDate(localDate);
    }

    private String getConvertedDate(LocalDate localDate) {
        int secondaryDay = localDate.getDayOfMonth();
        int secondaryMonth = localDate.getMonthValue();

        if (secondaryMonth <= 9) {
            mMonth = "0" + secondaryMonth;
        } else {
            mMonth = Integer.toString(localDate.getMonthValue());
        }

        if (secondaryDay <= 9) {
            mDay = "0" + secondaryDay;
        } else {
            mDay = Integer.toString(localDate.getDayOfMonth());
        }

        mYear = Integer.toString(localDate.getYear());

        return mDay + "/" + mMonth + "/" + mYear;
    }
}