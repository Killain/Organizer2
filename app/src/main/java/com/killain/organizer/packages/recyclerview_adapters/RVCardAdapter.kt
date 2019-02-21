package com.killain.organizer.packages.recyclerview_adapters

import android.annotation.SuppressLint

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar

import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.killain.organizer.R
import com.killain.organizer.packages.enums.AdapterRefreshType

import com.killain.organizer.packages.enums.DialogType
import com.killain.organizer.packages.fragments.TasksFragment
import com.killain.organizer.packages.interactors.DataManager
import com.killain.organizer.packages.interfaces.FragmentUIHandler
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter
import com.killain.organizer.packages.interfaces.ItemTouchHelperViewHolder
import com.killain.organizer.packages.interfaces.OnStartDragListener
import com.killain.organizer.packages.models.Task
import com.killain.organizer.packages.interactors.DateHelper

import java.util.ArrayList
import java.util.Collections

class RVCardAdapter(private val context: Context,
                    private val mDragStartListener: OnStartDragListener,
                    private val fragmentUIHandler: FragmentUIHandler?) :
        RecyclerView.Adapter<RVCardAdapter.CustomViewHolder>(),
        ItemTouchHelperAdapter {

    var arrayList: MutableList<Task?>? = null

    private val delayedTaskArrayList: ArrayList<Task?>?
    private var task: Task? = null
    private var delayedTask: Task? = null
    private val dataManager: DataManager?
    private val dateHelper: DateHelper
    var fragment: TasksFragment? = null

    init {
        dataManager = DataManager(context)
        delayedTaskArrayList = ArrayList()
        dateHelper = DateHelper()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        return CustomViewHolder(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        task = arrayList?.get(position)

        if (task?.isHasReference!!) {
            val rvSubTask = RVSubTask(context, task)
            holder.recyclerView?.visibility = View.VISIBLE
            holder.recyclerView?.adapter = rvSubTask
            holder.cardEditTextUpper?.setText(task?.task_string)
            holder.cardEditTextUpper?.isEnabled = false
            //            holder.cardDate.setText(task.getDate());
            holder.cardDate?.text = dateHelper.getConvertedDateFromLong(task?.date)
            holder.cardTime?.text = task?.time
        } else {
            holder.cardEditTextUpper?.setText(task!!.task_string)
            holder.cardTime?.text = task!!.time
            holder.cardEditTextUpper?.isEnabled = false
            //            holder.cardDate.setText(task.getDate());
            holder.cardDate?.text = dateHelper.getConvertedDateFromLong(task!!.date)
        }

        holder.expandArea?.visibility = View.GONE

        holder.itemView.setOnClickListener {

            if (holder.expandArea?.isShown!!) {
                YoYo.with(Techniques.FadeOut).duration(200).repeat(0).playOn(holder.expandArea)
                holder.expandArea.visibility = View.GONE
            } else {
                YoYo.with(Techniques.FadeInDown).duration(200).repeat(0).playOn(holder.expandArea)
                holder.expandArea.visibility = View.VISIBLE
            }
        }

        holder.dragger?.setOnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder)
            }
            false
        }

        holder.deleteButton?.setOnClickListener {
            removeAt(holder.adapterPosition, task!!)
            dataManager?.deleteTask(task)
        }

        holder.editButton?.setOnClickListener {
            fragment?.callDialogFragment(DialogType.EDIT_EXISTING_TASK,
                    fragmentUIHandler,
                    task!!.date!!, task)
        }
    }

    override fun getItemCount(): Int {
        return if (arrayList?.size != 0) {
            arrayList!!.size
        } else {
            0
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(arrayList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        removeAt(position, arrayList?.get(position))
        notifyDataSetChanged()
    }

    private fun removeAt(position: Int, t: Task?) {
        task = t
        task?.list_index = position
        task?.isDeleted = true

        try {
            delayedTask = task?.clone() as Task
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        delayedTaskArrayList?.add(delayedTask)
        arrayList?.removeAt(position)
        Snackbar.make(fragmentUIHandler?.background!!, "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    try {
                        task = delayedTaskArrayList?.get(delayedTaskArrayList.size - 1)?.clone() as Task
                    } catch (e: CloneNotSupportedException) {
                        e.printStackTrace()
                    }
                    task?.isDeleted = false

                    arrayList?.add(task)
                    delayedTaskArrayList?.removeAt(0)
                    fragmentUIHandler.refreshAdapterOnAdd(position, AdapterRefreshType.DEFAULT)
                }.show()
        fragmentUIHandler.refreshAdapterOnDelete(position)
    }

    fun loadItemsByState() {
        arrayList = dataManager?.getUndeletedTasks()
    }

    fun loadItemsByDate(date: Long) {
        arrayList = dataManager?.getAllTasksByDate(date)
    }

//    fun getArrayList(): ArrayList<Task?>? {
//        return arrayList as ArrayList<Task?>?
//    }

    fun onDestroy() {
        if (delayedTaskArrayList != null && delayedTaskArrayList.size != 0) {
            for (task in delayedTaskArrayList) {
                task?.isDeleted = true
                dataManager?.updateTask(task)
            }
        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            ItemTouchHelperViewHolder {

        val expandArea: ConstraintLayout?
        val cardEditTextUpper: EditText?
        val cardDate: TextView?
        val cardTime: TextView?
        val dragger: ImageView?
        val deleteButton: ImageButton?
        val recyclerView: RecyclerView?
        //        final ImageButton done_btn;
        val editButton: ImageButton?
        private val dateAndTimeBlock: ConstraintLayout?

        init {
            //            done_btn = itemView.findViewById(R.id.task_done_btn);
            recyclerView = itemView.findViewById(R.id.expanded_bottom_subtask_recyclerview)
            expandArea = itemView.findViewById(R.id.expanded_bottom)
            cardTime = itemView.findViewById(R.id.card_time)
            editButton = itemView.findViewById(R.id.card_expanded_bottom_edit)
            dateAndTimeBlock = itemView.findViewById(R.id.card_date_and_time_layout_block)
            deleteButton = itemView.findViewById(R.id.expanded_bottom_delete_img_btn)
            cardEditTextUpper = itemView.findViewById(R.id.card_text)
            dragger = itemView.findViewById(R.id.img_view_drag)
            //            extra_expand_area = itemView.findViewById(R.id.extra_expanded_bottom);
            cardDate = itemView.findViewById(R.id.card_date)
        }

        override fun onItemSelected() {}

        override fun onItemClear() {}
    }
}
