package com.example.painapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    public static final int FILE_RESULT_CODE = 1;
    public static final int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button pointing to Draw Page
        Button bt1 = (Button) findViewById(R.id.goToDraw);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });
        //Button pointing to File Page
        Button bt2 = (Button) findViewById(R.id.goToSelect);
        bt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileSelect.class);
                //When the new interface is closed, the data is returned.
                startActivityForResult(intent, FILE_RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Get data from the new interface
        String filePath = data.getStringExtra("filePath");
        Constant.filePath = filePath;
        if (RESULT_OK == resultCode) {
            //Bundle bundle = null;
            if (data != null && (data.getExtras()) != null) {
                TextView textView = (TextView) findViewById(R.id.filePath);
                //Show file path in Text View
                textView.setText("The folder you choose is：" + "\n" + filePath);
                //Determine the corresponding color according to the second half of the path string
                String str = filePath;
                String substr = str.substring(filePath.length() - 9);
                switch (substr) {
                    case "ruck.json":
                        Constant.color = Color.BLACK;
                        break;
                    case "hend.json":
                        Constant.color = Color.RED;
                        break;
                    case "rend.json":
                        Constant.color = Color.GREEN;
                        break;
                    case "umpf.json":
                        Constant.color = Color.BLUE;
                        break;
                    case "olik.json":
                        Constant.color = Color.GRAY;
                        break;
                    case "nnen.json":
                        Constant.color = 0xFFFFC0CB;
                        break;
                }
                Constant.ifImport = true;
            }
        }
    }


}
