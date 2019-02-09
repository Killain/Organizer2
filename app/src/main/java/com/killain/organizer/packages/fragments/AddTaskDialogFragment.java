package com.killain.organizer.packages.fragments;

import android.annotation.SuppressLint;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;

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
import android.widget.Toast;

import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.enums.DialogType;
import com.killain.organizer.packages.enums.FormatDateOutput;
import com.killain.organizer.packages.interactors.TaskInteractor;
import com.killain.organizer.packages.recyclerview_adapters.RVAHelper;
import com.killain.organizer.packages.interactors.DateHelper;
import com.killain.organizer.packages.ui_tools.DatePicker;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;

import com.killain.organizer.packages.models.Task;
import com.killain.organizer.packages.views.HeaderTextView;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;

import java.util.Calendar;

public class AddTaskDialogFragment extends BottomSheetDialogFragment implements
        View.OnClickListener,
        FragmentUIHandler {

    public Context context;
    private EditText user_txt;
    private Calendar time_calendar;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat sdf_time;
    private DataManager dataManager;
    public RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RVAHelper rvaHelper;
    private TextView cal_txt_btn;
    private FragmentUIHandler fragmentUIHandler;
    private Task task;
    private String cal_btn_preview;
    private DateHelper dateHelper;
    private DialogType dialogType;
    private HeaderTextView headerTextView;
    private LocalDate localDate;
    private String oldTaskText;

    public AddTaskDialogFragment() { }

    public static AddTaskDialogFragment newInstance(){
        return new AddTaskDialogFragment();
    }

    public void setListener(FragmentUIHandler fragmentUIHandler) {
        this.fragmentUIHandler = fragmentUIHandler;
    }
    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time_calendar = Calendar.getInstance();
        sdf_time = new SimpleDateFormat("HH:mm");
        localDate = LocalDate.now();
        context = getContext();
        dataManager = new DataManager(getContext(), null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_dialog, container, false);
        headerTextView = view.findViewById(R.id.dialog_textview_header);
        user_txt = view.findViewById(R.id.task_edit_text);
        user_txt.setVerticalScrollBarEnabled(false);
        cal_txt_btn = view.findViewById(R.id.dialog_txt_cal_btn);
        relativeLayout = view.findViewById(R.id.relative_layout_dialog);
        recyclerView = view.findViewById(R.id.recyclerView_dialog);
        rvaHelper = new RVAHelper(context, this);
        rvaHelper.setListener(this);
        recyclerView.setAdapter(rvaHelper);

        if (dialogType == DialogType.ADD_NEW_TASK) {
            headerTextView.setText("Set a new task");
        } else {
            headerTextView.setText("Edit task");
            user_txt.setText(task.getTask_string());
//            dateHelper.setDateAndTime(task.getDate(), task.getTime());
            if (cal_txt_btn != null) {
                cal_txt_btn.setText(cal_btn_preview);
            }

            if (cal_txt_btn != null) {
                user_txt.setText(task.getTask_string());
                oldTaskText = task.getTask_string();
            }

            if (rvaHelper != null) {
                rvaHelper.loadSubtasksByReference(task.getTask_string());
            }
        }

        TextView add_element = view.findViewById(R.id.add_element_dialog);

        ImageButton set = view.findViewById(R.id.save_task_btn);

        cal_txt_btn.setText(cal_btn_preview);
        cal_txt_btn.setOnClickListener(this);
        add_element.setOnClickListener(this);
        set.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.save_task_btn:

                    if (user_txt.getText().toString().equals("") ||
                            user_txt.getText().toString().equals(" ") ||
                            user_txt.getText().toString().trim().length() <= 0) {

                        Toast.makeText(getContext(), "Task is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                            TaskInteractor taskInteractor = new TaskInteractor(context);
                            task = taskInteractor.createTask(user_txt.getText().toString(),
                                    dateHelper.getLong(),
                                    sdf_time.format(time_calendar.getTime()),
                                    false, false,
                                    rvaHelper.getArrayList(), dialogType);
                        if (dialogType == DialogType.ADD_NEW_TASK) {
                            dataManager.addTask(task);
                        } else if (dialogType == DialogType.EDIT_EXISTING_TASK) {
                            dataManager.customUpdateTask(task, oldTaskText);
                        }

                        fragmentUIHandler.refreshAdapterOnAdd(1, AdapterRefreshType.RELOAD_FROM_DB);
                        changeToParentFragment();
                    }
                    break;

            case R.id.cancel_btn:
                changeToParentFragment();
                break;

            case R.id.dialog_txt_cal_btn:

                DatePicker datePicker = new DatePicker(getContext(), null,
                        dateHelper.getYear(),
                        dateHelper.getMonth(),
                        dateHelper.getDayOfMonth());

                          datePicker.getDatePicker().init(
                                  dateHelper.getYear(),
                                  dateHelper.getMonth() - 1,
                                  dateHelper.getDayOfMonth(),
                                  (view1, year, monthOfYear, dayOfMonth) ->
                                          dateHelper.setDate(year, monthOfYear + 1, dayOfMonth));
                datePicker.show();

                datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> {
                            timePickerDialog = new TimePickerDialog(AddTaskDialogFragment.this.getContext(),
                                    (view12, hourOfDay, minute) -> {
                                        time_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        time_calendar.set(Calendar.MINUTE, minute);
                                        dateHelper.setTime(hourOfDay, minute);
                                        cal_btn_preview = dateHelper.getFullDateWithTime();
                                        cal_txt_btn.setText(cal_btn_preview);
                                    },
                                    time_calendar.get(Calendar.HOUR_OF_DAY),
                                    time_calendar.get(Calendar.MINUTE), true);
                            timePickerDialog.show();
                        });
                break;

            case R.id.add_element_dialog:
                rvaHelper.addToRV();
                break;

            default:
                break;
        }
    }

    private void changeToParentFragment() {
        dismiss();
    }

    @Override
    public View getBackground() {
        return null;
    }

    //Method for refreshing and updating RecyclerView when new item is added
    @Override
    public void refreshAdapterOnAdd(int position, AdapterRefreshType refreshType) {
        if (refreshType == AdapterRefreshType.RELOAD_FROM_DB) {
            rvaHelper.notifyDataSetChanged();
        } else {
            rvaHelper.notifyItemInserted(position);
        }
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        rvaHelper.notifyItemRemoved(position);
    }

    public void setDate(LocalDate localDate) {
        dateHelper = new DateHelper();
        this.localDate = localDate;
        dateHelper.setLocalDate(localDate);
        cal_btn_preview = dateHelper.localDateToString(this.localDate, FormatDateOutput.FORMAT_DATE_OUTPUT);
    }

    public void setTaskAndDate(Task task, long date) {
        this.task = task;
        localDate = LocalDate.ofEpochDay(date);
        dateHelper = new DateHelper();
        cal_btn_preview = dateHelper.localDateToString(this.localDate,
                FormatDateOutput.DEFAULT) +
                " " +
                task.getTime();
    }
}
