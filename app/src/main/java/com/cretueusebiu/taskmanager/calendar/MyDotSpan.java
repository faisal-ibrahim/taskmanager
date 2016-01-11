package com.cretueusebiu.taskmanager.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

public class MyDotSpan implements LineBackgroundSpan {
    private final float radius;
    private final int color;
    private final int index;
    private int total = 1;

    public MyDotSpan(float radius, int color, int index, int total) {
        this.radius = radius;
        this.color = color;
        this.index = index;
        this.total = total;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();

        if (color != 0) {
            paint.setColor(color);
        }

        float cx = (left + right);

        if (total == 1) {
            cx = cx / 2;
        } else {
            cx = (cx / total) + ((index + 1) * 12);
        }

        canvas.drawCircle(cx, bottom + radius, radius, paint);

        paint.setColor(oldColor);
    }
}
