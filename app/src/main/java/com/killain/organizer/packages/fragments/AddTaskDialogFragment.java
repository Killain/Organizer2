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
    private ImageButton label, datepicker_btn, set;
    public String task_type = null;
    private Calendar day_calendar, time_calendar;
    private int group_tag = 0;
    private TasksFragment _fragment;
    private DatePicker datePicker;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat sdf_date, sdf_time;
    private DataManager dataManager;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RVAHelper rvaHelper;
    private ArrayList<SubTask> subTaskArrayList;

    public AddTaskDialogFragment() { }

    public static AddTaskDialogFragment newInstance(){
        return new AddTaskDialogFragment();
    }

    public void setListener(TasksFragment fragment) {
        _fragment = fragment;
    }

    @Override
    public void onResume() {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(getDialog().getWindow().getAttributes());
//        lp.height = height / 2;
//        lp.width = width;
//
//        getDialog().getWindow().setAttributes(lp);
        super.onResume();
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
        datepicker_btn = view.findViewById(R.id.cal_btn);
        set = view.findViewById(R.id.save_task_btn);
        user_txt = view.findViewById(R.id.task_edit_text);
        label = view.findViewById(R.id.label_btn);
        add_element.setOnClickListener(this);
        set.setOnClickListener(this);
        datepicker_btn.setOnClickListener(this);
        label.setOnClickListener(v -> showGroupPopup(v));

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_task_btn:
                Task task = new Task();
                task.setTask_string(user_txt.getText().toString());
                task.setGroup_tag(group_tag);
                task.setDate(sdf_date.format(day_calendar.getTime()));
                task.setTime(sdf_time.format(time_calendar.getTime()));
                task.setNotificationShowed(false);
                task.setCompleted(false);

                subTaskArrayList = rvaHelper.getArrayList();

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

                datePicker = new DatePicker(getContext(), null,
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

    public void showGroupPopup(View view) {
//        PopupMenu popupMenu = new PopupMenu(getContext(), view);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.inflate(R.menu.group_task_popup_menu);
//        popupMenu.show();
    }

    private void changeToParentFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _fragment.setNewAlphaInActivity();
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
