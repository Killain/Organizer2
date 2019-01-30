package com.killain.organizer.packages.ui_tools;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final View view;

    public CustomGestureListener(View view) {
        this.view = view;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getY() < e2.getY()) {
            return onSwipeDown();
        }
        else if (e1.getY() > e2.getY()) {
            return onSwipeUp();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    protected abstract boolean onSwipeUp();
    protected abstract boolean onSwipeDown();
}
