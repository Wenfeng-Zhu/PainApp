package com.example.painapp;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelect extends AppCompatActivity {

    ListView listview;
    TextView textView;
    //记录当前路径下 的所有文件的数组
    File currentParent;
    //记录当前路径下的所有文件的文件数组
    File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //获取列出全部文件发ListView
        listview = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.mPath);
        //获取系统的SD卡目录
        File root = new File("/mnt");
        //如果SD卡存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();//获取root目录下的所有文件
            //使用当前陆慕下的全部文件，文件夹来填充ListView
            inflateListView(currentFiles);
        }//if
        //为ListView的列表项的单击事件绑定监视器
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //用户点击了文件，则调用手机已安装软件操作该文件
                if (currentFiles[position].isFile()) {
                    Intent intent = OpenFile.openFile(currentFiles[position].getPath());
                    startActivity(intent);
                } else {
                    //获取currentFiles[position]路径下的所有文件
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(FileSelect.this, "empty!", Toast.LENGTH_SHORT).show();
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
        Button parent = (Button) findViewById(R.id.buttonConfirm);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onbey();
            }//onClick
        });
    }//onCraete

    //返回上层菜单
    private void onbey() {
        try {
            if (!"/mnt".equals(currentParent.getCanonicalPath())) {
                //获取上一层目录
                currentParent = currentParent.getParentFile();
                //列出当前目录下的所有文件
                currentFiles = currentParent.listFiles();
                //再次更新ListView
                inflateListView(currentFiles);
            }
            else{
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("PROMPT")
                        .setMessage("Determine the file you selected.")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }

    //更新列表
    private void inflateListView(File[] files) {
        if (files.length == 0)
            Toast.makeText(FileSelect.this, "sd卡不存在", Toast.LENGTH_SHORT).show();
        else {
            //创建一个List集合,List集合的元素是Map
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                //如果当前File是文件夹，使用folder图标；否则使用file图标
                if (files[i].isDirectory()) listItem.put("icon", R.drawable.icons_black);//文件夹
                    //else if(files[i].isFi)
                else listItem.put("icon", R.drawable.icons_white);//文件
                listItem.put("fileName", files[i].getName());
                //添加List项
                listItems.add(listItem);
            }//for
            //创建一个SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.activity_file_row, new String[]{"icon", "fileName"},
                    new int[]{R.id.icon, R.id.filename});
            //位ListView设置Adpter
            listview.setAdapter(simpleAdapter);
            try {
                textView.setText("当前路径为：" + currentParent.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }//catch
        }//esle
    }//inflateListView

    //监听手机自带返回键
    public void onBackPressed() {
        onbey();
    }


}


