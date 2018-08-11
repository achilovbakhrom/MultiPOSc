package com.jim.mpviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MpCircleWithText extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    int backColor = Color.parseColor("#DCE9F0");
    int frontColor = Color.parseColor("#94CAEC");
    int textColor = Color.parseColor("#4C4C4C");
    int textSize = 20;
    int size;
    String text = "";
    ValueAnimator mTimerAnimator;
    float sweepAngle = 0f;
    int angle;

    public MpCircleWithText(Context context) {
        super(context);
        init(context, null);
    }

    public MpCircleWithText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpCircleWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MpCircleWithText);
            if (array != null) {
                backColor = array.getColor(R.styleable.MpCircleWithText_circleBackColor, backColor);
                frontColor = array.getColor(R.styleable.MpCircleWithText_circleFrontColor, frontColor);
                textColor = array.getColor(R.styleable.MpCircleWithText_textColor, textColor);
                textSize = array.getDimensionPixelSize(R.styleable.MpCircleWithText_textSize, textSize);
                text = String.valueOf(array.getText(R.styleable.MpCircleWithText_circle_text));
            }
        }
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        size = Math.min(getWidth(), getHeight());
        paint.setColor(backColor);
        canvas.drawCircle(size / 2f, size / 2f, size / 2 - 10, paint);

        paint.setColor(Color.RED);
        canvas.drawArc(10, 10, size - 10, size - 10, 0f, sweepAngle, false, paint);

        canvas.drawText(text, size / 2, size / 2, textPaint);
    }

    public void setPercentandText(float percent, String text) {
        angle = (int) (360 * percent / 100);
        this.text = text;
        start();
    }

    void start() {
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(4000);
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(animation -> drawProgress());
        mTimerAnimator.start();
    }

    void drawProgress() {
        if (sweepAngle < angle)
            sweepAngle += 3;
        else mTimerAnimator.cancel();

        invalidate();
    }

}
