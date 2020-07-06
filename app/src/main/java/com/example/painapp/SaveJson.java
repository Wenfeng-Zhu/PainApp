package com.example.painapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveJson {
    private static Context context;
    private Bitmap bitmap;


    private Map<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
    private ArrayList<Integer> array_X = new ArrayList<Integer>();
    private ArrayList<Integer> array_Y = new ArrayList<Integer>();

    public SaveJson() {
    }

    public void exportJson(Map<String, Bitmap> map, float proportion, String filePath, String fileNum) {

        for (String string : map.keySet()) {
            String fileName = "Patient" + fileNum + "_" + string;
            for (int i = 0; i < Math.round(827 * proportion); i++) {
                for (int j = 0; j < Math.round(1169 * proportion); j++) {

                    if (map.get(string).getPixel(i, j) != 0) {
                        array_X.add(Math.round(i / proportion));
                        array_Y.add(1169 - Math.round(j / proportion));
                    }
                }
            }
            ArrayList<Integer> array_All = (ArrayList<Integer>) array_X.clone();
            array_All.addAll(array_Y);
            array_X.clear();
            array_Y.clear();
            createJsonFile(array_All, filePath, fileName);
        }
    }

    public static boolean createJsonFile(ArrayList<Integer> array_all, String filePath, String fileName) {

        boolean flag = true;
        String jsonString;

        String fullPath = filePath + File.separator + fileName + ".json";

        try {

            File file = new File(fullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            jsonString = SaveJson.formatJson(array_all);

            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

    public static String formatJson(ArrayList<Integer> array_all) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < array_all.size() - 1; i++) {
            stringBuilder.append(array_all.get(i));
            stringBuilder.append(",");
        }
        stringBuilder.append(array_all.get(array_all.size() - 1));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

