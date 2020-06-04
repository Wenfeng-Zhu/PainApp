package com.example.painapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorLong;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.Serializable;
import java.sql.SQLOutput;
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
    private ArrayList<String> typeList= new ArrayList<String>();
    //private int num = 2;
    private Handler handler = new Handler();

    //private int typenum = 2;
    private ArrayList<String> typeListFinal = new ArrayList<String>();
    private ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    private ArrayList<String> colors = new ArrayList<String>();

    private Map<String,Integer> map = new HashMap<String, Integer>();

    //private ArrayList<String> spinnerNames = new ArrayList<String>();
    //private ArrayList<String> buttonNames = new ArrayList<String>();
    //private String[] spinnerNames = {"spinner_3","spinner_4","spinner_5","spinner_6","spinner_7","spinner_8"};
    //private String[] buttonNames = {"button_3","button_4","button_5","button_6","button_7","button_8"};


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_paintype);


        RWList rwList = new RWList();
        colors = rwList.readList(context,"colorList.txt");
        typeList = rwList.readList(context,"typeList.txt");

        for(int i = 0;i<typeList.size();i++){
            String color = colors.get(i);
            map.put(typeList.get(i),Color.parseColor(color));
        }
        if(Constant.ifImport){
            System.out.println("导入文件名测试——————————"+Constant.fileName);
            Constant.color = map.get(Constant.fileName);
            System.out.println("导入文件颜色测试————————"+Constant.color);
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
        spinners.add(spinner_2);

        ImageButton addButton = (ImageButton) findViewById(R.id.button_add);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        final RWList RWList = new RWList();
        final DialogUtils dialogUtils = new DialogUtils();
        Button button_newType = (Button)findViewById(R.id.button_newType);
        button_newType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.passwordDialog(context);
                //finish();
//                if (dialogUtils.correctOfPassword){
//
//                    typeList.writeList(context,dialogUtils.typename);
//                    Toast.makeText(PainTypeActivity.this, "Add Successfully!", Toast.LENGTH_SHORT).show();
//
//
//
//                }

            }
        });
//        if (dialogUtils.state){
//            System.out.println("森林的砍伐韩国进口粮食的护法国会");
//            Intent intent = new Intent(PainTypeActivity.this,PainTypeActivity.class);
//            startActivity(intent);
//            PainTypeActivity.this.finish();
//            dialogUtils.state = false;
//        }



        Button button_next = (Button)findViewById(R.id.button_next);
        button_next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set set = new HashSet();
                for (Spinner spinner:spinners){
                    typeListFinal.add(spinner.getSelectedItem().toString());
                    set.add(spinner.getSelectedItem().toString());
                }
                if(typeListFinal.size()!=set.size()){
                    Toast toast = Toast.makeText(context, "The selected type has duplicates!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    typeListFinal.clear();
                    set.clear();
                }
                else {
                    Intent intent = new Intent(PainTypeActivity.this, DrawActivity.class);
                    Map<String,Integer> mapFinal = new HashMap<String, Integer>();
                    for (String string:typeListFinal){
                        mapFinal.put(string,map.get(string));
                    }


                    intent.putExtra("map",(Serializable)mapFinal);

                    //intent.putStringArrayListExtra("typeList",typeListFinal);
                    startActivity(intent);
                }

            }
        });



    }



    protected void addItem(){
        final LinearLayout linearLayout = new LinearLayout(context);
        Spinner spinner = new Spinner(context);
        final Button button = new Button(context);
        final Button button_delete = new Button(context);
        button_delete.setText("DELETE");
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
        linearLayout.addView(button_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutAll = (LinearLayout)findViewById(R.id.AllListLayout);
                layoutAll.removeView(linearLayout);
            }
        });
        dropBoxButton(spinner,button);
        spinners.add(spinner);
    }

    protected void dropBoxButton(Spinner spinner, final Button button){
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





//        for (int i = 0;i<itemList.size();i+=2){
//
//        }
//        switch (string) {
//            case "DRUCK":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_3, null));
//                break;
//            case "BOHREND":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_1, null));
//                break;
//            case "BRENNEN":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_2, null));
//                break;
//            case "DUMPF":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_4, null));
//                break;
//            case "KOLIK":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_5, null));
//                break;
//            case "STECHEND":
//                button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_1, null));
//                break;
//        }

    }


}
