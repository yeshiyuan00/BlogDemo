package com.why.flappy_bird;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.util.DatabaseHelper;
import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: ysy
 * Date: 2015/8/31
 */
public class CopyOfGameFlabbyBird extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;

    private DatabaseHelper databaseHelper;

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

    /**
     * 当前View的尺寸
     */
    private int mWidth;
    private int mHeight;
    private RectF mGamePanelRect = new RectF();

    /**
     * 背景
     */
    private Bitmap mBg;

    /**
     * *********鸟相关**********************
     */
    private Bird mBird;
    private Bitmap mBirdBitmap;

    private Paint mPaint;
    /**
     * 地板
     */
    private Floor mFloor;
    private Bitmap mFloorBg;
    private int mSpeed;

    /**
     * *********管道相关**********************
     */
    /**
     * 管道
     */
    private Bitmap mPipeTop;
    private Bitmap mPipeBottom;
    private RectF mPipeRect;
    private int mPipeWidth;

    /**
     * 管道的宽度 60dp
     */
    private static final int PIPE_WIDTH = 60;
    private List<Pipe> mPipes = new ArrayList<Pipe>();

    /**
     * 分数
     */
    private final int[] mNums = new int[]{R.drawable.n0, R.drawable.n1,
            R.drawable.n2, R.drawable.n3, R.drawable.n4, R.drawable.n5,
            R.drawable.n6, R.drawable.n7, R.drawable.n8, R.drawable.n9};
    private Bitmap[] mNumBitmap;

    private int mGrade = 0;
    /**
     * 单个数字的高度的1/15
     */
    private static final float RADIO_SINGLE_NUM_HEIGHT = 1 / 15F;
    /**
     * 单个数字的宽度
     */
    private int mSingleGradeWidth;
    /**
     * 单个数字的高度
     */
    private int mSingleGradeHeight;
    /**
     * 单个数字的范围
     */
    private RectF mSingleNumRectF;


    /**
     * 游戏的状态
     *
     * @author ysy
     */
    private enum GameStatus {
        WAITTING, RUNNING, STOP
    }

    /**
     * 记录游戏的状态
     */
    private GameStatus mStatus = GameStatus.WAITTING;

    /**
     * 触摸上升的距离，因为是上升，所以为负值
     */
    private static final int TOUCH_UP_SIZE = -16;
    /**
     * 将上升的距离转化为px；这里多存储一个变量，变量在run中计算
     */
    private final int mBirdUpDis = Util.dp2px(getContext(), TOUCH_UP_SIZE);

    private int mTmpBirdDis;
    /**
     * 鸟自动下落的距离
     */
    private final int mAutoDownSpeed = Util.dp2px(getContext(), 2);

    /**
     * 两个管道间距离
     */
    private final int PIPE_DIS_BETWEEN_TWO = Util.dp2px(getContext(), 300);
    /**
     * 记录移动的距离，达到 PIPE_DIS_BETWEEN_TWO 则生成一个管道
     */
    private int mTmpMoveDistance;

    /**
     * 记录需要移除的管道
     */
    private List<Pipe> mNeedRemovePipe = new ArrayList<Pipe>();

    private int mRemovedPipe = 0;

    private List<Integer> myGrade = new ArrayList<Integer>();

    private GameOverListener gameOverListener;

    public interface GameOverListener {
        void gameOver();
    }

    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    //音效的音量
    int streamVolume;

    //定义SoundPool 对象
    private SoundPool soundPool;

    //定义HASH表
    private HashMap<Integer, Integer> soundPoolMap;

    private int preGrede = 0;
    private int afterGrade = 0;
    private boolean isDown = true;

    public CopyOfGameFlabbyBird(Context context) {
        this(context, null);
    }

    public CopyOfGameFlabbyBird(Context context, AttributeSet attrs) {
        super(context, attrs);

        databaseHelper = DatabaseHelper.getInstance(getContext());

        mPipeWidth = Util.dp2px(getContext(), PIPE_WIDTH);

        mHolder = getHolder();
        mHolder.addCallback(this);

        setZOrderOnTop(true); // 设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        initBitmaps();

        // 初始化速度
        mSpeed = Util.dp2px(getContext(), 4);

        initSounds(context);
    }

    public void initSounds(Context context) {
        //初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);

        //初始化HASH表
        soundPoolMap = new HashMap<Integer, Integer>();

        //获得声音设备和设备音量
        AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

        soundPoolMap.put(1, soundPool.load(context, R.raw.wing, 1));
        try {
            Thread.sleep(50);// 给予初始化音乐文件足够时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPoolMap.put(2, soundPool.load(context, R.raw.point, 1));
        try {
            Thread.sleep(50);// 给予初始化音乐文件足够时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPoolMap.put(3, soundPool.load(context, R.raw.over, 1));
        try {
            Thread.sleep(50);// 给予初始化音乐文件足够时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPoolMap.put(4, soundPool.load(context, R.raw.die, 1));

        try {
            Thread.sleep(50);// 给予初始化音乐文件足够时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPoolMap.put(5, soundPool.load(context, R.raw.died, 1));

    }

    /**
     * 初始化图片
     */
    private void initBitmaps() {

        mBg = loadImageByResId(R.drawable.bg1);
        mBirdBitmap = loadImageByResId(R.drawable.b1);
        mFloorBg = loadImageByResId(R.drawable.floor_bg2);
        mPipeTop = loadImageByResId(R.drawable.g2);
        mPipeBottom = loadImageByResId(R.drawable.g1);

        mNumBitmap = new Bitmap[mNums.length];
        for (int i = 0; i < mNumBitmap.length; i++) {
            mNumBitmap[i] = loadImageByResId(mNums[i]);
        }
    }

    /**
     * 根据resId加载图片
     *
     * @param resId
     * @return
     */
    private Bitmap loadImageByResId(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mGamePanelRect.set(0, 0, w, h);

        mSingleGradeHeight = (int) (h * RADIO_SINGLE_NUM_HEIGHT);
        mSingleGradeWidth = (int) (mSingleGradeHeight * 1.0f
                / mNumBitmap[0].getHeight() * mNumBitmap[0].getWidth());
        mSingleNumRectF = new RectF(0, 0, mSingleGradeWidth, mSingleGradeHeight);

        // 初始化mBird
        mBird = new Bird(getContext(), mWidth, mHeight, mBirdBitmap);
        // 初始化地板
        mFloor = new Floor(mWidth, mHeight, mFloorBg);

        // 初始化管道范围
        mPipeRect = new RectF(0, 0, mPipeWidth, mHeight);
        //Pipe pipe = new Pipe(getContext(), w, h, mPipeTop, mPipeBottom);
        // mPipes.add(pipe);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 通知关闭线程
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long start = System.currentTimeMillis();
            logic();
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
    }

    private void draw() {

        try {
            // 获得canvas
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //draw something...
                drawBg();
                drawBird();
                drawPipes();
                drawFloor();
                drawGrades();

                // 更新我们地板绘制的x坐标
                //mFloor.setX(mFloor.getX() - mSpeed);

            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    private void drawGrades() {
        String grade = mGrade + "";
        mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
        mCanvas.translate(mWidth / 2 - grade.length() * mSingleGradeWidth / 2,
                1f / 8 * mHeight);
        // draw single num one by one
        for (int i = 0; i < grade.length(); i++) {
            String numStr = grade.substring(i, i + 1);
            int num = Integer.valueOf(numStr);
            mCanvas.drawBitmap(mNumBitmap[num], null, mSingleNumRectF, null);
            mCanvas.translate(mSingleGradeWidth, 0);
        }
        mCanvas.restore();
    }

    private void drawPipes() {
        for (Pipe pipe : mPipes) {
            //pipe.setX(pipe.getX() - mSpeed);
            pipe.draw(mCanvas, mPipeRect);
        }
    }

    private void drawFloor() {
        mFloor.draw(mCanvas, mPaint);
    }

    private void drawBird() {

        mBird.draw(mCanvas);
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        mCanvas.drawBitmap(mBg, null, mGamePanelRect, null);
    }

    /**
     * 处理一些逻辑上的计算
     */
    private void logic() {
        switch (mStatus) {
            case RUNNING:

                mGrade = 0;
                // 更新我们地板绘制的x坐标，地板移动
                mFloor.setX(mFloor.getX() - mSpeed);

                logicPipe();

                //默认下落，点击时瞬间上升
                mTmpBirdDis += mAutoDownSpeed;
                mBird.setY(mBird.getY() + mTmpBirdDis);

                // 计算分数
                mGrade += mRemovedPipe;
                for (Pipe pipe : mPipes) {
                    if (pipe.getX() + mPipeWidth < mBird.getX()) {
                        mGrade++;
                    }
                }

                preGrede = afterGrade;
                afterGrade = mGrade;

                if (afterGrade - preGrede == 1) {
                    soundPool.play(soundPoolMap.get(2), streamVolume, streamVolume, 1, 1, 1);
                }

                checkGameOver();
                break;
            case STOP:  // 鸟落下
                // 如果鸟还在空中，先让它掉下来
                if (mBird.getY() < mFloor.getY() - mBird.getWidth()) {
                    mTmpBirdDis += mAutoDownSpeed;
                    mBird.setY(mBird.getY() + mTmpBirdDis);
                } else {
                    mStatus = GameStatus.WAITTING;
                    soundPool.play(soundPoolMap.get(5), streamVolume, streamVolume, 1, 1, 1);
                    initPos();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //gameOverListener.gameOver();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 重置鸟的位置等数据
     */
    private void initPos() {
        mPipes.clear();
        mNeedRemovePipe.clear();
        //重置鸟的位置
        mBird.setY(mHeight * 1 / 2);
        //重置下落速度
        mTmpBirdDis = 0;
        mTmpMoveDistance = 0;
        mRemovedPipe = 0;
    }

    private void logicPipe() {
        // 管道移动
        for (Pipe pipe : mPipes) {
            if (pipe.getX() < -mPipeWidth) {
                mNeedRemovePipe.add(pipe);
                mRemovedPipe++;
                continue;
            }
            pipe.setX(pipe.getX() - mSpeed);
        }
        //移除管道
        mPipes.removeAll(mNeedRemovePipe);
        Log.e("TAG", "现存管道数量：" + mPipes.size());

        // 管道
        mTmpMoveDistance += mSpeed;
        // 生成一个管道
        if (mTmpMoveDistance >= PIPE_DIS_BETWEEN_TWO) {
            Pipe pipe = new Pipe(getContext(), getWidth(), getHeight(),
                    mPipeTop, mPipeBottom);
            mPipes.add(pipe);
            mTmpMoveDistance = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            switch (mStatus) {
                case WAITTING:
                    mStatus = GameStatus.RUNNING;
                    // hidebtn();
                    break;
                case RUNNING:
                    mTmpBirdDis = mBirdUpDis;
                    soundPool.play(soundPoolMap.get(1), streamVolume, streamVolume, 1, 1, 1);
                    break;
            }
        }
        return true;
    }

    private void checkGameOver() {
        // 如果触碰地板，gg
        if (mBird.getY() > mFloor.getY() - mBird.getHeight()) {
            mStatus = GameStatus.STOP;
            insertGrade(databaseHelper.getWritableDatabase(), mGrade);
            return;
        }
        // 如果撞到管道
        for (Pipe wall : mPipes) {
            if (wall.getX() + mPipeWidth < mBird.getX()) {
                continue;
            }
            if (wall.touchBird(mBird)) {
                soundPool.play(soundPoolMap.get(3), streamVolume, streamVolume, 2, 1, 1);
                mStatus = GameStatus.STOP;
                insertGrade(databaseHelper.getWritableDatabase(), mGrade);
                break;
            }
        }
    }

    private void insertGrade(SQLiteDatabase db, int mGrade) {
        db.execSQL("insert into " + Score.TABLE_NAME + " values(null,?)",
                new String[]{String.valueOf(mGrade)});
    }


}


