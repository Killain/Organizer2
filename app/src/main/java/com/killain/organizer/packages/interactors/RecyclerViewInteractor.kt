package com.killain.organizer.packages.interactors

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

import com.killain.organizer.packages.callbacks.SimpleItemTouchHelperCallback
import com.killain.organizer.packages.interfaces.ItemTouchHelperAdapter
import com.killain.organizer.packages.interfaces.OnStartDragListener
import com.killain.organizer.packages.recyclerview_adapters.IntroRVAdapter
import com.killain.organizer.packages.recyclerview_adapters.RVCardAdapter

class RecyclerViewInteractor(private val recyclerView: RecyclerView) :
        OnStartDragListener {

    private var callback: ItemTouchHelper.Callback? = null
    private var mItemTouchHelper: ItemTouchHelper? = null
    //    private RVCardAdapter adapter;
    var adapter: Any? = null
        set(value) {
            field = value
        }

    val listener: OnStartDragListener = this@RecyclerViewInteractor

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder)
    }

    fun bind() {
        callback = SimpleItemTouchHelperCallback(adapter as ItemTouchHelperAdapter?)
        mItemTouchHelper = ItemTouchHelper(callback as SimpleItemTouchHelperCallback)
        mItemTouchHelper?.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter as RecyclerView.Adapter<*>?
    }

    //    public void setAdapter(RVCardAdapter adapter) {
    //        this.adapter = adapter;
    //    }

//    fun setAdapter(adapter: Any) {
//        if (adapter is RVCardAdapter) {
//            this.adapter = adapter
//        } else if (adapter is IntroRVAdapter) {
//            this.adapter = adapter
//        }
//    }

}
