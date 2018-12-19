package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.killain.organizer.R;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.card.CardAdapter;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;

public class CalendarDayFragment extends Fragment implements OnStartDragListener {

    private static final String ARG_DATE = "year_param";
    private static final String ARG_MONTH = "month_param";
    private static final String ARG_DAY = "day_param";

    private String date_param;
    private String mParam2;
    private String mParam3;

    private ItemTouchHelper mItemTouchHelper;

    private CardAdapter cardAdapter;
    private AppDatabase db;
    private TaskDAO taskDAO;
    private ArrayList<Task> arrayList;
    private CalendarView calendarView;
    private long longDate;
    private RecyclerView recyclerView;

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
        db = AppDatabase.getAppDatabase(getContext());
        taskDAO = db.getTaskDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);

        getTasksByDate();

        recyclerView = view.findViewById(R.id.recycler_calendar_fragment);
        cardAdapter = new CardAdapter(getContext(), arrayList, this, this);
        calendarView = view.findViewById(R.id.main_backdrop);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(cardAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(cardAdapter);

        return view;
    }

    private void getTasksByDate() {
        arrayList = (ArrayList<Task>) taskDAO.getAllTasksByDate(date_param);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void setArgDate(String date) {
        date_param = date;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}