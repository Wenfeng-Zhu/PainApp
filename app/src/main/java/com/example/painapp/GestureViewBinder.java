package com.example.painapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class GestureViewBinder {
    private ViewGroup viewGroup;
    private DrawView drawView;
    private float dist;
    private ScaleGestureListener scaleGestureListener ;
    private ScrollGestureListener scrollGestureListener;
    private ScaleGestureBinder scaleGestureBinder ;
    private ScrollGestureBinder scrollGestureBinder;
    GestureViewBinder(Context context, ViewGroup viewGroup, DrawView drawView) {
        scaleGestureListener = new ScaleGestureListener(drawView,viewGroup);
        scrollGestureListener = new ScrollGestureListener(drawView,viewGroup);
        scaleGestureBinder = new ScaleGestureBinder(context, scaleGestureListener);
        scrollGestureBinder = new ScrollGestureBinder(context, scrollGestureListener);

        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    return scrollGestureBinder.onTouchEvent(event);
                } else if (event.getPointerCount() == 2) {
                    scrollGestureListener.setScale(scaleGestureListener.getScale());
                    return scaleGestureBinder.onTouchEvent(event);
                }
                return false;
            }
        });
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


}
