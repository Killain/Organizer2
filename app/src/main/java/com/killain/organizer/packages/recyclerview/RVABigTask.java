package com.killain.organizer.packages.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.killain.organizer.R;
import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.fragments.AddBigTaskFragment;
import com.killain.organizer.packages.interactors.DataManager;
import com.killain.organizer.packages.interfaces.IAdapterRefresher;
import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.tasks.SubTask;

import java.util.ArrayList;

import io.reactivex.Observable;

public class RVABigTask extends RecyclerView.Adapter<RVABigTask.ViewHolder>{

    private Context context;
    private IAdapterRefresher iAdapterRefresher;
    private ArrayList<SubTask> arrayList;
    private AddBigTaskFragment fragment;
    private DataManager dataManager;
    private int index = 0;

    public RVABigTask(Context context ) {
        this.context = context;
        this.iAdapterRefresher = iAdapterRefresher;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RVABigTask.ViewHolder vh = new RVABigTask.ViewHolder(v);
        dataManager = new DataManager(context, null);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubTask item = arrayList.get(position);

        holder.recycler_edittext.setText(item.getText());

        if (arrayList.size() > 0) {
            fragment.save_btn.setVisibility(View.VISIBLE);
        } else if (arrayList.size() <= 0) {
            fragment.save_btn.setVisibility(View.GONE);
        }

        if (holder.recycler_edittext.getText().toString().trim().length() == 0)
        {
            holder.recycler_edittext.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        holder.recycler_edittext.addTextChangedListener(new TextWatcher() {
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
            }
        });

        holder.delete_btn.setOnClickListener(v -> removeAt(position));
    }

    public void addToRV() {
        SubTask subTask = new SubTask();
        arrayList.add(subTask);
        fragment.refreshAdapterOnAdd(arrayList.size() - 1);
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

    public void setListener(AddBigTaskFragment fragment) {
        this.fragment = fragment;
    }

    public void removeAt(int position) {
        arrayList.remove(position);
        fragment.refreshAdapterOnDelete(position);
    }

    public void setSubTasksReference(String reference) {
        for (SubTask subTask : arrayList) {
            subTask.setReference(reference);
            dataManager.addSubTask(subTask);
        }
    }

    private void refreshItems(ArrayList<SubTask> array) {
        ArrayList<SubTask> secondary = new ArrayList<>();
        secondary.addAll(array);
        arrayList.clear();
        arrayList.addAll(secondary);
        fragment.refreshAdapterOnAdd(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText recycler_edittext;
        public ImageButton delete_btn;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            recycler_edittext = itemView.findViewById(R.id.recycler_item_edittext);
            delete_btn = itemView.findViewById(R.id.delete_item_recycler);
            checkBox = itemView.findViewById(R.id.checkbox_recycler);
        }
    }
}
