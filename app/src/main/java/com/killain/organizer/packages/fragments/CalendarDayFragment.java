package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.killain.organizer.R;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.card.CardAdapter;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.interfaces.OnStartDragListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDayFragment extends Fragment implements OnStartDragListener, IAdapterRefresher {

    private static final String ARG_DATE = "year_param";
    private static final String ARG_MONTH = "month_param";
    private static final String ARG_DAY = "day_param";

    private String date_param;
    private String mParam2;
    private String mParam3;

    private ItemTouchHelper mItemTouchHelper;

    private CardAdapter cardAdapter;
    private String todayString;
    private RecyclerView recyclerView;

    private Date date;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public CalendarDayFragment() {
    }

    public static CalendarDayFragment newInstance(String date) {
        CalendarDayFragment fragment = new CalendarDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date_param = getArguments().getString(ARG_DATE);
        }

        date = Calendar.getInstance().getTime();
        todayString = sdf.format(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);

        recyclerView = view.findViewById(R.id.recycler_calendar_fragment);
        cardAdapter = CardAdapter.newInstance(getContext(), this, this);
        cardAdapter.loadItemsByDate(todayString);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(cardAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(cardAdapter);

        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void refreshAdapterOnAdd() {
        cardAdapter.loadItemsByState();
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        cardAdapter.notifyItemRemoved(position);
    }

    public void reloadTasksOnDate(String date) {
        cardAdapter.loadItemsByDate(date);
        cardAdapter.notifyDataSetChanged();
    }
}