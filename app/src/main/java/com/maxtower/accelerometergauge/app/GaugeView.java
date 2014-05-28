package com.maxtower.accelerometergauge.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class GaugeView extends View {

    public static final int DEFAULT_SIZE = 400;
    public static int counter = 0;

    private float mMaxGauge = 100.0f;
    private float mPosition = 0;
    private float mPositionStep = 1;
    private float mIntermediatePosition = 30;
    private float mTickStep = 12.5f;
    private Paint mTicksPaint;
    private Paint mNeedlePaint;
    private Paint mNeedleScrewPaint;

    public GaugeView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GaugeView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GaugeView(final Context context) {
        super(context);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if(widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = DEFAULT_SIZE;
        }

        if(heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            height = heightSize;
        } else {
            height = DEFAULT_SIZE;
        }

        //Set height = half width
        if(width > 0 && height > 0) {
            width = Math.min(height*2, width);
            height = width/2;
        }
        else if(width >= 0) {
            height = width/2;
        }
        else if(height >= 0) {
            width = height*2;
        }
        else {
            width = 0;
            height = 0;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Clear canvas
        canvas.drawColor(Color.TRANSPARENT);

        // Draw Metallic Arc and background
        drawBackground(canvas);

        // Draw Ticks and colored arc
        drawTickMarks(canvas);

        // Draw Needle
        drawNeedle(canvas, mIntermediatePosition);

        if(mIntermediatePosition != mPosition) {
            mIntermediatePosition = computeNewIntermediate(mPosition, mIntermediatePosition, mPositionStep);
            invalidate();
        }
    }
    public static float computeNewIntermediate(float position, float intermediatePosition, float step) {
        float newIntermediatePos = 0.0f;
        if(intermediatePosition < 0){
            return 0;
        }
        if((position - intermediatePosition) > step) {
            newIntermediatePos = intermediatePosition + step;
        }
        else if((position - intermediatePosition) < -1*step) {
            newIntermediatePos = intermediatePosition - step;
        }
        else {
            newIntermediatePos = position;
        }

        return newIntermediatePos;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
    }
    private void drawTickMarks(Canvas canvas) {

        float ticksLength = 40;
        float length = 40;
        float viewAngle = 140.0f;
        float step = mTickStep/ mMaxGauge *viewAngle;
        RectF oval = getOval(canvas, 1);
        float radius = oval.width()*0.35f;

        float currentAngle = 20;
        double curProgress = 0;
        while (currentAngle <= 170) {

            if(((int)(currentAngle * 10)) % 2 == 0) {
                length = ticksLength;
            }
            else {
                length = ticksLength/2;
            }
            canvas.drawLine(
                    (float) (oval.centerX() + Math.cos((180-currentAngle)/180*Math.PI)*(radius-length)),
                    (float) (oval.centerY() - Math.sin(currentAngle/180*Math.PI)*(radius-length)),
                    (float) (oval.centerX() + Math.cos((180-currentAngle)/180*Math.PI)*(radius)),
                    (float) (oval.centerY() - Math.sin(currentAngle/180*Math.PI)*(radius)),
                    mTicksPaint
            );

            currentAngle += step;
            curProgress += mTickStep;
        }
    }

    private void drawNeedle(Canvas canvas) {
        RectF oval = getOval(canvas, 1);
        float radius = oval.width()*0.35f + 10;
        RectF smallOval = getOval(canvas, 0.2f);

        float angle = 20 + mPosition/ mMaxGauge*140;
        canvas.drawLine(
                (float) (oval.centerX() + Math.cos((180 - angle) / 180 * Math.PI) * smallOval.width()*0.5f),
                (float) (oval.centerY() - Math.sin(angle / 180 * Math.PI) * smallOval.width()*0.5f),
                (float) (oval.centerX() + Math.cos((180 - angle) / 180 * Math.PI) * (radius)),
                (float) (oval.centerY() - Math.sin(angle / 180 * Math.PI) * (radius)),
                mNeedlePaint
        );
    }

    private void drawNeedle(Canvas canvas, float position) {
        RectF oval = getOval(canvas, 1);
        float radius = oval.width()*0.35f + 10;
        RectF smallOval = getOval(canvas, 0.2f);

        float angle = 20 + position/mMaxGauge*140;
        canvas.drawLine(
                (float) (oval.centerX() + Math.cos((180 - angle) / 180 * Math.PI) * smallOval.width()*0.5f),
                (float) (oval.centerY() - Math.sin(angle / 180 * Math.PI) * smallOval.width()*0.5f),
                (float) (oval.centerX() + Math.cos((180 - angle) / 180 * Math.PI) * (radius)),
                (float) (oval.centerY() - Math.sin(angle / 180 * Math.PI) * (radius)),
                mNeedlePaint
        );
    }


    private RectF getOval(Canvas canvas, float factor) {
        RectF oval;
        final int canvasWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        final int canvasHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();

        if (canvasHeight*2 >= canvasWidth) {
            oval = new RectF(0, 0, canvasWidth*factor, canvasWidth*factor);
        } else {
            oval = new RectF(0, 0, canvasHeight*2*factor, canvasHeight*2*factor);
        }

        oval.offset((canvasWidth-oval.width())/2 + getPaddingLeft(), (canvasHeight*2-oval.height())/2 + getPaddingTop());

        return oval;
    }
    private void init() {

        mTicksPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTicksPaint.setStrokeWidth(7.0f);
        mTicksPaint.setStyle(Paint.Style.STROKE);
        mTicksPaint.setColor(Color.WHITE);

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStrokeWidth(6.0f);
        mNeedlePaint.setStyle(Paint.Style.STROKE);
        mNeedlePaint.setColor(Color.RED);
        mNeedlePaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
        mNeedlePaint.setStyle(Paint.Style.FILL);

        mNeedleScrewPaint = new Paint();
        mNeedleScrewPaint.setAntiAlias(true);
        mNeedleScrewPaint.setColor(0xffff3f3c);
        mNeedleScrewPaint.setStyle(Paint.Style.FILL);
    }

    public void setPosition(float position) {
        if(position > mMaxGauge) {
            mPosition = mMaxGauge;
        }
        else {
            mPosition = position;
        }
        mPositionStep = Math.abs(mPosition - mIntermediatePosition)/12;
        invalidate();
    }
    public void setScale(float scale) {
        mMaxGauge = scale;
        mTickStep = scale/8;
        invalidate();
    }
}
