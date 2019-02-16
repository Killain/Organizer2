package com.killain.organizer.packages.recyclerview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.killain.organizer.R;
import com.killain.organizer.packages.fragments.IntroFragment3;
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.models.Task;

import java.util.ArrayList;
import java.util.Collections;

public class IntroRVAdapter extends RecyclerView.Adapter<IntroRVAdapter.CustomViewHolder> implements
        ItemTouchHelperAdapter {

    private Context context;

    private IntroFragment3 fragment;
    private ArrayList<Task> arrayList;
    private Task task;

    public IntroRVAdapter(Context context, IntroFragment3 fragment ) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card, viewGroup, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        task = arrayList.get(i);

        customViewHolder.card_edit_text_upper.setText(task.getTask_string());
        customViewHolder.card_date.setText("15 Jan");
        customViewHolder.card_time.setText(task.getTime());
//        RVSubTask RVSubTask = new RVSubTask(context, task);
//        customViewHolder.recycler_view.setVisibility(View.VISIBLE);
//        customViewHolder.recycler_view.setAdapter(RVSubTask);
//        customViewHolder.card_edit_text_upper.setText(task.getTask_string());
//        customViewHolder.card_edit_text_upper.setEnabled(false);

        customViewHolder.expand_area.setVisibility(View.GONE);

        customViewHolder.itemView.setOnClickListener(v -> {

            if(customViewHolder.expand_area.isShown()){
                YoYo.with(Techniques.FadeOut).duration(200).repeat(0).playOn(customViewHolder.expand_area);
                customViewHolder.expand_area.setVisibility(View.GONE);
            }
            else{
                YoYo.with(Techniques.FadeInDown).duration(200).repeat(0).playOn(customViewHolder.expand_area);
                customViewHolder.expand_area.setVisibility(View.VISIBLE);
            }
        });

        customViewHolder.delete_btn.setOnClickListener(v -> removeAt(0, task));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayList(ArrayList<Task> arrayList) {
        this.arrayList = arrayList;
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

    private void removeAt(int position, Task task) {
        arrayList.remove(task);
        fragment.refreshAdapterOnDelete();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        final ConstraintLayout expand_area;
        final EditText card_edit_text_upper;
        final TextView card_date, card_time;
        final ImageButton delete_btn;
        final RecyclerView recycler_view;
        final ImageButton edit_btn;
        final ConstraintLayout date_and_time_block;

        CustomViewHolder(View itemView) {
            super(itemView);
            recycler_view = itemView.findViewById(R.id.expanded_bottom_subtask_recyclerview);
            expand_area = itemView.findViewById(R.id.expanded_bottom);
            card_time = itemView.findViewById(R.id.card_time);
            edit_btn = itemView.findViewById(R.id.card_expanded_bottom_edit);
            date_and_time_block = itemView.findViewById(R.id.card_date_and_time_layout_block);
            delete_btn = itemView.findViewById(R.id.expanded_bottom_delete_img_btn);
            card_edit_text_upper = itemView.findViewById(R.id.card_text);
            card_date = itemView.findViewById(R.id.card_date);
        }
    }
}
