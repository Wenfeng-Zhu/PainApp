package com.example.painapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class DrawActivity extends AppCompatActivity {
    public Integer[] x_array;
    public Integer[] y_array;
    private int num;
    //Context context;
    StringBuilder strb = new StringBuilder();

    private float proportion = (float)1.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        //System.out.println("测试1");
        init();

    }

    private void init() {


        //System.out.println("测试2");
        LinearLayout layout = (LinearLayout) findViewById(R.id.root);
        //System.out.println("测试3");
        //PaintDraw paintDraw = new PaintDraw(this);


        final DrawView view = new DrawView(this);

        view.setMinimumHeight(Math.round(1169*proportion));
        view.setMinimumWidth(Math.round(827*proportion));



        Button bt1 = (Button) findViewById(R.id.Druck);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.BLACK);
            }
        });
        Button bt2 = (Button) findViewById(R.id.Stechend);
        bt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.RED);
            }
        });
        Button bt3 = (Button) findViewById(R.id.Bohrend);
        bt3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.GREEN);
            }
        });
        Button bt4 = (Button) findViewById(R.id.Dumpf);
        bt4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.BLUE);
            }
        });
        Button bt5 = (Button) findViewById(R.id.Kolik);
        bt5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(Color.GRAY);
            }
        });
        Button bt6 = (Button) findViewById(R.id.Brennen);
        bt6.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setColor(0xFFFFC0CB);
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

        for (String i: view.getContext().fileList()){
            System.out.println(i);
            String target = i.substring(i.length()-9,i.length());
            System.out.println(target);
        }

        Button bt7 = (Button) findViewById(R.id.save);
        bt7.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //导出json文件
                SaveJson saveJson = new SaveJson();
                //System.out.println("可就是大富豪话费卡会尽快发"+view.getContext().getFilesDir().getAbsolutePath());
                saveJson.exportJson(view.getmBitmap(),proportion,view.getContext().getFilesDir().getAbsolutePath() ,"patient002");


                //System.out.println("埃里克东方航空拉发回来"+view.getContext().getFilesDir().getAbsolutePath());

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
