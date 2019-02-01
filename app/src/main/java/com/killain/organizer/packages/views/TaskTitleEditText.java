package com.killain.organizer.packages.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class TaskTitleEditText extends android.support.v7.widget.AppCompatEditText {
    public TaskTitleEditText(Context context) {
        super(context);
        init();
    }

    public TaskTitleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskTitleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        setTypeface(tf);
    }


}
