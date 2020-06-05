package com.example.painapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


public class MainActivity extends Activity {
    public static final int FILE_RESULT_CODE = 1;
    public static final int RESULT_OK = 1;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Constant.proportion = (float) (screenWidth)/(float) (827);


        //Button pointing to Draw Page
        Button bt1 = (Button) findViewById(R.id.goToDraw);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PainTypeActivity.class);
                startActivity(intent);
            }
        });
        //Button pointing to File Page
        Button bt2 = (Button) findViewById(R.id.goToSelect);
        bt2.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

//                try {
//                    String filePath = context.getFilesDir().getAbsolutePath()+File.separator+"colorList.txt";
//                    File typeList = new File(filePath);
//                    typeList.createNewFile();
//                    try (FileWriter writer = new FileWriter(typeList);BufferedWriter out = new BufferedWriter(writer)){
//                        String[] list = context.getResources().getStringArray(R.array.colorList);
//                        for (String string:list){
//                            out.write(string.toUpperCase()+"\r\n");
//                        }
//                        out.flush();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                Intent intent = new Intent(MainActivity.this, FileSelectActivity.class);
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

                Constant.fileName = str.substring(filePath.lastIndexOf("_")+1,filePath.length()-5);

                Constant.ifImport = true;
            }
        }
    }


}
