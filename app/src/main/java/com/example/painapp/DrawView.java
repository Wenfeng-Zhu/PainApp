package com.example.painapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
    public Integer[] x_array;
    public Integer[] y_array;
    private int num;
    StringBuilder strb = new StringBuilder();
    ArrayList arrayX = new ArrayList();
    ArrayList arrayY = new ArrayList();


    private Bitmap mBitmap;
    private Bitmap hBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint hBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    private Paint mPaint;
    private int paintColor = Color.BLACK;

    private float proportion = (float) 1.2;

    public DrawView(Context context) {
        super(context);
        this.context = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint.setColor(Color.BLACK);
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

        Bitmap b = Bitmap.createBitmap( Math.round(827*proportion),Math.round(1169*proportion),Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(b);


        //Log.d("DrawingView", "DrawingView=========================");
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Math.round(827*proportion),Math.round(1169*proportion));
    }

    protected Bitmap getmBitmap(){
        return this.mBitmap;
    }

    protected void setColor(int color){
        paintColor = color;
    }

    protected void setProportion(float proportion){
        this.proportion = proportion;
    }





    protected void onDraw(Canvas canvas) {
        //Bitmap bitmap = Bitmap.createBitmap(827, 1169, Bitmap.Config.ARGB_8888);



        try {



            InputStream is = getResources().getAssets().open("original.json");
            //System.out.println(context.getFilesDir().getAbsoluteFile()+"patient_druck.json");
            //InputStream is = new FileInputStream("/data/user/0/com.example.painapp/files/patient001_druck.json");
            //InputStream is = new FileInputStream(this.context.getFilesDir().getAbsolutePath()+"/patient001_druck.json");
            //System.out.println("是法国进口水果还是"+getResources().getAssets());

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
        hBitmap = Bitmap.createBitmap(827,1169,Bitmap.Config.ARGB_8888);
        for (int i = 0; i < num; i++) {
            hBitmap.setPixel((x_array[i]),(1169 - y_array[i]),Color.BLACK);
            //canvas.drawPoint((x_array[i]), (1169 - y_array[i]),  mPaint);
        }
        canvas.drawBitmap(zoom(hBitmap,proportion),0,0,hBitmapPaint);
        //canvas.save();
        //canvas.scale(2,2);

        super.onDraw(canvas);

        mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(10);



        //for (int i = 0; i < num; i++) {
        //    mBitmap.setPixel((x_array[i]),(1169 - y_array[i]),Color.BLACK);
        //    //canvas.drawPoint((x_array[i]), (1169 - y_array[i]),  mPaint);
        //}

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);


        canvas.drawPath(mPath, mPaint);


        canvas.drawPath(circlePath, circlePaint);



        //Log.d("onDraw", "onDraw================================");
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //Log.d("OnSizeChanged", "OnSizeChanged=========================");

    }
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

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
            //mPath.quadTo(mX, mY, x, y);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }


    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = (int)Math.ceil(event.getX());
        float y = (int)Math.ceil(event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                //System.out.println("start point is: x = "+x+"y = "+y);;
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                //System.out.println("中间坐标"+x+"y="+y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                //System.out.println("end point is: x = 是"+x+"y = "+ y);


                break;
        }

        return true;
    }

    private static Bitmap zoom(Bitmap bitmap,float proportion) {
        Matrix matrix = new Matrix();
        matrix.postScale(proportion,proportion); //长和宽放大缩小的比例
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBitmap;
    }


}
