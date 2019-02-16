package com.killain.organizer.packages.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.killain.organizer.R;
import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.interactors.RecyclerViewInteractor;
import com.killain.organizer.packages.models.Task;
import com.killain.organizer.packages.recyclerview_adapters.IntroRVAdapter;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;

import java.util.ArrayList;

public class IntroFragment3 extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private IntroRVAdapter adapter;
    private RecyclerViewInteractor recyclerViewInteractor;
    private ArrayList<Task> arrayList = new ArrayList<>();
    private boolean isFabClicked = false;

    public IntroFragment3() {
    }

    public static IntroFragment3 newInstance() {
        IntroFragment3 fragment = new IntroFragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro_fragment3, container, false);

        fab = view.findViewById(R.id.intro_frg3_fab);
        recyclerView = view.findViewById(R.id.intro_frg3_recycler);
        Task task = new Task();
        task.setTime("13:44");
        task.setTask_string("Call Charlie and remind her about the project");
        arrayList.add(task);
        adapter = new IntroRVAdapter(getContext(), this);
        adapter.setArrayList(arrayList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            if (!isFabClicked) {
                Task task1 = new Task();
                task1.setTime("18:00");
                task1.setTask_string("Rate this app in Google Play");
                arrayList.add(task1);
                adapter.setArrayList(arrayList);
                adapter.notifyDataSetChanged();
                isFabClicked = true;
            } else {
                Toast.makeText(getContext(), "See? It's easy!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void refreshAdapterOnDelete() {
        adapter.notifyItemRemoved(0);
    }
}
