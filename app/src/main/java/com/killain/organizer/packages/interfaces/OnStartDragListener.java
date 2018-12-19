package com.killain.organizer.packages.interfaces;

import android.support.v7.widget.RecyclerView;

import com.killain.organizer.packages.card.CardAdapter;

public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */

    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
