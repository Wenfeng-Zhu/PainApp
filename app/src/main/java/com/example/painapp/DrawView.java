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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DrawView extends View {
    private float mX, mY;

    public Integer[] x_array;
    public Integer[] y_array;
    public Integer[] x_array_import;
    public Integer[] y_array_import;
    private int num;
    StringBuilder strb = new StringBuilder();
    StringBuilder strb_import = new StringBuilder();


    private Bitmap mBitmap;
    private Bitmap hBitmap;
    private Bitmap cBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint hBitmapPaint;
    private Paint mEraserPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private Paint mPaint;
    private int paintColor = Color.BLACK;
    private int mMode = 1;
    public static final int PEN = 1;
    public static final int ERASER = 2;
    private static final float TOUCH_TOLERANCE = 4;

    private float proportion = Constant.proportion;

    public DrawView(Context context) {
        super(context);
        this.context = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint.setColor(Color.BLACK);

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
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(1);

        Bitmap b = Bitmap.createBitmap(827, 1169, Bitmap.Config.ARGB_8888);
        Bitmap b1 = zoom(b,proportion);
        //mBitmap = b1;

        mCanvas = new Canvas(b);


        //Log.d("DrawingView", "DrawingView=========================");
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Math.round(827 * proportion), Math.round(1169 * proportion));
    }

    protected Bitmap getmBitmap() {
        return this.mBitmap;
    }

    protected void setColor(int color) {
        paintColor = color;
    }

    protected void setMode(int mode) {
        this.mMode = mode;
    }


    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(827, 1169, Bitmap.Config.ARGB_8888);
        //mBitmap = zoom(bitmap,proportion);

        try {
            InputStream is = getResources().getAssets().open("original.json");
            //InputStream is = new FileInputStream("/data/user/0/com.example.painapp/files/patient001_druck.json");
            //InputStream is = new FileInputStream(this.context.getFilesDir().getAbsolutePath()+"/patient001_druck.json");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String str = "";
            while ((str = br.readLine()) != null) {
                strb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(strb.toString());
            num = jsonArray.length() / 2;
            x_array = new Integer[num];
            y_array = new Integer[num];
            for (int i = 0; i < num; i++) {
                x_array[i] = jsonArray.getInt(i);
                y_array[i] = jsonArray.getInt(num + i);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        canvas.drawColor(Color.parseColor("#00FFFFFF"));

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        hBitmap = Bitmap.createBitmap(827, 1169, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < num; i++) {
            hBitmap.setPixel((x_array[i]), (1169 - y_array[i]), Color.BLACK);
        }
        canvas.drawBitmap(zoom(hBitmap, proportion), 0, 0, hBitmapPaint);

        if (Constant.ifImport) {
            try {
                InputStream is_import = new FileInputStream(Constant.filePath);
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
                num = jsonArray.length() / 2;
                x_array_import = new Integer[num];
                y_array_import = new Integer[num];
                for (int i = 0; i < num; i++) {
                    x_array_import[i] = jsonArray.getInt(i);
                    y_array_import[i] = jsonArray.getInt(num + i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cBitmap = Bitmap.createBitmap(Math.round(827 * proportion), Math.round(1169 * proportion), Bitmap.Config.ARGB_8888);

            for (int i = 0; i < num; i++) {
                cBitmap.setPixel(x_array_import[i], 1169 - y_array_import[i], Constant.color);
            }
            mCanvas.drawBitmap(zoom(cBitmap, proportion), 0, 0, mBitmapPaint);

        }

        super.onDraw(canvas);

        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(10);
        //onSizeChanged(Math.round(827*proportion),Math.round(1169*proportion),827,1169);


        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        switch (mMode) {
            case PEN:
                canvas.drawPath(mPath, mPaint);
                canvas.drawPath(circlePath, circlePaint);
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                canvas.drawPath(circlePath, circlePaint);
                break;
        }

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }



    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        switch (mMode) {
            case PEN:
                mCanvas.drawPath(mPath, mPaint);
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                break;
        }
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            switch (mMode) {
                case PEN:
                    mCanvas.drawPath(mPath, mPaint);
                    break;
                case ERASER:
                    mCanvas.drawPath(mPath, mEraserPaint);
                    break;
            }
            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        switch (mMode) {
            case PEN:
                mCanvas.drawPath(mPath, mPaint);
                break;
            case ERASER:
                mCanvas.drawPath(mPath, mEraserPaint);
                break;
        }
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    private static Bitmap zoom(Bitmap bitmap, float proportion) {
        Matrix matrix = new Matrix();
        matrix.postScale(proportion, proportion); //长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
