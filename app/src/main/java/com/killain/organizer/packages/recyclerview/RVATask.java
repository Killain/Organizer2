package com.killain.organizer.packages.recyclerview;

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

public class RVATask extends RecyclerView.Adapter<RVATask.CustomViewHolder> {

    private ArrayList<SubTask> arrayList;
    public Context context;
    public DataManager dataManager;

    public RVATask(Context context, Task task) {
        this.context = context;
        dataManager = new DataManager(context, null);
        arrayList = dataManager.getSubTasksByReference(task.getTitle());
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RVATask.CustomViewHolder vh = new RVATask.CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.recycler_edittext.setText(arrayList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public EditText recycler_edittext;
        public ImageButton delete_btn;
        public CheckBox checkBox;

        public CustomViewHolder(View itemView) {
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
