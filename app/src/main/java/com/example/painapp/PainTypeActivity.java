package com.example.painapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PainTypeActivity extends AppCompatActivity {

    Context context = this;

    private Spinner spinner_1 = null;
    private Spinner spinner_2 = null;
    private ArrayAdapter<String> arrayAdapter = null;

    private String[] painType = null;
    private static String[] painList = {"DRUCK", "STECHEND", "BOHREND", "DUMPF", "KOLIK", "BRENNEN"};
    private int num = 2;

    //private ArrayList<String> spinnerNames = new ArrayList<String>();
    //private ArrayList<String> buttonNames = new ArrayList<String>();
    //private String[] spinnerNames = {"spinner_3","spinner_4","spinner_5","spinner_6","spinner_7","spinner_8"};
    //private String[] buttonNames = {"button_3","button_4","button_5","button_6","button_7","button_8"};


    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_paintype);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, painList);
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

        final Button bt2 = (Button) findViewById(R.id.button_2);
        spinner_2 = (Spinner) findViewById(R.id.spinner_2);
        spinner_2.setAdapter(arrayAdapter);
        spinner_2.setVisibility(View.VISIBLE);
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string_2 = ((TextView) view).getText().toString();
                setButtonColor(bt2, string_2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageButton addButton = (ImageButton) findViewById(R.id.button_add);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        Button button_next = (Button)findViewById(R.id.button_next);
        button_next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PainTypeActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });

    }



    protected void addItem(){
        LinearLayout linearLayout = new LinearLayout(context);
        Spinner spinner = new Spinner(context);
        final Button button = new Button(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(3);
        LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params_0);
        int width_1 = getResources().getDimensionPixelSize(R.dimen.spinnerWidth);
        int height_1 = getResources().getDimensionPixelSize(R.dimen.spinnerHeight);
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(width_1, height_1,2.0f);
        spinner.setLayoutParams(params_1);
        int width_2 = getResources().getDimensionPixelSize(R.dimen.buttonWidth);
        int height_2 = getResources().getDimensionPixelSize(R.dimen.buttonHeight);
        LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(width_2, height_2,1.0f);
        button.setLayoutParams(params_2);
        LinearLayout layout = (LinearLayout)findViewById(R.id.AllListLayout);
        layout.addView(linearLayout);
        linearLayout.addView(spinner);
        linearLayout.addView(button);
        dropBoxButton(spinner,button);
    }

    protected void dropBoxButton(Spinner spinner, final Button button){
        //adapter的下拉列表风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    protected void setButtonColor(Button button, String string) {
        switch (string) {
            case "DRUCK":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_druck, null));
                break;
            case "BOHREND":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_bohrend, null));
                break;
            case "BRENNEN":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_brennen, null));
                break;
            case "DUMPF":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_dumpf, null));
                break;
            case "KOLIK":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_kolik, null));
                break;
            case "STECHEND":
                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_stechend, null));
                break;
        }

    }


}
