package com.example.painapp;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RWList {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected ArrayList<String> readList(Context context,String fileName) {
        ArrayList<String> itemList = new ArrayList<String>();
        String filePath = context.getFilesDir().getAbsolutePath()+File.separator+fileName;
        try (FileReader reader = new FileReader(filePath);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                itemList.add(line.toUpperCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void writeList(Context context, String typename,String fileName){
        try {
            ArrayList<String> arrayList = readList(context,fileName);
            arrayList.add(typename);
            String filePath = context.getFilesDir().getAbsolutePath()+File.separator+fileName;
            File writeName = new File(filePath); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                for (String string:arrayList){
                    out.write(string.toUpperCase()+"\r\n");
                }
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
