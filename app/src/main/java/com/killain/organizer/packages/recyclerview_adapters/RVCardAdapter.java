package com.killain.organizer.packages.recyclerview_adapters;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;

import com.killain.organizer.packages.enums.DialogType;
import com.killain.organizer.packages.fragments.AddTaskDialogFragment;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter;
import com.killain.organizer.packages.interfaces.ItemTouchHelperViewHolder;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.models.Task;
import com.killain.organizer.packages.interactors.DateHelper;
import com.killain.organizer.packages.ui_tools.DatePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RVCardAdapter extends RecyclerView.Adapter<RVCardAdapter.CustomViewHolder> implements
        ItemTouchHelperAdapter{

    private Context context;
    private List<Task> arrayList;
    private ArrayList<Task> delayedTaskArrayList;
    private OnStartDragListener mDragStartListener;
    private FragmentUIHandler fragmentUIHandler;
    private Task task, delayed_task;
    private DataManager dataManager;
    private DateHelper dateHelper;
    private TasksFragment fragment;

    public RVCardAdapter(Context context,
                         OnStartDragListener dragListener,
                         FragmentUIHandler fragmentUIHandler) {

        this.context = context;
        mDragStartListener = dragListener;
        this.fragmentUIHandler = fragmentUIHandler;
        dataManager = new DataManager(context, null);
        delayedTaskArrayList = new ArrayList<>();
        dateHelper = new DateHelper();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new CustomViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        task = arrayList.get(position);

        if (task.isHasReference()) {
            RVSubTask RVSubTask = new RVSubTask(context, task);
            holder.recycler_view.setVisibility(View.VISIBLE);
            holder.recycler_view.setAdapter(RVSubTask);
            holder.card_edit_text_upper.setText(task.getTask_string());
            holder.card_edit_text_upper.setEnabled(false);
//            holder.cardDate.setText(task.getDate());
            holder.card_date.setText(dateHelper.getConvertedDateFromLong(task.getDate()));
            holder.card_time.setText(task.getTime());
        } else {
            holder.card_edit_text_upper.setText(task.getTask_string());
            holder.card_time.setText(task.getTime());
            holder.card_edit_text_upper.setEnabled(false);
//            holder.cardDate.setText(task.getDate());
            holder.card_date.setText(dateHelper.getConvertedDateFromLong(task.getDate()));
        }

        holder.expand_area.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {

            if(holder.expand_area.isShown()){
                YoYo.with(Techniques.FadeOut).duration(200).repeat(0).playOn(holder.expand_area);
                holder.expand_area.setVisibility(View.GONE);
            }
            else{
                YoYo.with(Techniques.FadeInDown).duration(200).repeat(0).playOn(holder.expand_area);
                holder.expand_area.setVisibility(View.VISIBLE);
            }
        });

        holder.dragger.setOnTouchListener((v, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
            }
            return false;
        });

        holder.delete_btn.setOnClickListener((v) -> {
            removeAt(holder.getAdapterPosition(), task);
            dataManager.deleteTask(task);
        });

        holder.edit_btn.setOnClickListener(v ->
                fragment.callDialogFragment(DialogType.EDIT_EXISTING_TASK,
                        fragmentUIHandler,
                        task.getDate(),
                        task));
    }

    @Override
    public int getItemCount() {
            if (arrayList.size() != 0) {
                return arrayList.size();
            } else {
                return 0;
            }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        removeAt(position, arrayList.get(position));
        notifyDataSetChanged();
    }

    private void removeAt(int position, Task t) {
        task = t;
        task.setList_index(position);
        try {
            delayed_task = (Task) task.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        delayedTaskArrayList.add(t);
        task.setDeleted(true);
        arrayList.remove(position);
        Snackbar.make(fragmentUIHandler.getBackground(), "Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    task.setDeleted(false);
                    try {
                        task = (Task) delayedTaskArrayList.get(delayedTaskArrayList.size() - 1).clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    arrayList.add(task);
                    delayedTaskArrayList.remove(0);
                    fragmentUIHandler.refreshAdapterOnAdd(position, AdapterRefreshType.DEFAULT);
                }).show();
        fragmentUIHandler.refreshAdapterOnDelete(position);
    }

    public void loadItemsByState() {
        arrayList = dataManager.getUndeletedTasks();
    }

    public void loadItemsByDate(long date) {
        arrayList = dataManager.getAllTasksByDate(date);
    }

    public ArrayList<Task> getArrayList() {
        return (ArrayList<Task>) arrayList;
    }

    public void onDestroy() {
        if (delayedTaskArrayList != null && delayedTaskArrayList.size() != 0) {
            for (Task task : delayedTaskArrayList) {
                task.setDeleted(true);
                dataManager.updateTask(task);
            }
        }
    }

    public void setParentFragment(TasksFragment fragment) {
        this.fragment = fragment;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        final ConstraintLayout expand_area;
        final EditText card_edit_text_upper;
        final TextView card_date, card_time;
        final ImageView dragger;
        final ImageButton delete_btn;
//        final RelativeLayout extra_expand_area;
        final RecyclerView recycler_view;
//        final ImageButton done_btn;
        final ImageButton edit_btn;
        final ConstraintLayout date_and_time_block;

        CustomViewHolder(View itemView)
        {
            super(itemView);
//            done_btn = itemView.findViewById(R.id.task_done_btn);
            recycler_view = itemView.findViewById(R.id.expanded_bottom_subtask_recyclerview);
            expand_area = itemView.findViewById(R.id.expanded_bottom);
            card_time = itemView.findViewById(R.id.card_time);
            edit_btn = itemView.findViewById(R.id.card_expanded_bottom_edit);
            date_and_time_block = itemView.findViewById(R.id.card_date_and_time_layout_block);
            delete_btn = itemView.findViewById(R.id.expanded_bottom_delete_img_btn);
            card_edit_text_upper = itemView.findViewById(R.id.card_text);
            dragger = itemView.findViewById(R.id.img_view_drag);
//            extra_expand_area = itemView.findViewById(R.id.extra_expanded_bottom);
            card_date = itemView.findViewById(R.id.card_date);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }
}
