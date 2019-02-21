package com.killain.organizer.packages.fragments

import android.annotation.SuppressLint
import android.support.design.widget.BottomSheetDialogFragment

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.killain.organizer.R
import com.killain.organizer.packages.enums.AdapterRefreshType
import com.killain.organizer.packages.enums.DialogType
import com.killain.organizer.packages.enums.FormatDateOutput
import com.killain.organizer.packages.interactors.TaskInteractor
import com.killain.organizer.packages.recyclerview_adapters.RVAHelper
import com.killain.organizer.packages.interactors.DateHelper
import com.killain.organizer.packages.ui_tools.DatePicker
import com.killain.organizer.packages.interactors.DataManager
import com.killain.organizer.packages.interfaces.FragmentUIHandler

import com.killain.organizer.packages.models.Task
import com.killain.organizer.packages.views.HeaderTextView

import org.threeten.bp.LocalDate

import java.text.SimpleDateFormat

import java.util.Calendar

class AddTaskDialogFragment : BottomSheetDialogFragment(), View.OnClickListener, FragmentUIHandler {

    private var user_txt: EditText? = null
    private var time_calendar: Calendar? = null
    private var timePickerDialog: TimePickerDialog? = null
    private var sdf_time: SimpleDateFormat? = null
    private var dataManager: DataManager? = null
    var relativeLayout: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var rvaHelper: RVAHelper? = null
    private var cal_txt_btn: TextView? = null
    private var fragmentUIHandler: FragmentUIHandler? = null
    private var task: Task? = null
    private var cal_btn_preview: String? = null
    private var dateHelper: DateHelper? = null
    private var dialogType: DialogType? = null
    private var headerTextView: HeaderTextView? = null
    private var localDate: LocalDate? = null
    private var oldTaskText: String? = null

    override val background: View?
        get() = null

    fun setListener(fragmentUIHandler: FragmentUIHandler) {
        this.fragmentUIHandler = fragmentUIHandler
    }

    fun setDialogType(dialogType: DialogType) {
        this.dialogType = dialogType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        time_calendar = Calendar.getInstance()
        sdf_time = SimpleDateFormat("HH:mm")
        localDate = LocalDate.now()
        dataManager = DataManager(context!!)
    }

//    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.custom_dialog, container, false)
        headerTextView = view.findViewById(R.id.dialog_textview_header)
        user_txt = view.findViewById(R.id.task_edit_text)
        user_txt?.isVerticalScrollBarEnabled = false
        cal_txt_btn = view.findViewById(R.id.dialog_txt_cal_btn)
        relativeLayout = view.findViewById(R.id.relative_layout_dialog)
        recyclerView = view.findViewById(R.id.recyclerView_dialog)
        rvaHelper = RVAHelper(context, this)
        rvaHelper?.setListener(this)
        recyclerView?.adapter = rvaHelper

        if (dialogType === DialogType.ADD_NEW_TASK) {
            headerTextView?.text = "Set a new task"
        } else {
            headerTextView?.text = "Edit task"
            user_txt?.setText(task?.task_string)
            //            dateHelper.setDateAndTime(task.getDate(), task.getTime());
            if (cal_txt_btn != null) {
                cal_txt_btn?.text = cal_btn_preview
            }

            if (cal_txt_btn != null) {
                user_txt?.setText(task?.task_string)
                oldTaskText = task?.task_string
            }

            if (rvaHelper != null) {
                rvaHelper?.loadSubtasksByReference(task!!.task_string)
            }
        }

        val add_element = view.findViewById<TextView>(R.id.add_element_dialog)

        val set = view.findViewById<ImageButton>(R.id.save_task_btn)

        cal_txt_btn?.text = cal_btn_preview
        cal_txt_btn?.setOnClickListener(this)
        add_element.setOnClickListener(this)
        set.setOnClickListener(this)

        return view
    }

    @SuppressLint("NewApi")
    override fun onClick(view: View) {

        when (view.id) {

            R.id.save_task_btn ->

                if (user_txt?.text.toString() == "" ||
                        user_txt?.text.toString() == " " ||
                        user_txt?.text.toString().trim { it <= ' ' }.isEmpty()) {

                    Toast.makeText(context, "Task is empty", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    val taskInteractor = TaskInteractor(context!!)
                    task = taskInteractor.createTask(user_txt!!.text.toString(),
                            dateHelper?.long,
                            sdf_time?.format(time_calendar!!.time),
                            false, false,
                            rvaHelper!!.arrayList, dialogType!!)
                    if (dialogType === DialogType.ADD_NEW_TASK) {
                        dataManager!!.addTask(task!!)
                    } else if (dialogType === DialogType.EDIT_EXISTING_TASK) {
                        dataManager!!.customUpdateTask(task, oldTaskText!!)
                    }

                    fragmentUIHandler!!.refreshAdapterOnAdd(1, AdapterRefreshType.RELOAD_FROM_DB)
                    changeToParentFragment()
                }

            R.id.cancel_btn -> changeToParentFragment()

            R.id.dialog_txt_cal_btn -> {

                val datePicker = DatePicker(context!!, null,
                        dateHelper?.year!!,
                        dateHelper?.getMonth()!!,
                        dateHelper?.dayOfMonth!!)

                datePicker.datePicker.init(
                        dateHelper?.year!!,
                        dateHelper!!.getMonth()!! - 1,
                        dateHelper?.dayOfMonth!!)
                { _, year, monthOfYear, dayOfMonth -> dateHelper!!.setDate(year, monthOfYear + 1, dayOfMonth) }
                datePicker.show()

                datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK")
                { _, _ ->
                    timePickerDialog = TimePickerDialog(context,
                            { view12, hourOfDay, minute ->
                                time_calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                time_calendar?.set(Calendar.MINUTE, minute)
                                dateHelper?.setTime(hourOfDay, minute)
                                cal_btn_preview = dateHelper?.fullDateWithTime
                                cal_txt_btn?.text = cal_btn_preview
                            },
                            time_calendar?.get(Calendar.HOUR_OF_DAY)!!,
                            time_calendar?.get(Calendar.MINUTE)!!, true)
                    timePickerDialog?.show()
                }
            }

            R.id.add_element_dialog -> rvaHelper!!.addToRV()

            else -> {
            }
        }
    }

    private fun changeToParentFragment() {
        dismiss()
    }

    //Method for refreshing and updating RecyclerView when new item is added
    override fun refreshAdapterOnAdd(position: Int, refreshType: AdapterRefreshType) {
        if (refreshType === AdapterRefreshType.RELOAD_FROM_DB) {
            rvaHelper?.notifyDataSetChanged()
        } else {
            rvaHelper?.notifyItemInserted(position)
        }
    }

    override fun refreshAdapterOnDelete(position: Int) {
        rvaHelper?.notifyItemRemoved(position)
    }

    fun setDate(localDate: LocalDate) {
        dateHelper = DateHelper()
        this.localDate = localDate
        dateHelper?.setLocalDate(localDate)
        cal_btn_preview = dateHelper?.localDateToString(this.localDate, FormatDateOutput.FORMAT_DATE_OUTPUT)
    }

    fun setTaskAndDate(task: Task, date: Long) {
        this.task = task
        localDate = LocalDate.ofEpochDay(date)
        dateHelper = DateHelper()
        cal_btn_preview = dateHelper?.localDateToString(this.localDate,
                FormatDateOutput.DEFAULT) +
                " " +
                task.time
    }

    companion object {

        fun newInstance(): AddTaskDialogFragment {
            return AddTaskDialogFragment()
        }
    }
}
