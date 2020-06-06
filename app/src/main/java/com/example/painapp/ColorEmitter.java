package com.example.painapp;

class ColorEmitter implements ColorMonitor{
    private int color;

    @Override
    public int getColor() {
        return color;
    }

    void onColor(int color) {
        this.color = color;
    }
}
