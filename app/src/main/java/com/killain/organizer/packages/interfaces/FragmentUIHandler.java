package com.killain.organizer.packages.interfaces;

import android.view.View;

import com.killain.organizer.packages.enums.AdapterRefreshType;

public interface FragmentUIHandler {
    View getBackground();
    void refreshAdapterOnAdd(int position, AdapterRefreshType refreshType);
    void refreshAdapterOnDelete(int position);
}
