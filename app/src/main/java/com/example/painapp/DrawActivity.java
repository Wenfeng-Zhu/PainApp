package com.example.painapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class DrawActivity extends AppCompatActivity {
    private float proportion = Container.proportion;
    private int Pen = 1;
    private int Eraser = 2;
    Context context = this;
    private Path lastPath;
    private Map<String, Integer> map;

    private ScaleGestureDetector mScaleGestureDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw);
        DialogUtils dialogUtils = new DialogUtils();
        init();
        dialogUtils.tipsDialog(this, "You need to know before painting");

    }

    protected void addItem(ArrayList<Button> buttonList) {

        LinearLayout buttonsArea = (LinearLayout) findViewById(R.id.buttonsArea);

        LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(3);
        linearLayout.setLayoutParams(params_0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.buttonWidth), LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

        final Button bt1 = new Button(this);
        bt1.setLayoutParams(params);
        final Button bt2 = new Button(this);
        bt2.setLayoutParams(params);
        final Button bt3 = new Button(this);
        bt3.setLayoutParams(params);

        linearLayout.addView(bt1);
        linearLayout.addView(bt2);
        linearLayout.addView(bt3);
        buttonList.add(bt1);
        buttonList.add(bt2);
        buttonList.add(bt3);
        buttonsArea.addView(linearLayout);

    }


    private ScaleGestureBinder scaleGestureBinder;
    private ScaleGestureListener scaleGestureListener;
    private GestureViewBinder gestureViewBinder;
    private ScrollGestureListener scrollGestureListener;
    private GestureDetector gestureDetector;


    private void init() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.root);
        int height = layout.getHeight();

//      ScrollView scrollView = (ScrollView) findViewById(R.id.drawArea);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        final DrawView view = new DrawView(this);
//        view.setMinimumHeight(Math.round(1169 * proportion));
//        view.setMinimumWidth(Math.round(827 * proportion));
        view.setLayoutParams(params);
        //ArrayList<String> typeList = this.getIntent().getStringArrayListExtra("typeList");
        map = (Map<String, Integer>) this.getIntent().getSerializableExtra("map");
        view.setMap(map);
        view.invalidate();
        layout.addView(view,params);


        scaleGestureBinder.bindView(this,view,layout);
        gestureViewBinder = new GestureViewBinder(this,layout,view);

        //scrollGestureListener = new ScrollGestureListener(view);
        //gestureViewBinder = new GestureViewBinder(context,scrollGestureListener,layout,view);


        ArrayList<Button> buttonList = new ArrayList<Button>();
        final int num = map.size();
        double time = Math.ceil((double) num / 3.0);
        for (int i = 0; i < time; i++) {
            addItem(buttonList);
        }
        int k = 0;
        for (final Map.Entry<String, Integer> map1 : map.entrySet()) {
            if (k == 0) {
                view.setPaintColor(map1.getValue());
            }
            buttonList.get(k).setText(map1.getKey());
            buttonList.get(k).setBackgroundColor(map1.getValue());

            int finalK = k;
            buttonList.get(k).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        buttonList.get(finalK).setBackgroundColor(Color.parseColor("#FFADC1D4"));
                        view.setPaintColor(map1.getValue());
                        view.setMode(Pen);
                        view.pressed(true);
                        view.invalidate();
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        buttonList.get(finalK).setBackgroundColor(map1.getValue());
                    }
                    return false;
                }
            });
            k++;
        }


        Button button_undo = (Button) findViewById(R.id.button_undo);
        button_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!view.getPathList().isEmpty()) {

                    LinkedHashMap<Path, Integer> pathList = view.getPathList();
                    //System.out.println("PathList非同测试"+(pathList.get(0)==pathList.get(1)));
                    Iterator<Path> iterator = pathList.keySet().iterator();
                    while (iterator.hasNext()) {
                        lastPath = iterator.next();
                    }
                    if (pathList.get(lastPath) == view.getmEraserPaint().getColor()) {
                        Map<String, ArrayList<Path>> PathCollection = view.getPathCollection();
                        ArrayList<Path> paths = PathCollection.get("eraser");
                        paths.remove(paths.size() - 1);
                        PathCollection.put("eraser", paths);
                        view.setPathCollection(PathCollection);

                        pathList.remove(lastPath);
                        view.setPathList(pathList);
                        //view.setUndo(true);
                        view.invalidate();
                    } else {
                        Map<String, ArrayList<Path>> PathCollection = view.getPathCollection();
                        ArrayList<Path> paths = PathCollection.get(view.getTmap().get(pathList.get(lastPath)));
                        paths.remove(paths.size() - 1);
                        PathCollection.put(view.getTmap().get(pathList.get(lastPath)), paths);
                        view.setPathCollection(PathCollection);
                        pathList.remove(lastPath);
                        view.setPathList(pathList);
                        //view.setUndo(true);
                        view.invalidate();
                    }
                } else {
                    Toast toast = Toast.makeText(context, "There is nothing to undo ~ (×_×) ~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        Button button_eraser = (Button) findViewById(R.id.button_eraser);
        button_eraser.setOnClickListener(new Button.OnClickListener() {
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


        Button button_save = (Button) findViewById(R.id.save);
        button_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (view.saveScreen()) {
                    DialogUtils saveDialog = new DialogUtils();
                    saveDialog.savePasswordDialog(context, view.getSbMap(), proportion, view.getContext().getFilesDir().getAbsolutePath());
                } else {
                    Toast toast = Toast.makeText(context, "There is nothing to save ~ (×_×) ~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });
        Button button_back = (Button) findViewById(R.id.back);
        button_back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DrawActivity.this, MainActivity.class);
                startActivity(intent);
                Container.ifImport = false;
            }
        });
    }


}
