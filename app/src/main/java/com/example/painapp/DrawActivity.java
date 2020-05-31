package com.example.painapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class DrawActivity extends AppCompatActivity {


    private float proportion = Constant.proportion;
    private int Pen = 1;
    private int Eraser = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);
        DialogUtils dialogUtils = new DialogUtils();

        init();
        dialogUtils.showCompleteDialog(this, "You need to know before painting");


    }

    private void init() {
        ScrollView scrollView = (ScrollView)findViewById(R.id.drawArea);

        LinearLayout layout = (LinearLayout) findViewById(R.id.root);
        final DrawView view = new DrawView(this);

        view.setMinimumHeight(Math.round(1169 * proportion));
        view.setMinimumWidth(Math.round(827 * proportion));


        Button bt1 = (Button) findViewById(R.id.Druck);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.BLACK);
                view.setMode(Pen);
            }
        });
        Button bt2 = (Button) findViewById(R.id.Stechend);
        bt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.RED);
                view.setMode(Pen);
            }
        });
        Button bt3 = (Button) findViewById(R.id.Bohrend);
        bt3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.GREEN);
                view.setMode(Pen);
            }
        });
        Button bt4 = (Button) findViewById(R.id.Dumpf);
        bt4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.BLUE);
                view.setMode(Pen);
            }
        });
        Button bt5 = (Button) findViewById(R.id.Kolik);
        bt5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.GRAY);
                view.setMode(Pen);
            }
        });
        Button bt6 = (Button) findViewById(R.id.Brennen);
        bt6.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(0xFFFFC0CB);
                view.setMode(Pen);
            }
        });
        Button bt7 = (Button) findViewById(R.id.button_eraser);
        bt7.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setMode(Eraser);

            }
        });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        view.invalidate();
        layout.addView(view);


        Button bt8 = (Button) findViewById(R.id.save);
        bt8.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveJson saveJson = new SaveJson();
                saveJson.exportJson(view.getmBitmap(), proportion, view.getContext().getFilesDir().getAbsolutePath(), "patient002");
            }
        });
        Button bt9 = (Button) findViewById(R.id.back);
        bt9.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DrawActivity.this, MainActivity.class);
                startActivity(intent);
                Constant.ifImport = false;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

}
