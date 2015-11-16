package com.yesy.drccurve;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ysy.blogdemo.R;

/**
 * User: ysy
 * Date: 2015/11/4
 * Time: 15:24
 */
public class DrcCurve extends View {
    private Paint mPaint;

    private int threshold_count;

    /**
     * 背景颜色
     */
    private int background;

    /**
     * 坐标轴标签颜色
     */
    private int textColor;

    /**
     * 网格颜色
     */
    private int gridLineColor;
    /**
     * 原始曲线颜色
     */
    private int originCurveColor;

    /**
     * 调整曲线颜色
     */
    private int realCurveColor;

    /**
     * 标签字体大小
     */
    private int indexTextSize;

    /**
     * 坐标轴标签数组
     */
    private final float[] xIndex = new float[]
            {-90f, -80f, -70f, -60f, -50f, -40f, -30f, -20f, -10f, 0f};
    private final float[] yIndex = new float[]
            {-90f, -80f, -70f, -60f, -50f, -40f, -30f, -20f, -10f, 0f};

    /**
     * 横纵坐标网格数
     */
    private int xLine_count = 10;
    private int yLine_count = 10;

    /**
     * 网格线之间的距离
     */
    private float x_interval;
    private float y_interval;

    /**
     * 噪声门
     */
    private MyPoint NT;

    /**
     * 扩音门
     */
    private MyPoint ET;

    /**
     * 压缩门
     */
    private MyPoint CT;

    /**
     * 限制门
     */
    private MyPoint LT;

    /**
     * 移动状态
     */
    private int MoveState = 0;
    private static final int MOVE_IDLE = 0;
    private static final int MOVE_NT = 1;
    private static final int MOVE_ET = 2;
    private static final int MOVE_CT = 3;
    private static final int MOVE_LT = 4;

    /*
    * 监听数据改变
    * */
    public interface OnDataChangeListener {
        void dataChange();
    }

    private OnDataChangeListener listener;

    public void setDataChangeListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    public DrcCurve(Context context) {
        this(context, null);
    }

    public DrcCurve(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrcCurve);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DrcCurve_threshold_count:
                    threshold_count = a.getInt(attr, 3);
                    break;
                case R.styleable.DrcCurve_gridlinecolor:
                    gridLineColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.DrcCurve_textcolor:
                    textColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DrcCurve_indexTextSize:
                    indexTextSize = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DrcCurve_originCurveColor:
                    originCurveColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.DrcCurve_realCurveColor:
                    realCurveColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.DrcCurve_backgroundColor:
                    background = a.getColor(attr, Color.WHITE);
                    break;
            }
        }


        NT = new MyPoint(-70, -70);
        ET = new MyPoint(-50, -50);
        CT = new MyPoint(-25, -25);
        LT = new MyPoint(-10, -10);

        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(getResources().getColor(R.color.blue));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        x_interval = width / (yLine_count + 1);
        y_interval = width / (xLine_count + 1);

        setMeasuredDimension(width, width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(background);
        drawGridLine(canvas);
        drawText(canvas);
        drawCurve(canvas);

        listener.dataChange();
    }

    /*
    * 特性曲线
    * */
    private void drawCurve(Canvas canvas) {

        mPaint.setPathEffect(null);
        mPaint.setColor(originCurveColor);
        canvas.drawLine(getWidth() / (yLine_count + 1), (getHeight() / (xLine_count + 1)) * xLine_count
                , (getWidth() / (yLine_count + 1)) * yLine_count, getHeight() / (xLine_count + 1), mPaint);

        float ntX = (float) (x_interval + ((90.0 + NT.x) / 10.0) * x_interval);
        float ntY = (float) (y_interval + (-(NT.y) / 10.0) * y_interval);
        float etX = (float) (x_interval + ((90.0 + ET.x) / 10.0) * x_interval);
        float etY = (float) (y_interval + (-(ET.y) / 10.0) * y_interval);
        float ctX = (float) (x_interval + ((90.0 + CT.x) / 10.0) * x_interval);
        float ctY = (float) (y_interval + (-(CT.y) / 10.0) * y_interval);
        float ltX = (float) (x_interval + ((90.0 + LT.x) / 10.0) * x_interval);
        float ltY = (float) (y_interval + (-(LT.y) / 10.0) * y_interval);

      /*  Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);
        mPaint.setXfermode(xfermode);*/
        mPaint.setColor(realCurveColor);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(ntX, (float) (y_interval * 10.0));
        path.lineTo(ntX, ntY);
        path.lineTo(etX, etY);
        path.lineTo(ctX, ctY);
        path.lineTo(ltX, ltY);
        path.lineTo((float) (x_interval * 10.0), ltY);
        canvas.drawPath(path, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(ntX, ntY, 10, mPaint);
        canvas.drawCircle(etX, etY, 10, mPaint);
        canvas.drawCircle(ctX, ctY, 10, mPaint);
        canvas.drawCircle(ltX, ltY, 10, mPaint);

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(20);
        textPaint.setColor(textColor);
        StaticLayout layout = new StaticLayout("NT:\r\n" + "x:" + NT.x + "\r\n" + "y:"
                + NT.y, textPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

        canvas.save();
        canvas.translate(ntX - x_interval, ntY - y_interval);
        layout.draw(canvas);
        canvas.restore();

        layout = new StaticLayout("ET:\r\n" + "x:" + ET.x + "\r\n" + "y:"
                + ET.y, textPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate(etX - x_interval, etY - y_interval);
        layout.draw(canvas);
        canvas.restore();

        layout = new StaticLayout("CT:\r\n" + "x:" + CT.x + "\r\n" + "y:"
                + CT.y, textPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate(ctX - x_interval, ctY - y_interval);
        layout.draw(canvas);
        canvas.restore();

        layout = new StaticLayout("LT:\r\n" + "x:" + LT.x + "\r\n" + "y:"
                + LT.y, textPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        canvas.save();
        canvas.translate(ltX - x_interval, ltY - y_interval);
        layout.draw(canvas);
        canvas.restore();
        //canvas.drawText("x:" + NT.x + "dB\r\n" + "y:" + NT.y + "dB", ntX, ntY, mPaint);
    }

    /*
    * 画坐标标签
    * */
    private void drawText(Canvas canvas) {
        mPaint.setColor(textColor);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(indexTextSize);
        canvas.drawText("X/dB", getWidth() / 2,
                getHeight() - (getHeight() / ((xLine_count + 1) * 2)), mPaint);
        Path path = new Path();
        path.moveTo((getWidth() / ((yLine_count + 1) * 2)), 0);
        path.lineTo((getWidth() / ((yLine_count + 1) * 2)), getHeight());
        canvas.drawTextOnPath("Y/dB", path, getHeight() / 2, 0, mPaint);
        mPaint.setTextSize(indexTextSize);
        canvas.save();
        canvas.translate(0, (getHeight() / (xLine_count + 1)));
        for (int i = 0; i < yLine_count; i++) {
            canvas.translate((getWidth() / (yLine_count + 1)), 0);
            canvas.drawText("" + xIndex[i], 0, -20, mPaint);
        }

        for (int i = 0; i < (xLine_count - 1); i++) {
            canvas.translate(0, (getWidth() / (yLine_count + 1)));
            canvas.drawText("" + yIndex[yIndex.length - (i + 2)], 20, 0, mPaint);
        }
        canvas.restore();
    }

    /*
    * 画网格线
    * */
    private void drawGridLine(Canvas canvas) {
        mPaint.setXfermode(null);
        PathEffect effect = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
        mPaint.setPathEffect(effect);
        float startX = 0;
        float startY = 0;
        float stopX = 0;
        float stopY = 0;
        mPaint.setColor(gridLineColor);

        for (int i = 0; i < yLine_count; i++) {
            startX = (getWidth() / (yLine_count + 1)) * (i + 1);
            startY = (getHeight() / (xLine_count + 1));
            stopX = (getWidth() / (yLine_count + 1)) * (i + 1);
            stopY = (getHeight() / (xLine_count + 1)) * (yLine_count);
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        for (int i = 0; i < xLine_count; i++) {
            startX = (getWidth() / (yLine_count + 1));
            startY = (getHeight() / (xLine_count + 1)) * (i + 1);
            stopX = (getWidth() / (yLine_count + 1)) * (xLine_count);
            stopY = (getHeight() / (xLine_count + 1)) * (i + 1);
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }

    }

    float old_x = 0.0f;
    float old_y = 0.0f;
    float dx = 0.0f;
    float dy = 0.0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);

//        float) (x_interval + ((90.0 + NT.x) / 10.0) * x_interval),
//                (float) (y_interval + (-(NT.y) / 10.0) * y_interval)(

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                old_x = event.getX();
                old_y = event.getY();
                dx = 0.0f;
                dy = 0.0f;
                /*
                * 根据触摸坐标判断改变那个门的值
                * */
                if ((Math.abs(old_x - (x_interval + ((90.0 + NT.x) / 10.0) * x_interval))
                        <= x_interval) &&
                        (Math.abs(old_y - (y_interval + (-(NT.y) / 10.0) * y_interval))
                                <= y_interval)) {
                    MoveState = MOVE_NT;
                }
                if ((Math.abs(old_x - (x_interval + ((90.0 + ET.x) / 10.0) * x_interval))
                        <= x_interval) &&
                        (Math.abs(old_y - (y_interval + (-(ET.y) / 10.0) * y_interval))
                                <= y_interval)) {
                    MoveState = MOVE_ET;
                }
                if ((Math.abs(old_x - (x_interval + ((90.0 + CT.x) / 10.0) * x_interval))
                        <= x_interval) &&
                        (Math.abs(old_y - (y_interval + (-(CT.y) / 10.0) * y_interval))
                                <= y_interval)) {
                    MoveState = MOVE_CT;
                }
                if ((Math.abs(old_x - (x_interval + ((90.0 + LT.x) / 10.0) * x_interval))
                        <= x_interval) &&
                        (Math.abs(old_y - (y_interval + (-(LT.y) / 10.0) * y_interval))
                                <= y_interval)) {
                    MoveState = MOVE_LT;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                dx = x - old_x;
                dy = y - old_y;

                old_x = x;
                old_y = y;

                break;
            case MotionEvent.ACTION_UP:
                MoveState = MOVE_IDLE;
                break;
        }

        if (MoveState == MOVE_LT) {
            moveLT();
        }
        if (MoveState == MOVE_CT) {
            moveCT();
        }
        if (MoveState == MOVE_ET) {
            moveET();
        }
        if (MoveState == MOVE_NT) {
            moveNT();
        }

        invalidate();

        //return super.onTouchEvent(event);
        return true;
    }


    private void moveLT() {
        LT.x += (dx / x_interval) * 10.0;
        if (LT.x > 0) LT.x = 0.0f;
        if (LT.x < CT.x) LT.x = CT.x;
        LT.y = LT.x;
        if (LT.y > 0) LT.y = 0.0f;
        if (LT.y < CT.y) LT.y = CT.y;
        LT.x = LT.y;
    }

    private void moveCT() {
        CT.x += (dx / x_interval) * 10.0;
        CT.y -= (dy / y_interval) * 10.0;
        if (CT.x > LT.x) CT.x = LT.x;
        if (CT.x < ET.x) CT.x = ET.x;
        if (CT.y > LT.y) CT.y = LT.y;
        if (CT.y < ET.y) CT.y = ET.y;
        if (CT.y < CT.x) CT.y = CT.x;
        ET.y = ET.x + (CT.y - CT.x);
    }

    private void moveET() {
        ET.x += (dx / x_interval) * 10.0;
        ET.y -= (dy / y_interval) * 10.0;
        if (ET.x > CT.x) ET.x = CT.x;
        if (ET.x < NT.x) ET.x = NT.x;
        if (ET.y > CT.y) ET.y = CT.y;
        if (ET.y < NT.y) ET.y = NT.y;
        if (ET.y < ET.x) ET.y = ET.x;
        CT.y = CT.x + (ET.y - ET.x);
        if (CT.y > LT.y) CT.y = LT.y;
        ET.y = ET.x + (CT.y - CT.x);
    }

    private void moveNT() {
        NT.x += (dx / x_interval) * 10.0;
        if (NT.x > ET.x) NT.x = ET.x;
        if (NT.x < -90.0f) NT.x = -90.0f;
        NT.y = NT.x;
        if (NT.y > ET.y) NT.y = ET.y;
        if (NT.y < -90.0f) NT.y = -90.0f;
        NT.x = NT.y;
    }

    public MyPoint getCT() {
        return CT;
    }

    public MyPoint getET() {
        return ET;
    }

    public MyPoint getLT() {
        return LT;
    }

    public MyPoint getNT() {
        return NT;
    }
}
