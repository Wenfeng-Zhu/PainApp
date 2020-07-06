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


//Drawing class to integrate functions in the drawing process

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

    private int Mode = 1;
    public static final int PEN = 1;
    public static final int ERASER = 2;
    private static final float TOUCH_TOLERANCE = 4;

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    private float proportion = Container.proportion;

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    private float zoom = 1;

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


        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint.setColor(paintColor);

        mEraserPaint = new Paint((Paint.DITHER_FLAG));
        mEraserPaint.setAlpha(0);
        //mEraserPaint.setColor(Color.BLACK);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setDither(true);
        mEraserPaint.setStyle(Paint.Style.STROKE);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeWidth(30);

        hBitmapPaint = new Paint(Paint.DITHER_FLAG);
        hBitmapPaint.setColor(Color.BLACK);
        cBitmapPaint = new Paint(Paint.DITHER_FLAG);
        cBitmapPaint.setColor(Container.typeColor);

        mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setAlpha(0xFF);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
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

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        this.Mode = mode;
    }


    public Map<String, Integer> getMap() {
        return map;
    }

    private int colorNum = 0;

    protected void setMap(Map<String, Integer> map) {
        this.map = map;
        for (String key : map.keySet()) {
            colorNum++;
            PathCollection.put(key, new ArrayList<Path>());
            tmap.put(map.get(key), key);
        }
        PathCollection.put("eraser", new ArrayList<Path>());

    }

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
                cBitmap.setPixel(x_array_import[i], 1169 - y_array_import[i], Container.typeColor);
            }
        }

    }

    private static Bitmap zoom(Bitmap bitmap, float proportion) {
        Matrix matrix = new Matrix();
        matrix.postScale(proportion, proportion);
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
        //invalidate();
    }

    public void pressed(boolean pressed) {
        this.pressed = pressed;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(zoom(hBitmap, proportion), 0, 0, hBitmapPaint);

        clear(mCanvas);

        if (Container.ifImport) {
            mCanvas.drawBitmap(zoom(cBitmap, proportion), 0, 0, cBitmapPaint);
            //canvas.drawBitmap(mBitmap,0,0,mBitmapPaint);
            invalidate();
        }


        mPaint.setColor(paintColor);


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
            if (pressed) {
                for (Path path : pathList.keySet()) {
                    if (pathList.get(path).equals(tmap.get(paintColor))) {
                        mCanvas.drawPath(path, mPaint);
                    } else if (pathList.get(path).equals("eraser")) {
                        mCanvas.drawPath(path, mEraserPaint);
                    }
                }
                pressed = false;
            }


        }
        canvas.drawBitmap(zoom(mBitmap, zoom), 0, 0, mBitmapPaint);

        switch (Mode) {
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

        int num = PathCollection.size();
        for (Map.Entry<String, ArrayList<Path>> entry : PathCollection.entrySet()) {
            if (entry.getValue().isEmpty()) {
                num--;
            }
        }

        Bitmap saveBitmap = Bitmap.createBitmap(Math.round(827 * proportion), Math.round(1169 * proportion), Bitmap.Config.ARGB_8888);
        if (num == 0) {
            return false;
        } else {

            for (String string : PathCollection.keySet()) {
                Canvas saveCanvas = new Canvas(saveBitmap);
                if (PathCollection.get(string).isEmpty()) {

                } else {
                    Bitmap emptyBitmap = Bitmap.createBitmap(saveBitmap.getWidth(), saveBitmap.getHeight(), Bitmap.Config.ARGB_8888);

                    if (string.equals("eraser")) {
                        if (num == 1) {
                            if (Container.ifImport) {
//                                saveBitmap = mergeBitmap(saveBitmap,zoom(cBitmap,proportion),mPaint);
                                saveCanvas.drawBitmap(zoom(cBitmap, proportion), 0, 0, mPaint);
                                for (Path path : pathList.keySet()) {
                                    if (pathList.get(path).equals(mEraserPaint.getColor())) {
                                        saveCanvas.drawPath(path, mEraserPaint);
                                    }
                                }
                                if (emptyBitmap.sameAs(saveBitmap)) {
                                    return false;
                                } else {
                                    sbMap.put(Container.typeName, saveBitmap.copy(Bitmap.Config.ARGB_8888, true));
                                    clear(saveCanvas);
                                }

                            }

                        }
                    } else {
                        mPaint.setColor(map.get(string));
                        if (string.equals(Container.typeName)) {
                            saveCanvas.drawBitmap(zoom(cBitmap, proportion), 0, 0, mPaint);
                            //saveBitmap = mergeBitmap(saveBitmap, cBitmap, mPaint).copy(Bitmap.Config.ARGB_8888, true);
                        }
                        for (Path path : pathList.keySet()) {

                            if (pathList.get(path).equals(map.get(string))) {
                                saveCanvas.drawPath(path, mPaint);
                            } else if (pathList.get(path).equals(mEraserPaint.getColor())) {
                                saveCanvas.drawPath(path, mEraserPaint);
                            }
                        }

                        if (emptyBitmap.sameAs(saveBitmap)) {
                            return false;
                        } else {
                            sbMap.put(string, saveBitmap.copy(Bitmap.Config.ARGB_8888, true));
                            clear(saveCanvas);
                        }


                    }


                }

            }


            return true;
        }
    }

    public static Bitmap mergeBitmap(Bitmap background, Bitmap foreground, Paint paint) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap newMap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newMap);
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(zoom(foreground, Container.proportion), 0, 0, paint);
        return newMap;
    }

    void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        switch (Mode) {
            case PEN:
                mCanvas.drawPath(mPath, mPaint);
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                break;
        }
    }

    void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            switch (Mode) {
                case PEN:
                    mCanvas.drawPath(mPath, mPaint);
                    break;
                case ERASER:
                    mCanvas.drawPath(mPath, mEraserPaint);
                    break;
            }
            invalidate();
        }

    }

    void touch_up() {
        mPath.lineTo(mX, mY);

        switch (Mode) {
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
        if (!Container.ifDisplay) {

            float x = (int) Math.ceil(event.getX());
            float y = (int) Math.ceil(event.getY());

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);

                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();

                    break;
            }

            return Container.move_action;
        } else {
            return false;
        }


    }


}