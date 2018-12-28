package com.killain.organizer.packages.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.killain.organizer.R;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.fragments.CalendarDayFragment;
import com.killain.organizer.packages.fragments.TasksFragment;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter;
import com.killain.organizer.packages.interfaces.ItemTouchHelperViewHolder;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.recyclerview.RVATask;
import com.killain.organizer.packages.tasks.SubTask;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CustomViewHolder> implements ItemTouchHelperAdapter{

    private Context context;
    private List<Task> arrayList;
    private ArrayList<SubTask> subTaskArrayList;
    private OnStartDragListener mDragStartListener;
    private IAdapterRefresher iAdapterRefresher;
    private Task task_buffer;
    private int size;
    private Task task;
    private DataManager dataManager;

    public CardAdapter(Context context,
                       OnStartDragListener dragListener,
                       IAdapterRefresher iAdapterRefresher) {

        this.context = context;
        mDragStartListener = dragListener;
        this.iAdapterRefresher = iAdapterRefresher;
        dataManager = new DataManager(context, null);
        arrayList = dataManager.getTasksByState(false, false);
    }

    public CardAdapter() {}

    public static CardAdapter newInstance(Context context,
                                   OnStartDragListener dragListener,
                                   IAdapterRefresher iAdapterRefresher) {

        return new CardAdapter(context, dragListener, iAdapterRefresher);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(v);

        return cvh;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        task = arrayList.get(position);
        task_buffer = task;

        if (task.getTitle() != null) {
            subTaskArrayList = dataManager.getSubTasksByReference(task.getTitle());
            RVATask rvaTask = new RVATask(context, task);
            holder.recycler_view.setVisibility(View.VISIBLE);
            holder.recycler_view.setAdapter(rvaTask);
            holder.card_text_upper.setText(task.getTitle());
        } else {
            holder.card_text_upper.setText(task.getTask_string());
            holder.expanded_text_big_task.setVisibility(View.GONE);
        }

        holder.expand_area.setVisibility(View.GONE);

        holder.extra_expand_area.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {

            if(holder.expand_area.isShown()){
                YoYo.with(Techniques.FadeOut).duration(700).repeat(0).playOn(holder.expand_area);
                holder.expand_area.setVisibility(View.GONE);
            }
            else{
                YoYo.with(Techniques.FadeInDown).duration(700).repeat(0).playOn(holder.expand_area);
                holder.expand_area.setVisibility(View.VISIBLE);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if(holder.extra_expand_area.isShown()){
                holder.extra_expand_area.setVisibility(View.GONE);
            }
            else{
                holder.extra_expand_area.setVisibility(View.VISIBLE);
            }
            return true;
        });

        holder.handler.setOnTouchListener((v, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
            }
            return false;
        });

        holder.delete_btn.setOnClickListener((v) -> {
            removeAt(holder.getAdapterPosition(), task);
            dataManager.deleteTask(task);
        });

        holder.done_btn.setOnClickListener(v -> {
            task.setCompleted(true);
            dataManager.updateTask(task);
            arrayList.remove(position);
            removeAt(position, task);
            iAdapterRefresher.refreshAdapterOnDelete(position);
            refreshItems();
        });
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
    }

    public void removeAt(int position, Task t) {
        task = t;
        task.setDeleted(true);
        arrayList.remove(position);
        dataManager.updateTask(task);
        iAdapterRefresher.refreshAdapterOnDelete(position);
//        refreshItems();
    }

    public void refreshItems() {
        arrayList = dataManager.getTasksByState(false, false);
    }

    public void reloadItemsByDate(String date) {
        arrayList = dataManager.taskDAO.getAllTasksByDate(date);
    }

    public ArrayList<Task> getArrayList() {
        return (ArrayList<Task>) arrayList;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final RelativeLayout expand_area;
        public final TextView card_text_upper;
        public final TextView expanded_text_big_task;
        public final ImageView handler;
        public final ImageButton delete_btn;
        public final RelativeLayout extra_expand_area;
        public final RecyclerView recycler_view;
        public final ImageButton done_btn;

        public CustomViewHolder(View itemView)
        {
            super(itemView);
            done_btn = itemView.findViewById(R.id.task_done_btn);
            recycler_view = itemView.findViewById(R.id.expanded_bottom_subtask_recyclerview);
            expand_area = itemView.findViewById(R.id.expanded_bottom);
            delete_btn = itemView.findViewById(R.id.delete_task_btn);
            card_text_upper = itemView.findViewById(R.id.card_text);
            expanded_text_big_task = itemView.findViewById(R.id.big_task_expanded_text);
            handler = itemView.findViewById(R.id.img_view_drag);
            extra_expand_area = itemView.findViewById(R.id.extra_expanded_bottom);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
