package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.recyclerview_adapters.RVAHelper;
import com.killain.organizer.packages.tasks.SubTask;
import com.killain.organizer.packages.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddBigTaskFragment extends Fragment  {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ImageButton close_btn, save_btn;
    public EditText big_task_title;
    private RelativeLayout rl_add;
    private RecyclerView recyclerView;
    private RVAHelper adapter;
    private ArrayList<SubTask> arrayList;
    private TasksFragment fragment;
    private Calendar day_calendar, time_calendar;

    private String mParam1;
    private String mParam2;

    private DataManager dataManager;

    private SimpleDateFormat sdf_date, sdf_time;

    public AddBigTaskFragment() {
    }

    public static AddBigTaskFragment newInstance(String param1, String param2) {
        AddBigTaskFragment fragment = new AddBigTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        dataManager = new DataManager(getContext(), null);
        day_calendar = Calendar.getInstance();
        time_calendar = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("dd/MM/yyyy");
        sdf_time = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_big_task, container, false);
        close_btn = view.findViewById(R.id.fr_add_big_task_close_btn);
        save_btn = view.findViewById(R.id.save_big_task);
        rl_add = view.findViewById(R.id.rl_add_subtask);
        recyclerView = view.findViewById(R.id.recycler_add_big_task);
        big_task_title = view.findViewById(R.id.big_task_title);

//        adapter = new RVAHelper(getContext());
//        adapter.setListener(this);

        recyclerView.setAdapter(adapter);

        close_btn.setOnClickListener(v -> getFragmentManager().popBackStack());
        rl_add.setOnClickListener(v -> adapter.addToRV());

        save_btn.setOnClickListener(v -> {
            if (big_task_title.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "Title is required", Toast.LENGTH_SHORT).show();
            }
            else
                if (big_task_title.getText().toString().trim().length() > 0) {
                Task task = new Task();
                task.setTitle(big_task_title.getText().toString());
                task.setDate(sdf_date.format(day_calendar.getTime()));
                task.setTime(sdf_time.format(time_calendar.getTime()));
                task.setNotificationShowed(false);
                task.setDeleted(false);
                dataManager.addTask(task);
                adapter.setSubTasksReference(big_task_title.getText().toString());
            }

            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_out, R.anim.slide_up);
            transaction.remove(this);
            getFragmentManager().popBackStack();
            transaction.commit();
            fragment.refreshAdapterOnAdd(1);
        });
        return view;
    }

    public void setListener(TasksFragment fragment) {
        this.fragment = fragment;
    }

    public void refreshAdapterOnAdd(int position) {
        adapter.notifyItemInserted(position);
//        adapter.notifyDataSetChanged();
    }

    public void refreshAdapterOnDelete(int position) {
        adapter.notifyItemRemoved(position);
    }
}
