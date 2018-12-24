package com.killain.organizer.packages.fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.killain.organizer.R;
import com.killain.organizer.packages.DatePicker;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskDialogFragment extends android.support.v4.app.DialogFragment implements
        View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public Context context;
    private EditText user_txt;
    private Button set, cancel;
    private TextView group_textview, header, header_editable;
    private ImageButton label, datepicker_btn;
    public String task_type = null;
    private Calendar day_calendar, time_calendar;
    private int group_tag = 0;
    private TasksFragment _fragment;
//    private AppDatabase db;
//    private TaskDAO taskDAO;
    private DatePicker datePicker;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat sdf_date, sdf_time;
    private DataManager dataManager;

    public AddTaskDialogFragment() {
    }

    //TODO: передать listener через Bundle
    public void setListener(TasksFragment fragment) {
        _fragment = fragment;
    }


    @Override
    public void onResume() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.height = height / 2;
        lp.width = width;

        getDialog().getWindow().setAttributes(lp);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        db = AppDatabase.getAppDatabase(getContext());
//        taskDAO = db.getTaskDAO();
        day_calendar = Calendar.getInstance();
        time_calendar = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("dd/MM/yyyy");
        sdf_time = new SimpleDateFormat("HH:mm:ss");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_dialog, container, false);

        datepicker_btn = view.findViewById(R.id.cal_btn);
        header = view.findViewById(R.id.dialog_textview_header);
        header_editable = view.findViewById(R.id.dialog_edittext_header);
        set = view.findViewById(R.id.set_btn);
        cancel = view.findViewById(R.id.cancel_btn);
        user_txt = view.findViewById(R.id.task_edit_text);
//        group_textview = view.findViewById(R.id.dialog_group_textview);
        label = view.findViewById(R.id.label_btn);

        switch (task_type) {
            case "simple":
                header_editable.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                break;

            case "big":
                header_editable.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        set.setOnClickListener(this);
        cancel.setOnClickListener(this);
        datepicker_btn.setOnClickListener(this);
        label.setOnClickListener(v -> showGroupPopup(v));
        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.set_btn:
                Task task = new Task();
                task.setTask_string(user_txt.getText().toString());
                task.setGroup_tag(group_tag);
                task.setDate(sdf_date.format(day_calendar.getTime()));
                task.setTime(sdf_time.format(time_calendar.getTime()));
                task.setNotificationShowed(false);
                task.setCompleted(false);
                dataManager = new DataManager(getContext(), null);
                dataManager.addTask(task);
//                taskDAO.addTask(task);
                _fragment.refreshAdapterOnAdd();
//                db.destroyInstance();
                dismiss();
                break;

            case R.id.cancel_btn:
                dismiss();
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

            default:
                break;
        }
    }

    public void showGroupPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.group_task_popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.none_group:
                group_tag = 0;
                group_textview.setText(" ");
                group_textview.setVisibility(View.GONE);
                return true;

            case R.id.important_group:
                group_tag = 1;
                group_textview.setText("Important");
                group_textview.setVisibility(View.VISIBLE);
                return true;

            case R.id.family_group:
                group_tag = 2;
                group_textview.setText("Family");
                group_textview.setVisibility(View.VISIBLE);
                return true;

            case R.id.sport_group:
                group_tag = 3;
                group_textview.setText("Sport");
                group_textview.setVisibility(View.VISIBLE);
                return true;

            case R.id.work_group:
                group_tag = 4;
                group_textview.setText("Work");
                group_textview.setVisibility(View.VISIBLE);
                return true;

        }
        return true;
    }
}
