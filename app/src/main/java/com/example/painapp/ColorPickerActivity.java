package com.example.painapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class ColorPickerActivity extends Dialog {


    private static final String SAVED_STATE_KEY_COLOR = "saved_state_key_color";
    private static final int INITIAL_COLOR = 0xFFFF8000;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;
    private float centerX;
    private float centerY;
    private float radius;

    private int sliderMargin;
    private int sliderHeight;

    public Button getClose() {
        return close;
    }

    public void setClose(Button close) {
        this.close = close;
    }

    private Button close;

    public ColorPickerActivity(@NonNull Context context) {
        super(context);

        setContentView(R.layout.activity_colorpick);

        LinearLayout colorPickerView = (LinearLayout) findViewById(R.id.colorPicker);
        LinearLayout colorslider = (LinearLayout)findViewById(R.id.Slider);
        View pickedColor = (View)findViewById(R.id.pickedColor);
        close = (Button)findViewById(R.id.confirmColor);
        TextView colorHex = (TextView)findViewById(R.id.colorHex);
        ColorWheel colorWheel = new ColorWheel(context);
        BrightnessSliderView brightnessSliderView = new BrightnessSliderView(context);


        float density = context.getResources().getDisplayMetrics().density;
        int margin = (int) (8 * density);
        sliderMargin = 2 * margin;
        sliderHeight = (int) (50 * density);





        centerX = colorWheel.getCenterX();
        centerY = colorWheel.getCenterY();
        radius = colorWheel.getRadius();
        colorPickerView.setMinimumHeight(Math.round(2*radius));

        ColorWheelSelector selector = new ColorWheelSelector(context);
        colorPickerView.addView(colorWheel);
        colorPickerView.addView(selector);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);
        params.topMargin = sliderMargin;
        colorslider.addView(brightnessSliderView, 0, params);



        colorWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_MOVE:
                        setViewColor(pickedColor,colorWheel.getColor());
                        brightnessSliderView.setBaseColor(colorWheel.getColor());
                        setColorHex(colorHex,colorWheel.getColor());
                        color = colorWheel.getColor();
                        System.out.println("MOVE_TOUCH事件监听————————————————");
                }
                return false;
            }
        });

        brightnessSliderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_MOVE:
                        setViewColor(pickedColor,brightnessSliderView.getColor());
                        setColorHex(colorHex,brightnessSliderView.getColor());
                        color = colorWheel.getColor();
                        System.out.println("MOVE_TOUCH事件监听————————————————");
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(SAVED_STATE_KEY_COLOR,color);
//    }

    private String colorHex(int color){
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X%02X", a, r, g, b);

    }

    private void setViewColor(View view,int color){
        view.setBackgroundColor(color);
    }
    private void setColorHex(TextView textView,int color){
        textView.setText(colorHex(color));
    }

}

