package com.example.painapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

class ScrollGestureBinder extends GestureDetector {
    private ScrollGestureListener scrollGestureListener;
    public ScrollGestureBinder(Context context, ScrollGestureListener listener) {
        super(context, listener);
        scrollGestureListener = listener;
    }

}
