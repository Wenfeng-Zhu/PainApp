package com.example.painapp;

import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

    private DrawView targetView;
    private float scaleTemp = 1;

    public void setScale(float scale) {
        this.scale = scale;
    }

    private float scale;
    private static float maxScale =  3.0f;

    ScaleGestureListener(DrawView targetView, ViewGroup viewGroup) {
        this.targetView = targetView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = detector.getScaleFactor();
        scale = scaleTemp * scale;
        targetView.setScaleX(scale);
        targetView.setScaleY(scale);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaleTemp = scale;
    }

    public ScaleGestureListener getScale() {
        return this;
    }
}

