package com.why.flappy_bird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * User: ysy
 * Date: 2015/8/31
 */
public class GameFlabbyBird extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;

    /**
     * 与SurfaceHolder绑定的Canvas
     */
    private Canvas mCanvas;
    /**
     * 用于绘制的线程
     */
    private Thread t;
    /**
     * 线程的控制开关
     */
    private boolean isRunning;

    public GameFlabbyBird(Context context) {
        this(context, null);
    }

    public GameFlabbyBird(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);

        setZOrderOnTop(true); // 设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //TODO
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 通知关闭线程
        isRunning = false;
    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        draw();
        long end = System.currentTimeMillis();

        try {
            if (end - start < 50) {
                Thread.sleep(50 - (end - start));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void draw() {

        try {
            // 获得canvas
            mCanvas = mHolder.lockCanvas();
            if(mCanvas!=null){
                //draw something...
            }
        }catch (Exception e){

        }finally {
            if(mCanvas!=null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

}
