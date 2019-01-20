package com.killain.organizer.packages.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.killain.organizer.R;
import com.killain.organizer.packages.recyclerview_adapters.RVAHelper;
import com.killain.organizer.packages.ui_tools.DatePicker;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.tasks.SubTask;
import com.killain.organizer.packages.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskDialogFragment extends Fragment implements
        View.OnClickListener, IAdapterRefresher {

    public Context context;
    private EditText user_txt;
    public String task_type = null;
    private Calendar day_calendar, time_calendar;
    private TasksFragment _fragment;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat sdf_date, sdf_time;
    private DataManager dataManager;
    public RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RVAHelper rvaHelper;

    public AddTaskDialogFragment() { }

    public static AddTaskDialogFragment newInstance(){
        return new AddTaskDialogFragment();
    }

    public void setListener(TasksFragment fragment) {
        _fragment = fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day_calendar = Calendar.getInstance();
        time_calendar = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("dd/MM/yyyy");
        sdf_time = new SimpleDateFormat("HH:mm");
        context = getContext();
        dataManager = new DataManager(getContext(), null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_dialog, container, false);

        relativeLayout = view.findViewById(R.id.relative_layout_dialog);
        recyclerView = view.findViewById(R.id.recyclerView_dialog);
        rvaHelper = new RVAHelper(context, this);
        rvaHelper.setListener(this);
        recyclerView.setAdapter(rvaHelper);
        TextView add_element = view.findViewById(R.id.add_element_dialog);
        ImageButton datepicker_btn = view.findViewById(R.id.cal_btn);
        ImageButton set = view.findViewById(R.id.save_task_btn);
        user_txt = view.findViewById(R.id.task_edit_text);
        add_element.setOnClickListener(this);
        set.setOnClickListener(this);
        datepicker_btn.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_task_btn:
                Task task = new Task();
                task.setTask_string(user_txt.getText().toString());
                task.setDate(sdf_date.format(day_calendar.getTime()));
                task.setTime(sdf_time.format(time_calendar.getTime()));
                task.setNotificationShowed(false);
                task.setCompleted(false);

                ArrayList<SubTask> subTaskArrayList = rvaHelper.getArrayList();

                if (subTaskArrayList != null) {
                    for (SubTask subTask : subTaskArrayList) {
                        task.setHasReference(true);
                        subTask.setReference(task.getTask_string());
                        dataManager.addSubTask(subTask);
                    }
                }

                dataManager.addTask(task);
                _fragment.refreshAdapterOnAdd(1);
                changeToParentFragment();
                break;

            case R.id.cancel_btn:
                changeToParentFragment();
                break;

            case R.id.cal_btn:

                DatePicker datePicker = new DatePicker(getContext(), null,
                        day_calendar.get(Calendar.YEAR),
                        day_calendar.get(Calendar.MONTH),
                        day_calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().init(
                        day_calendar.get(Calendar.YEAR),
                        day_calendar.get(Calendar.MONTH),
                        day_calendar.get(Calendar.DAY_OF_MONTH),
                        (view1, year, monthOfYear, dayOfMonth) ->
                                day_calendar.set(year, monthOfYear, dayOfMonth));
                datePicker.show();

                datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> {
                    timePickerDialog = new TimePickerDialog(AddTaskDialogFragment.this.getContext(),
                            (view12, hourOfDay, minute) -> {
                                time_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                time_calendar.set(Calendar.MINUTE, minute);
                            },
                            time_calendar.get(Calendar.HOUR_OF_DAY),
                            time_calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                });
                break;

            case R.id.add_element_dialog:
                recyclerView.setVisibility(View.VISIBLE);
                rvaHelper.addToRV();
                break;

            default:
                break;
        }
    }

    private void changeToParentFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _fragment.setNewAlphaInActivity();
            _fragment.UISwitch();
        }
    }

    @Override
    public void refreshAdapterOnAdd(int position) {
        rvaHelper.notifyItemInserted(position);
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        rvaHelper.notifyItemRemoved(position);
    }
}
