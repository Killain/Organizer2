package com.killain.organizer.packages.recyclerview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.ItemTouchHelperViewHolder;
import com.killain.organizer.packages.tasks.SubTask;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;

public class RVSubTask extends RecyclerView.Adapter<RVSubTask.CustomViewHolder> {

    private ArrayList<SubTask> arrayList;
    public Context context;
    public DataManager dataManager;

    RVSubTask(Context context, Task task) {
        this.context = context;
        dataManager = new DataManager(context, null);
        arrayList = dataManager.getSubTasksByReference(task.getTask_string());
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        SubTask subTask = arrayList.get(position);
        holder.recycler_edittext.setText(subTask.getText());
        holder.recycler_edittext.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

         EditText recycler_edittext;
         ImageButton delete_btn;
         CheckBox checkBox;

         CustomViewHolder(View itemView) {
            super(itemView);
            recycler_edittext = itemView.findViewById(R.id.recycler_item_edittext);
            delete_btn = itemView.findViewById(R.id.delete_item_recycler);
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
