package com.killain.organizer.packages.recyclerview_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.killain.organizer.R;
import com.killain.organizer.packages.enums.AdapterRefreshType;
import com.killain.organizer.packages.fragments.AddTaskDialogFragment;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.FragmentUIHandler;
import com.killain.organizer.packages.models.SubTask;
import com.killain.organizer.packages.views.ListEditTextView;

import java.util.ArrayList;

public class RVAHelper extends RecyclerView.Adapter<RVAHelper.ViewHolder>{

    private Context context;
    private FragmentUIHandler fragmentUIHandler;
    private ArrayList<SubTask> arrayList;
    private AddTaskDialogFragment fragment;
    private DataManager dataManager;
    private int index = 0;

    public RVAHelper(Context context, FragmentUIHandler fragmentUIHandler) {
        this.context = context;
        this.fragmentUIHandler = fragmentUIHandler;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RVAHelper.ViewHolder vh = new RVAHelper.ViewHolder(v);
        dataManager = new DataManager(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubTask item = arrayList.get(position);

        holder.recycler_edittext.setText(item.getText());

        if (holder.recycler_edittext.getText().toString().trim().length() == 0)
        {
            holder.recycler_edittext.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        holder.recycler_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                arrayList.get(position).setText(s.toString());
                arrayList.get(holder.getAdapterPosition()).setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//               arrayList.get(position).setText(s.toString());
                arrayList.get(holder.getAdapterPosition()).setText(s.toString());
            }
        });

        holder.delete_btn.setOnClickListener(v -> removeAt(item, holder.getAdapterPosition()));
    }

    public void addToRV() {
        SubTask subTask = new SubTask();
        arrayList.add(subTask);
        fragmentUIHandler.refreshAdapterOnAdd(arrayList.size() - 1, AdapterRefreshType.DEFAULT);
        index++;
    }

    @Override
    public int getItemCount() {
        if (arrayList.size() != 0) {
            return arrayList.size();
        } else {
            return 0;
        }
    }

    public ArrayList<SubTask> getArrayList() {
        return arrayList;
    }

    public void setListener(AddTaskDialogFragment fragment) {
        this.fragment = fragment;
    }

    private void removeAt(SubTask subTask, int position) {
//        if (position == 0) {
//            arrayList.remove(position);
//            fragmentUIHandler.refreshAdapterOnDelete(position);
//        }
            arrayList.remove(subTask);
            fragmentUIHandler.refreshAdapterOnDelete(position);
    }

    public void setSubTasksReference(String reference) {
        for (SubTask subTask : arrayList) {
            subTask.setReference(reference);
            dataManager.addSubTask(subTask);
        }
    }

    private void refreshItems(ArrayList<SubTask> array) {
        ArrayList<SubTask> secondary = new ArrayList<>(array);
        arrayList.clear();
        arrayList.addAll(secondary);
        fragment.refreshAdapterOnAdd(index, AdapterRefreshType.DEFAULT);
    }

    public void loadSubtasksByReference(String reference) {
        DataManager dataManager = new DataManager(context);
        arrayList = dataManager.getSubTasksByReference(reference);
        fragmentUIHandler.refreshAdapterOnAdd(0, AdapterRefreshType.RELOAD_FROM_DB);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ListEditTextView recycler_edittext;
        ImageButton delete_btn;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            recycler_edittext = itemView.findViewById(R.id.recycler_item_edittext);
            delete_btn = itemView.findViewById(R.id.delete_item_recycler);
            checkBox = itemView.findViewById(R.id.checkbox_recycler);
        }
    }
}
