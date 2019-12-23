package com.example.xoprojectlesson;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class XOCustomVIew extends View {

    private Paint gridPaint = new Paint();
    int width;
    int height;
    int alternativeWidth;
    int alternativeHeight;
    float tolzina = 5;
    float xRatio = 1 / 3f;
    private Path mPath = new Path();
    private Paint mPaint = new Paint();



    public XOCustomVIew(Context context) {
        super(context);
    }

    public XOCustomVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public XOCustomVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XOCustomVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initPaint() {
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        drawGrid(canvas);
        drawHorizontalLine(canvas);
        drawVerticalLine(canvas);
        drawX(canvas);
        canvas.drawPath(mPath, mPaint);


    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float eventX = event.getX();
//        float eventY = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPath.moveTo(eventX, eventY);
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(eventX, eventY);
//                invalidate();
//                return true;
//
//            default:
//                return super.onTouchEvent(event);
//
//        }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(eventX, eventY);
                invalidate();

                default:
                    return super.onTouchEvent(event);
        }


    }

    public void drawVerticalLine(Canvas canvas) {
        canvas.drawLine(getWidth() * xRatio, 0f, getWidth() * xRatio, getHeight(), gridPaint);
        canvas.drawLine(getWidth() * (2 * xRatio), 0f, getWidth() * (2 * xRatio), getHeight(), gridPaint);
    }

    public void drawHorizontalLine(Canvas canvas) {
        canvas.drawLine(0f, getHeight() * xRatio, getWidth(), getHeight() * xRatio, gridPaint);
        canvas.drawLine(0f, getHeight() * (2 * xRatio), getWidth(), getHeight() * (2 * xRatio), gridPaint);
    }

    public void drawX(Canvas canvas) {
//        PointF pointF = new PointF(2, 2);
        RectF rectF = new RectF(2, 2, 2, 2);
        canvas.drawOval(rectF, gridPaint);
    }







    public void drawGrid(Canvas canvas) {
        for (int i = 0; i < 2; i++) {
            float left = alternativeWidth * (i + 1);

            float right = left + tolzina;
            float top = 0;
            float bottom = height;

            canvas.drawRect(left, top, right, bottom, gridPaint);


            float left2 = 0;
            float right2 = width;
            float top2 = alternativeHeight * (i + 1);
            float bottom2 = top2 * tolzina;

            canvas.drawRect(left2, top2, right2, bottom2, gridPaint);
        }
    }
}
