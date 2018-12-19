package com.killain.organizer.packages.ui_tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.MenuPopupWindow;
import android.view.View;
import android.widget.PopupMenu;

public class GroupTaskPopupMenu extends MenuPopupWindow.MenuDropDownListView {

    @SuppressLint("RestrictedApi")
    public GroupTaskPopupMenu(Context context, boolean hijackFocus) {
        super(context, hijackFocus);
    }


}
