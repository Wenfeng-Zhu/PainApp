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

        //System.out.println("导入数据函数测试！！！！！！！");
        for (String string : map.keySet()) {
            String fileName = "Patient"+ fileNum+"_"+string;
            for (int i = 0; i < Math.round(827 * proportion); i++) {
                for (int j = 0; j < Math.round(1169 * proportion); j++) {

                    if (map.get(string).getPixel(i, j) != 0) {
                        System.out.println("————————————Bitmap测试————————————————");
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
        // 标记文件生成是否成功
        System.out.println("生成文件函数测试开始！！！！！！");
        boolean flag = true;
        String jsonString;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";
        System.out.println("生成文件测试节点——1");

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            System.out.println("生成文件测试节点——2");
            file.createNewFile();
            System.out.println("生成文件测试节点——2.1");
            // 格式化json字符串
            jsonString = SaveJson.formatJson(array_all);
            System.out.println("生成文件测试节点——3");
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            System.out.println("生成文件测试节点——4");
            write.write(jsonString);
            System.out.println("生成文件测试节点——5");
            write.flush();
            System.out.println("生成文件测试节点——6");
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        System.out.println("生成文件成功标识！！！！！！！！！！");
        return flag;
    }

    public static String formatJson(ArrayList<Integer> array_all) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0;i<array_all.size()-1;i++){
            stringBuilder.append(array_all.get(i));
            stringBuilder.append(",");
        }
        stringBuilder.append(array_all.get(array_all.size()-1));
        stringBuilder.append("]");
        return stringBuilder.toString();


//        String result = "[";
//        for (Object tmp : array_all) {
//            result += tmp + ",";
//        }
//        result = result.substring(0,result.length()-1);
//        result += "]";
//
//        return result;
    }
}

