package com.example.painapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class MainActivity extends Activity {
    public static final int FILE_RESULT_CODE = 1;
    public static final int RESULT_OK = 1;
    Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RWList rwList = new RWList();
        Container.colorList= rwList.readList(context,"colorList.txt");
        Container.typeList = rwList.readList(context,"typeList.txt");

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        Container.proportion = (float) (screenWidth*0.8)/(float) (827);

        ImageView imageView = (ImageView)findViewById(R.id.app_icon);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = screenWidth*2/3;
        params.height = screenWidth*2/3;

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

//The comment part is to read and write the color file and the name file again

//                try {
//                    String filePath = context.getFilesDir().getAbsolutePath()+File.separator+"typeList.txt";
//                    File typeList = new File(filePath);
//                    typeList.createNewFile();
//                    try (FileWriter writer = new FileWriter(typeList); BufferedWriter out = new BufferedWriter(writer)){
//                        String[] list = context.getResources().getStringArray(R.array.typeList);
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

        Button bt3 = (Button)findViewById(R.id.button_display);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Container.ifImport){
                    Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                    startActivity(intent);
                    Container.ifDisplay = true;
                    Container.ifRedraw = false;
                }
                else {
                    Toast toast = Toast.makeText(context, "Please select a file!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });

        Button bt4 = (Button)findViewById(R.id.button_redraw);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Container.ifImport){
                    Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                    startActivity(intent);
                    Container.ifRedraw = true;
                    Container.ifDisplay = false;

                }
                else {
                    Toast toast = Toast.makeText(context, "Please select a file!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Get data from the new interface
        String filePath = data.getStringExtra("filePath");
        Container.filePath = filePath;
        if (RESULT_OK == resultCode) {
            //Bundle bundle = null;
            if (data != null && (data.getExtras()) != null) {
                TextView textView = (TextView) findViewById(R.id.filePath);
                //Show file path in Text View
                textView.setText("The folder you choose is：" + "\n" + filePath);
                //Determine the corresponding color according to the second half of the path string
                String str = filePath;

                Container.typeName = str.substring(filePath.lastIndexOf("_")+1,filePath.length()-5);
                Container.typeColor = Color.parseColor(Container.colorList.get(Container.typeList.indexOf(Container.typeName)));

                Container.ifImport = true;
            }
        }
    }


}
