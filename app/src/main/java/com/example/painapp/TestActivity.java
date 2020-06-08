package com.example.painapp;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private ScaleGestureDetector mScaleGestureDetector = null;
    private Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mScaleGestureDetector = new ScaleGestureDetector(this,
                new ScaleGestureListener());

        mSurfaceView.post(new Runnable() {

            @Override
            public void run() {

                mBitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
                // 锁定整个SurfaceView
                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                // 画图
                Paint hBitmapPaint = new Paint(Paint.DITHER_FLAG);
                hBitmapPaint.setColor(Color.BLACK);
                mCanvas.drawBitmap(mBitmap, 0f, 0f, null);
                mCanvas.drawCircle(100,100,100,hBitmapPaint);
                // 绘制完成，提交修改
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                // 重新锁一次
                mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 返回给ScaleGestureDetector来处理
        return mScaleGestureDetector.onTouchEvent(event);
    }

    public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        private float scale;
        private float preScale = 1;// 默认前一次缩放比例为1

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub

            Matrix mMatrix = new Matrix();
            float previousSpan = detector.getPreviousSpan();
            float currentSpan = detector.getCurrentSpan();
            if (currentSpan < previousSpan) {
                // 缩小
                System.out.println("缩小————————————————————");
                // scale = preScale-detector.getScaleFactor()/3;
                scale = preScale - (previousSpan - currentSpan) / 1000;
            } else {
                // 放大
                System.out.println("放大————————————————————");
                // scale = preScale+detector.getScaleFactor()/3;
                scale = preScale + (currentSpan - previousSpan) / 1000;
            }
            mMatrix.setScale(scale, scale);

            // 锁定整个SurfaceView
            Canvas mCanvas = mSurfaceHolder.lockCanvas();
            // 清屏
            mCanvas.drawColor(Color.WHITE);
            // 画缩放后的图
            mCanvas.drawBitmap(mBitmap, mMatrix, null);
            // 绘制完成，提交修改
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            // 重新锁一次
            mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;//记住本次的缩放后的图片比例
        }
    }
}
