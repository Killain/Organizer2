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
import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.tasks.SubTask;

import java.util.ArrayList;

import io.reactivex.Observable;

public class RVABigTask extends RecyclerView.Adapter<RVABigTask.ViewHolder>{

    private Context context;
    private ArrayList<SubTask> arrayList;
    private AddBigTaskFragment fragment;

    public RVABigTask(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        RVABigTask.ViewHolder vh = new RVABigTask.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Toast.makeText(context, "VH Pos:" + position, Toast.LENGTH_SHORT).show();

        SubTask item = arrayList.get(position);

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
                item.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
               item.setText(s.toString());
            }
        });

        holder.delete_btn.setOnClickListener(v -> removeAt(position));
    }

    public void addToRV() {
        SubTask subTask = new SubTask();
        arrayList.add(subTask);
        notifyItemInserted(arrayList.size());
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
        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, arrayList.size());
        notifyDataSetChanged();
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
