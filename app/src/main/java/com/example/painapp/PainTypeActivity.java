package com.example.painapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PainTypeActivity extends AppCompatActivity {

    Context context = this;

    private Spinner spinner_1 = null;
    private Spinner spinner_2 = null;
    private ArrayAdapter<String> arrayAdapter = null;

    private String[] painType = null;
    private ArrayList<String> typeList = new ArrayList<String>();
    //private int num = 2;
    private Handler handler = new Handler();


    private ArrayList<String> typeListFinal = new ArrayList<String>();
    private ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    private ArrayList<String> colrList = new ArrayList<String>();

    private Map<String, Integer> map = new HashMap<String, Integer>();


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_paintype);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.alpha);


        colrList = Container.colorList;
        typeList = Container.typeList;

        for (int i = 0; i < typeList.size(); i++) {
            String color = colrList.get(i);
            map.put(typeList.get(i), Color.parseColor(color));
        }


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Button bt1 = (Button) findViewById(R.id.button_1);
        spinner_1 = (Spinner) findViewById(R.id.spinner_1);
        spinner_1.setAdapter(arrayAdapter);
        spinner_1.setVisibility(View.VISIBLE);
        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string_1 = ((TextView) view).getText().toString();
                setButtonColor(bt1, string_1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinners.add(spinner_1);


        ImageButton addButton = (ImageButton) findViewById(R.id.button_add);
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        addItem();
                }
                return false;
            }
        });


        final RWList RWList = new RWList();
        final DialogUtils dialogUtils = new DialogUtils();
        ImageButton button_newType = (ImageButton) findViewById(R.id.button_newType);
        button_newType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_newType.startAnimation(animation);
                dialogUtils.addPasswordDialog(context);
            }
        });

        ImageButton button_next = (ImageButton) findViewById(R.id.button_next);
        button_next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_next.startAnimation(animation);
                Set set = new HashSet();
                for (Spinner spinner : spinners) {
                    typeListFinal.add(spinner.getSelectedItem().toString());
                    set.add(spinner.getSelectedItem().toString());
                }
                if (typeListFinal.size() != set.size()) {
                    Toast toast = Toast.makeText(context, "The selected type has duplicates!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    typeListFinal.clear();
                    set.clear();
                } else {
                    Intent intent = new Intent(PainTypeActivity.this, DrawActivity.class);
                    Map<String, Integer> mapFinal = new HashMap<String, Integer>();
                    for (String string : typeListFinal) {
                        mapFinal.put(string, map.get(string));
                    }


                    intent.putExtra("map", (Serializable) mapFinal);

                    //intent.putStringArrayListExtra("typeList",typeListFinal);
                    startActivity(intent);
                }

            }
        });
    }

    protected void addItem() {
        final LinearLayout linearLayout = new LinearLayout(context);
        Spinner spinner = new Spinner(context);
        final Button button = new Button(context);
        final Button button_delete = new Button(context);
        button_delete.setText("DELETE");
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(6);
        LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params_0);
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4.0f);
        spinner.setLayoutParams(params_1);
        params_1.setMargins(2, 2, 2, 2);

        LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        params_2.setMargins(2, 2, 2, 2);
        button.setLayoutParams(params_2);
        button_delete.setLayoutParams(params_2);
        LinearLayout layout = (LinearLayout) findViewById(R.id.AllListLayout);
        layout.addView(linearLayout);
        linearLayout.addView(spinner);
        linearLayout.addView(button);
        linearLayout.addView(button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutAll = (LinearLayout) findViewById(R.id.AllListLayout);
                layoutAll.removeView(linearLayout);
                spinners.remove(spinner);
            }
        });
        dropBoxButton(spinner, button);
        spinners.add(spinner);
    }

    protected void dropBoxButton(Spinner spinner, final Button button) {
        //adapter的下拉列表风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string_1 = ((TextView) view).getText().toString();
                setButtonColor(button, string_1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void setButtonColor(Button button, String string) {
        int num = map.get(string);
        button.setBackgroundColor(num);

    }


}
