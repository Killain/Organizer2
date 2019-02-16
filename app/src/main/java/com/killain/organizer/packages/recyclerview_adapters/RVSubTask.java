package com.killain.organizer.packages.recyclerview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.killain.organizer.R;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.ItemTouchHelperViewHolder;
import com.killain.organizer.packages.models.SubTask;
import com.killain.organizer.packages.models.Task;

import java.util.ArrayList;

public class RVSubTask extends RecyclerView.Adapter<RVSubTask.CustomViewHolder> {

    private ArrayList<SubTask> arrayList;
    public Context context;
    private DataManager dataManager;

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
        holder.recycler_item_edittext.setText(subTask.getText());
        holder.recycler_item_edittext.setEnabled(false);

        if (subTask.isChecked()) {
            holder.recycler_item_edittext.setAlpha(0.4f);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
               holder.recycler_item_edittext.setAlpha(0.4f);
               buttonView.setChecked(true);
               dataManager.updateSubTask(subTask);
            } else {
                holder.recycler_item_edittext.setAlpha(1f);
                buttonView.setChecked(false);
                dataManager.updateSubTask(subTask);
            }
        });

            holder.recycler_item_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arrayList.get(position).setText(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    arrayList.get(position).setText(s.toString());
                    dataManager.updateSubTask(arrayList.get(position));
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

         EditText recycler_item_edittext;
         ImageButton delete_btn;
         CheckBox checkBox;

         CustomViewHolder(View itemView) {
            super(itemView);
            recycler_item_edittext = itemView.findViewById(R.id.recycler_item_edittext);
            delete_btn = itemView.findViewById(R.id.delete_item_recycler);
            checkBox = itemView.findViewById(R.id.checkbox_recycler);
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
