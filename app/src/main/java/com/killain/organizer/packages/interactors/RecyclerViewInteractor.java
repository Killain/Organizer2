package com.killain.organizer.packages.interactors;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;

public class RecyclerViewInteractor implements OnStartDragListener {
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper mItemTouchHelper;
    private RVCardAdapter adapter;
    private RecyclerView recyclerView;
    private OnStartDragListener listener = RecyclerViewInteractor.this;

    public RecyclerViewInteractor(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void bind() {
        callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public OnStartDragListener getListener() {
        return listener;
    }

    public void setAdapter(RVCardAdapter adapter) {
        this.adapter = adapter;
    }
}
