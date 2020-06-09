package com.example.painapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class ScrollGestureListener extends GestureDetector.SimpleOnGestureListener  {

    private DrawView targetView;
    private float distanceXTemp = 1;
    private float distanceYTemp = 1;

    private ScaleGestureListener scaleGestureListener;
    public ScrollGestureListener(DrawView targetView, ViewGroup viewGroup){
        this.targetView = targetView;
    }

    //e1 is the MotionEvent before sliding,
    // e2 is the MotionEvent during sliding or after each small segment of sliding,
    // distanceX is the distance obtained by subtracting the starting sliding coordinate of the X axis from the sliding coordinate.
    // In this way, sliding to the right is a negative number. Sliding to the left is a positive number.
    // It should be noted here that distanceY is the sliding distance of the Y axis, and its characteristics are the same as distanceX.
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        distanceX = -distanceX;
        distanceY = -distanceY;
        distanceXTemp += distanceX;
        distanceYTemp += distanceY;
        targetView.setTranslationX(distanceXTemp);
        targetView.setTranslationY(distanceYTemp);
//        return super.onScroll(e1, e2, distanceX, distanceY);
        return false;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    public void setScale(ScaleGestureListener scale) {

    }
}
