package com.killain.organizer.packages.interactors;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class UIInteractor {

    private View background;

    private FloatingActionButton fab;
    private com.github.clans.fab.FloatingActionButton fab_custom;
    private View navigation;

    public UIInteractor(@Nullable View background, @Nullable com.github.clans.fab.FloatingActionButton button, @Nullable View navigation) {
        this.background = background;
        this.fab_custom = button;
        this.navigation = navigation;
    }

    public UIInteractor(@Nullable View background, @Nullable FloatingActionButton fab, @Nullable View navigation) {
        this.background = background;
        this.fab = fab;
        this.navigation = navigation;
    }

    public void UISwitch() {
        if (navigation.isShown()) {
            if (fab_custom != null) {
                fab_custom.setVisibility(View.GONE);
                fab_custom.hide(false);
            }
            navigation.setVisibility(View.GONE);
        } else if (!navigation.isShown()) {
            if (fab_custom != null) {
                fab_custom.setVisibility(View.VISIBLE);
                fab_custom.show(true);
            }
            navigation.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setNewAlpha() {
        if (background.getAlpha() == 1f) {
            background.setAlpha(0.3f);
        } else {
            background.setAlpha(1f);
        }
    }
}
