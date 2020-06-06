package com.example.painapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DrawView extends View {
    private float mX, mY;

    public Map<String, Bitmap> getSbMap() {
        return sbMap;
    }

    public void setSbMap(Map<String, Bitmap> sbMap) {
        this.sbMap = sbMap;
    }

    private Map<String, Bitmap> sbMap;

    private Integer[] x_array;
    private Integer[] y_array;
    private Integer[] x_array_import;
    private Integer[] y_array_import;
    StringBuilder strb = new StringBuilder();
    StringBuilder strb_import = new StringBuilder();

//    private Bitmap saveBitmap;
//    private Canvas saveCanvas;

    private Bitmap mBitmap;
    private Bitmap hBitmap;
    private Bitmap cBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint hBitmapPaint;
    private Paint cBitmapPaint;
    private Paint mEraserPaint;
    Context context;
    private Paint mPaint;
    private int paintColor = Color.BLACK;

    private boolean Undo;

    private int Mod = 1;
    public static final int PEN = 1;
    public static final int ERASER = 2;
    private static final float TOUCH_TOLERANCE = 4;

    private float proportion = Container.proportion;

    private ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();

    private Map<String, ArrayList<Path>> PathCollection = new HashMap<String, ArrayList<Path>>();

    private LinkedHashMap<Path, Integer> pathList = new LinkedHashMap<Path, Integer>();

    private Map<Path, Integer> pathToColor = new HashMap<Path, Integer>();

    private Map<Integer, String> tmap = new HashMap<Integer, String>();

    private Map<String, Integer> map = new HashMap<String, Integer>();


    public DrawView(Context context) {
        super(context);
        this.context = context;

        sbMap = new HashMap<String, Bitmap>();

        this.importBackGround();
        Undo = false;


        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint.setColor(paintColor);

        mEraserPaint = new Paint((Paint.DITHER_FLAG));
        mEraserPaint.setAlpha(0);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeWidth(20);

        hBitmapPaint = new Paint(Paint.DITHER_FLAG);
        hBitmapPaint.setColor(Color.BLACK);
        cBitmapPaint = new Paint(Paint.DITHER_FLAG);
        cBitmapPaint.setColor(Container.color);

        mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        this.importImage();
    }

    public Paint getmEraserPaint() {
        return mEraserPaint;
    }

    public void setmEraserPaint(Paint mEraserPaint) {
        this.mEraserPaint = mEraserPaint;
    }

    public Map<String, ArrayList<Path>> getPathCollection() {
        return PathCollection;
    }

    public void setPathCollection(Map<String, ArrayList<Path>> pathCollection) {
        PathCollection = pathCollection;
    }

    public Map<Integer, String> getTmap() {
        return tmap;
    }

    public void setTmap(Map<Integer, String> tmap) {
        this.tmap = tmap;
    }

    public LinkedHashMap<Path, Integer> getPathList() {
        return pathList;
    }

    public void setPathList(LinkedHashMap<Path, Integer> pathList) {
        this.pathList = pathList;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    public int getMod() {
        return Mod;
    }

    public void setMod(int mod) {
        this.Mod = mod;
    }


    public Map<String, Integer> getMap() {
        return map;
    }

    protected void setMap(Map<String, Integer> map) {
        this.map = map;
        for (String key : map.keySet()) {
            PathCollection.put(key, new ArrayList<Path>());
            tmap.put(map.get(key), key);
        }
        PathCollection.put("eraser", new ArrayList<Path>());

    }

//    public boolean isUndo() {
//        return Undo;
//    }
//
//    public void setUndo(boolean undo) {
//        Undo = undo;
//    }

    protected void importBackGround() {
        try {
            InputStream is = getResources().getAssets().open("original.json");
            //InputStream is = new FileInputStream("/data/user/0/com.example.painapp/files/patient001_druck.json");
            //InputStream is = new FileInputStream(this.context.getFilesDir().getAbsolutePath()+"/patient001_druck.json");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            //isr = null;
            String str = "";
            while ((str = br.readLine()) != null) {
                strb.append(str);
            }
            //br = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(strb.toString());
            int num_background = jsonArray.length() / 2;
            x_array = new Integer[num_background];
            y_array = new Integer[num_background];
            for (int i = 0; i < num_background; i++) {
                x_array[i] = jsonArray.getInt(i);
                y_array[i] = jsonArray.getInt(num_background + i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hBitmap = Bitmap.createBitmap(827, 1169, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < x_array.length; i++) {
            hBitmap.setPixel((x_array[i]), (1169 - y_array[i]), Color.BLACK);
        }
    }

    protected void importImage() {

        if (Container.ifImport) {
            try {
                InputStream is_import = new FileInputStream(Container.filePath);
                InputStreamReader isr_import = new InputStreamReader(is_import, "UTF-8");
                BufferedReader br_import = new BufferedReader(isr_import);
                String str_import = "";
                while ((str_import = br_import.readLine()) != null) {
                    strb_import.append(str_import);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(strb_import.toString());
                int num_image = jsonArray.length() / 2;
                x_array_import = new Integer[num_image];
                y_array_import = new Integer[num_image];
                for (int i = 0; i < num_image; i++) {
                    x_array_import[i] = jsonArray.getInt(i);
                    y_array_import[i] = jsonArray.getInt(num_image + i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cBitmap = Bitmap.createBitmap(Math.round(827 * proportion), Math.round(1169 * proportion), Bitmap.Config.ARGB_8888);

            for (int i = 0; i < x_array_import.length; i++) {
                //System.out.println("颜色赋值测试————————————" + Constant.color);
                cBitmap.setPixel(x_array_import[i], 1169 - y_array_import[i], Container.color);
            }
        }

    }

    private static Bitmap zoom(Bitmap bitmap, float proportion) {
        Matrix matrix = new Matrix();
        matrix.postScale(proportion, proportion); //长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Math.round(827 * proportion), Math.round(1169 * proportion));
    }

    private boolean pressed = false;

    public void clear(Canvas canvas) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        invalidate();
    }

    public void pressed(boolean pressed) {
        this.pressed = pressed;
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(zoom(hBitmap, proportion), 0, 0, hBitmapPaint);






        if (Container.ifImport) {
            canvas.drawBitmap(zoom(cBitmap, proportion), 0, 0, cBitmapPaint);
            //canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
            invalidate();
        }


        mPaint.setColor(paintColor);


        clear(mCanvas);

        if (!pathList.isEmpty()) {
            for (Map.Entry<Path, Integer> entry : pathList.entrySet()) {
                if (entry.getValue() == mEraserPaint.getColor()) {
                    mCanvas.drawPath(entry.getKey(), mEraserPaint);
                } else {
                    mPaint.setColor(entry.getValue());
                    mCanvas.drawPath(entry.getKey(), mPaint);
                    mPaint.setColor(paintColor);
                }

            }
            //mCanvas.drawPath(mPath,mPaint);
            if (pressed) {
                for (Path path : PathCollection.get(tmap.get(paintColor))) {
                    mCanvas.drawPath(path, mPaint);
                }
                for (Path path : PathCollection.get("eraser")) {
                    mCanvas.drawPath(path, mEraserPaint);
                }
            }


        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        switch (Mod) {
            case PEN:
                canvas.drawPath(mPath, mPaint);
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                break;
        }

    }


    protected boolean saveScreen() {

        //Paint paint = new Paint((Paint.DITHER_FLAG));
        int num = 0;
        for (Map.Entry<String, ArrayList<Path>> entry : PathCollection.entrySet()) {
            if (entry.getValue().isEmpty()) {
                num++;
            }
        }

        Bitmap saveBitmap = Bitmap.createBitmap(Math.round(827 * proportion), Math.round(1169 * proportion), Bitmap.Config.ARGB_8888);
        if (PathCollection.size() == num) {
            return false;
        } else {
//            for (Canvas canvas : canvasBitmapMap.keySet()) {

                for (String string : PathCollection.keySet()) {
                    Canvas saveCanvas  = new Canvas(saveBitmap);
                    if (PathCollection.get(string).isEmpty()) {

                    } else {
                        if (string.equals("eraser")) {

                        } else {
                            mPaint.setColor(map.get(string));
                            for (Path path : PathCollection.get(string)) {
                                Path path1 = new Path();
                                path1.set(path);
                                saveCanvas.drawPath(path1, mPaint);
                                path1.reset();
                            }
                            for (Path path : PathCollection.get("eraser")) {
                                Path path1 = new Path();
                                path1.set(path);
                                saveCanvas.drawPath(path1, mEraserPaint);
                                path1.reset();
                            }
                            System.out.println("疼痛类型打印————————————"+ Container.fileName);
                            if (string.equals(Container.fileName)) {
                                System.out.println("啊圣诞节复活节快速导航范德萨积分");
                                Bitmap bitmap = mergeBitmap(saveBitmap, cBitmap,mPaint).copy(Bitmap.Config.ARGB_8888,true);
                                sbMap.put(string, bitmap);
                            } else {
                                sbMap.put(string, saveBitmap.copy(Bitmap.Config.ARGB_8888, true));
                                clear(saveCanvas);
                            }

                        }

                    }

                }


//            }


            return true;
        }
    }

    public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground,Paint paint) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap newMap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newMap);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(zoom(foreground, Container.proportion),0,0,paint);
        return newMap;
    }


    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        switch (Mod) {
            case PEN:
                mCanvas.drawPath(mPath, mPaint);
                Path path = new Path();
                path.set(mPath);
                PathCollection.get(tmap.get(mPaint.getColor())).add(path);
                pathList.put(path, mPaint.getColor());
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                Path path_1 = new Path();
                path_1.set(mPath);
                PathCollection.get("eraser").add(path_1);
                pathList.put(path_1, mEraserPaint.getColor());
                break;
        }
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 2) {
                getParent().requestDisallowInterceptTouchEvent(false);
                //getParent().getParent().requestDisallowInterceptTouchEvent(false);
            } else if (event.getPointerCount() == 1) {
                getParent().requestDisallowInterceptTouchEvent(true);
                //getParent().getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        float x = (int) Math.ceil(event.getX());
        float y = (int) Math.ceil(event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


}
