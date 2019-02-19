package com.killain.organizer.packages.interfaces

import android.view.View

import com.killain.organizer.packages.enums.AdapterRefreshType

interface FragmentUIHandler {
    val background: View?
    fun refreshAdapterOnAdd(position: Int, refreshType: AdapterRefreshType)
    fun refreshAdapterOnDelete(position: Int)
}
