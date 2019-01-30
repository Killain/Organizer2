package com.killain.organizer.packages.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SwipeableRelativeLayout extends RelativeLayout {

    private GestureDetector mGestureDetector;

    public SwipeableRelativeLayout(Context context) {
        super(context);
    }

    public SwipeableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        mGestureDetector = gestureDetector;
    }
}
