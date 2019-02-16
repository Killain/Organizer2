package com.killain.organizer.packages.interactors;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback;
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter;
import com.killain.organizer.packages.interfaces.OnStartDragListener;
import com.killain.organizer.packages.recyclerview_adapters.IntroRVAdapter;
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter;

public class RecyclerViewInteractor implements OnStartDragListener {
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper mItemTouchHelper;
//    private RVCardAdapter adapter;
    private Object adapter;
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
        callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    public OnStartDragListener getListener() {
        return listener;
    }

//    public void setAdapter(RVCardAdapter adapter) {
//        this.adapter = adapter;
//    }

    public void setAdapter(Object adapter) {
        if (adapter instanceof RVCardAdapter) {
            this.adapter = (RVCardAdapter) adapter;
        }
        else if (adapter instanceof IntroRVAdapter) {
            this.adapter = (IntroRVAdapter) adapter;
        }
    }

}
