package com.killain.organizer.packages.recyclerview_adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.killain.organizer.R
import com.killain.organizer.packages.fragments.IntroFragment3
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter
import com.killain.organizer.packages.models.Task

import java.util.ArrayList
import java.util.Collections

class IntroRVAdapter(private val context: Context?,
                     private val fragment: IntroFragment3?) :
        RecyclerView.Adapter<IntroRVAdapter.CustomViewHolder>(),
        ItemTouchHelperAdapter {

    var arrayList: ArrayList<Task?>? = null
    private var task: Task? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.task_card, viewGroup, false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        task = arrayList?.get(i)

        customViewHolder.cardEditTextUpper?.setText(task!!.task_string)
        customViewHolder.cardDate?.text = "15 Jan"
        customViewHolder.cardTime?.text = task!!.time
        //        RVSubTask RVSubTask = new RVSubTask(context, task);
        //        customViewHolder.recyclerView.setVisibility(View.VISIBLE);
        //        customViewHolder.recyclerView.setAdapter(RVSubTask);
        //        customViewHolder.cardEditTextUpper.setText(task.getTask_string());
        //        customViewHolder.cardEditTextUpper.setEnabled(false);

        customViewHolder.expandArea?.visibility = View.GONE

        customViewHolder.itemView.setOnClickListener {
            if (customViewHolder.expandArea?.isShown!!) {
                YoYo.with(Techniques.FadeOut).duration(200).repeat(0).playOn(customViewHolder.expandArea)
                customViewHolder.expandArea.visibility = View.GONE
            } else {
                YoYo.with(Techniques.FadeInDown).duration(200).repeat(0).playOn(customViewHolder.expandArea)
                customViewHolder.expandArea.visibility = View.VISIBLE
            }
        }

        customViewHolder.deleteButton.setOnClickListener {
            removeAt(0, task)
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size ?: 0
    }

//    fun setArrayList(arrayList: ArrayList<Task?>) {
//        this.arrayList = arrayList
//    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(arrayList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        removeAt(position, arrayList?.get(position))
    }

    private fun removeAt(position: Int, task: Task?) {
        arrayList?.remove(task)
        fragment?.refreshAdapterOnDelete()
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val expandArea: ConstraintLayout?
        val cardEditTextUpper: EditText?
        val cardDate: TextView?
        val cardTime: TextView?
        val deleteButton : ImageButton
        private val recyclerView : RecyclerView?
        private val editButton : ImageButton?
        private val dateAndTimeBlock: ConstraintLayout?

        init {
            recyclerView = itemView.findViewById(R.id.expanded_bottom_subtask_recyclerview)
            expandArea = itemView.findViewById(R.id.expanded_bottom)
            cardTime = itemView.findViewById(R.id.card_time)
            editButton = itemView.findViewById(R.id.card_expanded_bottom_edit)
            dateAndTimeBlock = itemView.findViewById(R.id.card_date_and_time_layout_block)
            deleteButton = itemView.findViewById(R.id.expanded_bottom_delete_img_btn)
            cardEditTextUpper = itemView.findViewById(R.id.card_text)
            cardDate = itemView.findViewById(R.id.card_date)
        }
    }
}
