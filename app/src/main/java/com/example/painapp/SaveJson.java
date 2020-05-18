package com.example.painapp;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class SaveJson {
    private Bitmap mBitmap;
    ArrayList array_druck_X = new ArrayList();
    ArrayList array_druck_Y = new ArrayList();
    ArrayList array_stechend_X = new ArrayList();
    ArrayList array_stechend_Y = new ArrayList();
    ArrayList array_bohrend_X = new ArrayList();
    ArrayList array_bohrend_Y = new ArrayList();
    ArrayList array_dumpf_X = new ArrayList();
    ArrayList array_dumpf_Y = new ArrayList();
    ArrayList array_kolik_X = new ArrayList();
    ArrayList array_kolik_Y = new ArrayList();
    ArrayList array_brennen_X = new ArrayList();
    ArrayList array_brennen_Y = new ArrayList();

    public SaveJson(){
    }
    public void exportJson(Bitmap bitmap,float proportion,String fileName){
        mBitmap = bitmap;
        for (int i = 0; i< Math.round(827*proportion);i++){
            for (int j = 0;j<Math.round(1169*proportion);j++){
                if (mBitmap.getPixel(i,j)!=0){
                    if (mBitmap.getPixel(i,j) == Color.BLACK){
                        array_druck_X.add(Math.round(i/proportion));
                        array_druck_Y.add(Math.round(j/proportion));
                    }
                    else if (mBitmap.getPixel(i,j) == Color.RED){
                        array_stechend_X.add(Math.round(i/proportion));
                        array_stechend_Y.add(Math.round(j/proportion));
                    }
                    else if (mBitmap.getPixel(i,j) == Color.GREEN){
                        array_bohrend_X.add(Math.round(i/proportion));
                        array_bohrend_Y.add(Math.round(j/proportion));
                    }
                    else if (mBitmap.getPixel(i,j) == Color.BLUE){
                        array_dumpf_X.add(Math.round(i/proportion));
                        array_dumpf_Y.add(Math.round(j/proportion));
                    }
                    else if (mBitmap.getPixel(i,j) == Color.GRAY){
                        array_kolik_X.add(Math.round(i/proportion));
                        array_kolik_Y.add(Math.round(j/proportion));
                    }
                    else if (mBitmap.getPixel(i,j) == 0xFFFFC0CB){
                        array_brennen_X.add(Math.round(i/proportion));
                        array_brennen_Y.add(Math.round(j/proportion));
                    }

                }
            }
        }
        if(array_druck_X != null){
            createJsonFile(array_druck_X,array_druck_Y,"src/main/res",fileName+"_druck");
        }





    }
    public static boolean createJsonFile(ArrayList array_x,ArrayList array_y, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;
        String jsonString;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

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
            file.createNewFile();

            // 格式化json字符串
            jsonString = SaveJson.formatJson(array_x, array_y);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

    public static String formatJson(ArrayList array_x,ArrayList array_y) {
        String result  ="[";
        for(Object tmp:array_x){
            result += tmp+",";
        }
        for(int i= 0;i<array_y.size();i++){
            if(i == (array_y.size()-1)){
                result += array_y.get(i)+"]";
            }
            else {
                result += array_y.get(i)+",";
            }
        }
        return result;
    }

}
