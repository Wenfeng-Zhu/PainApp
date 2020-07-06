package com.example.painapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelectActivity extends AppCompatActivity {

    ListView listview;
    TextView textView;
    File currentParent;
    File[] currentFiles;
    public static final int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        listview = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.mPath);

        File root = new File(getExternalFilesDir("").getAbsolutePath());
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();
            inflateListView(currentFiles);
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isFile()) {
                    Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
                    String filePath = currentFiles[position].getPath();
                    String suffix = filePath.substring(filePath.length()-4);
                    if(suffix.equals("json")){
                        intent.putExtra("filePath", filePath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        Toast.makeText(FileSelectActivity.this, "Please select a json file!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(FileSelectActivity.this, "empty!", Toast.LENGTH_SHORT).show();
                    }//if
                    else {
                        currentParent = currentFiles[position];
                        currentFiles = tmp;
                        inflateListView(currentFiles);
                    }
                }
            }
        });
        Button cancel = (Button) findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private void inflateListView(File[] files) {
        if (files.length == 0)
            Toast.makeText(FileSelectActivity.this, "Folder is empty", Toast.LENGTH_SHORT).show();
        else {

            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> listItem = new HashMap<String, Object>();

                if (files[i].isDirectory()) listItem.put("icon", R.drawable.icons_folder);//文件夹
                else
                    listItem.put("icon", R.drawable.icons_file);//文件
                listItem.put("fileName", files[i].getName());

                listItems.add(listItem);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.files_list, new String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.filename});

            listview.setAdapter(simpleAdapter);
            try {
                textView.setText("The current path is:" + "\n" + currentParent.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
        startActivity(intent);

    }


}


