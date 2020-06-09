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
    //记录当前路径下 的所有文件的数组
    File currentParent;
    //记录当前路径下的所有文件的文件数组
    File[] currentFiles;
    public static final int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //获取列出全部文件发ListView
        listview = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.mPath);
        //获取软件文件目录"/data/user/0/com.example.painapp/files"
//        File root = new File(this.getFilesDir().getAbsolutePath());
        File root = new File(getExternalFilesDir("").getAbsolutePath());
        //如果路径存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();//获取root目录下的所有文件
            //使用当前目录下的全部文件，文件夹来填充ListView
            inflateListView(currentFiles);
        }//if
        //为ListView的列表项的单击事件绑定监视器
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentFiles[position].isFile()) {
                    //Intent intent = OpenFile.openFile(currentFiles[position].getPath());
                    Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
                    String filePath = currentFiles[position].getPath();
                    //Bundle bundle = new Bundle();

                    //bundle.putString("filePath",filePath);
                    String suffix = filePath.substring(filePath.length()-4);
                    if(suffix.equals("json")){
                        intent.putExtra("filePath", filePath);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else {
                        Toast.makeText(FileSelectActivity.this, "Please select a json file!", Toast.LENGTH_SHORT).show();
                    }

                    //startActivity(intent);
                } else {
                    //获取currentFiles[position]路径下的所有文件
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(FileSelectActivity.this, "empty!", Toast.LENGTH_SHORT).show();
                    }//if
                    else {
                        //获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                        currentParent = currentFiles[position];
                        //保存当前文件夹内的全部问价和文件夹
                        currentFiles = tmp;
                        inflateListView(currentFiles);
                    }//else
                }//else
            }//onItemClick
        });
        Button cancel = (Button) findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
                startActivity(intent);
            }//onClick
        });
    }


    //更新列表
    private void inflateListView(File[] files) {
        if (files.length == 0)
            Toast.makeText(FileSelectActivity.this, "Folder is empty", Toast.LENGTH_SHORT).show();
        else {
            //创建一个List集合,List集合的元素是Map
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                //如果当前File是文件夹，使用folder图标；否则使用file图标
                if (files[i].isDirectory()) listItem.put("icon", R.drawable.icons_folder);//文件夹
                else
                    listItem.put("icon", R.drawable.icons_file);//文件
                listItem.put("fileName", files[i].getName());
                //添加List项
                listItems.add(listItem);
            }//for
            //创建一个SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.files_list, new String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.filename});
            //位ListView设置Adpter
            listview.setAdapter(simpleAdapter);
            try {
                textView.setText("The current path is:" + "\n" + currentParent.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //监听手机自带返回键
    public void onBackPressed() {
        Intent intent = new Intent(FileSelectActivity.this, MainActivity.class);
        startActivity(intent);

    }


}


