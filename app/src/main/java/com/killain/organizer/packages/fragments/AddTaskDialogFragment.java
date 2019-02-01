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
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.enums.FormatDateOutput;
import com.killain.organizer.packages.interactors.TaskInteractor;
import com.killain.organizer.packages.recyclerview_adapters.RVAHelper;
import com.killain.organizer.packages.interactors.DateHelper;
import com.killain.organizer.packages.ui_tools.DatePicker;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import com.killain.organizer.packages.models.SubTask;
import com.killain.organizer.packages.models.Task;

import org.threeten.bp.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskDialogFragment extends Fragment implements
        View.OnClickListener,
        FragmentUIHandler {

    public Context context;
    private EditText user_txt;
    private Calendar day_calendar, time_calendar;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat sdf_date, sdf_time;
    private DataManager dataManager;
    public RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RVAHelper rvaHelper;
    private TextView cal_txt_btn;
    FragmentUIHandler fragmentUIHandler;
    private LocalDate date;
    private TaskInteractor taskInteractor;
    private GestureDetector gestureDetector;
    Task task;
    private String formatted_date, cal_btn_preview;
    private DateHelper dateHelper;

    public AddTaskDialogFragment() { }

    public static AddTaskDialogFragment newInstance(){
        return new AddTaskDialogFragment();
    }

    public void setListener(FragmentUIHandler fragmentUIHandler) {
        this.fragmentUIHandler = fragmentUIHandler;
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

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.custom_dialog, container, false);

        relativeLayout = view.findViewById(R.id.relative_layout_dialog);
        recyclerView = view.findViewById(R.id.recyclerView_dialog);
        cal_txt_btn = view.findViewById(R.id.dialog_txt_cal_btn);
        rvaHelper = new RVAHelper(context, this);
        rvaHelper.setListener(this);
        recyclerView.setAdapter(rvaHelper);
        TextView add_element = view.findViewById(R.id.add_element_dialog);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("STASYAN", "ONTOUCH");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });



        ImageButton set = view.findViewById(R.id.save_task_btn);
        user_txt = view.findViewById(R.id.task_edit_text);
        user_txt.setVerticalScrollBarEnabled(false);
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

                if (formatted_date == null || formatted_date.equals("")) {
                    task = new Task();
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
                } else {
                    task = new Task();
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
                }

                dataManager.addTask(task);
                fragmentUIHandler.refreshAdapterOnAdd(1, AdapterRefreshType.RELOAD_FROM_DB);
                changeToParentFragment();
                break;

            case R.id.cancel_btn:
                changeToParentFragment();
                break;

            case R.id.dialog_txt_cal_btn:

                DatePicker datePicker = new DatePicker(getContext(), null,
                        date.getYear(),
                        date.getMonthValue() - 1,
                        date.getDayOfMonth());

                dateHelper.setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

                datePicker.getDatePicker().init(
                        date.getYear(),
                        date.getMonthValue() - 1,
                        date.getDayOfMonth(),
                        new android.widget.DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(android.widget.DatePicker view1, int year, int monthOfYear, int dayOfMonth) {
                                day_calendar.set(year, monthOfYear, dayOfMonth);
                                dateHelper.setDate(year, monthOfYear + 1, dayOfMonth);
                            }
                        });
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
            fragmentUIHandler.setNewAlpha();
            fragmentUIHandler.UISwitch();
        }
    }

    @Override
    public View getBackground() {
        return null;
    }

    @Override
    public void refreshAdapterOnAdd(int position, AdapterRefreshType refreshType) {
        rvaHelper.notifyItemInserted(position);
    }

    @Override
    public void refreshAdapterOnDelete(int position) {
        rvaHelper.notifyItemRemoved(position);
    }

    @Override
    public void setNewAlpha() {
        //Useless here. Just leave it as it is.
    }

    @Override
    public void UISwitch() {
        //Useless here. Just leave it as it is.
    }

    public void setDate(LocalDate date) {
        dateHelper = new DateHelper();
        this.date = date;
        cal_btn_preview = dateHelper.localDateToString(date, FormatDateOutput.FORMAT_DATE_OUTPUT);
        formatted_date = getConvertedDateString(date);
    }

    private String getConvertedDateString(LocalDate localDate) {

        int secondaryDay = localDate.getDayOfMonth();
        int secondaryMonth = localDate.getMonthValue();

        String mMonth;
        if (secondaryMonth <= 9) {
            mMonth = "0" + secondaryMonth;
        } else {
            mMonth = Integer.toString(localDate.getMonthValue());
        }

        String mDay;
        if (secondaryDay <= 9) {
            mDay = "0" + secondaryDay;
        } else {
            mDay = Integer.toString(localDate.getDayOfMonth());
        }

        String mYear = Integer.toString(localDate.getYear());

        return mDay + "/" + mMonth + "/" + mYear;
    }
}
