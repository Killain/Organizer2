package com.killain.organizer.packages.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TasksFragmentLineView extends View {

    private Canvas canvas;
    private Paint paint = new Paint();
    private float relativeTop;
    private float relativeBottom;
    private float relativeRight;
    private float relativeLeft;
    private float x, y;

    public TasksFragmentLineView(Context context) {
        super(context);
    }

    public TasksFragmentLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TasksFragmentLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TasksFragmentLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        draw();
        super.onDraw(canvas);
    }

    private void draw() {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5f);
        canvas.drawPoint(x, y, paint);
    }

    public void setRelativeTop(float relativeTop) {
        this.relativeTop = relativeTop;
    }

    public void setRelativeBottom(float relativeBottom) {
        this.relativeBottom = relativeBottom;
    }

    public void setRelativeRight(float relativeRight) {
        this.relativeRight = relativeRight;
    }

    public void setRelativeLeft(float relativeLeft) {
        this.relativeLeft = relativeLeft;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
}
