package com.example.painapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    public static final int FILE_RESULT_CODE = 1;
    public static final int RESULT_OK = 1;
    private int requestCode;
    private int resultCode;
    private Intent data;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt1 = (Button) findViewById(R.id.goToDraw);
        bt1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                startActivity(intent);
            }
        });
        Button bt2 = (Button) findViewById(R.id.goToSelect);
        bt2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileSelect.class);
                startActivityForResult(intent,FILE_RESULT_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //Intent getIntent = getIntent();
        String filePath = data.getStringExtra("filePath");

        if (RESULT_OK == resultCode) {
            System.out.println("健康良好卡的话发卡行加快分解卡号发");;
            //Bundle bundle = null;
            if (data != null && (data.getExtras()) != null) {
                System.out.println("啊迪斯科解放哈尽快发回集安回复");

                //String filePath = getIntent.getStringExtra("filePath");
                TextView textView = (TextView) findViewById(R.id.filePath);
                textView.setText("The folder you choose is：" + filePath);
            }
        }

    }


}
