package com.example.painapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class ScaleGestureBinder extends ScaleGestureDetector {


    public ScaleGestureBinder(Context context, ScaleGestureListener scaleGestureListener) {
        super(context, scaleGestureListener);
    }

    public static ScaleGestureBinder bindView(Context context, DrawView targetView, ViewGroup viewGroup) {
        return new ScaleGestureBinder(context, targetView, viewGroup);
    }

    ScaleGestureBinder(Context context, DrawView targetView, ViewGroup viewGroup) {
        super(context, new ScaleGestureListener(targetView, viewGroup));
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(event);
            }
        });

    }
}
