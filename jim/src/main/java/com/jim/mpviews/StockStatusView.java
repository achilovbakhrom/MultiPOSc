package com.jim.mpviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class StockStatusView extends View {

    private double available;
    private double sold = 0;
    private double count = 0;
    private double max;
    private boolean expired = false;
    private boolean closeToExpire = false;

    public void setExpired(boolean expired) {
        this.expired = expired;
        closeToExpire = !expired;
    }

    public void setCloseToExpire(boolean closeToExpire) {
        this.closeToExpire = closeToExpire;
        expired = !closeToExpire;
    }

    private Paint paint;

    public StockStatusView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public StockStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StockStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StockStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public void setSold(double sold) {
        this.sold = sold;
        available = max - sold;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public void setMax(double max) {
        this.max = max;
        available = max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        float x = getX() + paddingLeft + paddingRight;
        float y = getY() + paddingTop + paddingBottom;
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
        paint.setStrokeWidth(0);
        canvas.drawRect(getX(), getY(), getWidth(), getHeight(), paint);
        if (available != max) {
            if (count == 0) {
                if (sold != max) {
                    int soldPercent = (int) (sold * 100 / max);
                    int soldHeight = contentHeight * soldPercent / 100;
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                    paint.setStrokeWidth(0);
                    canvas.drawRect(x, y, contentWidth, soldHeight, paint);
                    if (expired) {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, soldHeight, contentWidth, contentHeight, paint);
                    } else if (closeToExpire) {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, soldHeight, contentWidth, contentHeight, paint);
                    } else {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, soldHeight, contentWidth, contentHeight, paint);
                    }
                } else {
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                    paint.setStrokeWidth(0);
                    canvas.drawRect(x, y, contentWidth, contentHeight, paint);
                }
            } else {
                if (sold != max) {
                    if (expired) {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, y, contentWidth, contentHeight, paint);
                    } else if (closeToExpire) {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, y, contentWidth, contentHeight, paint);
                    } else {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, y, contentWidth, contentHeight, paint);
                    }
                    int soldPercent = (int) (sold * 100 / max);
                    if (soldPercent <= 10)
                        soldPercent = 20;
                    int soldHeight = contentHeight * soldPercent / 100;
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                    paint.setStrokeWidth(0);
                    canvas.drawRect(x, y, contentWidth, soldHeight, paint);
                    if (count == max - sold) {
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, soldHeight, contentWidth, contentHeight, paint);
                    } else {
                        int countPercent = (int) ((count + sold) * 100 / max);
                        int countHeight = contentHeight * countPercent / 100;
                        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                        paint.setStrokeWidth(0);
                        canvas.drawRect(x, soldHeight, contentWidth, countHeight, paint);
                    }
                } else {
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
                    paint.setStrokeWidth(0);
                    canvas.drawRect(x, y, contentWidth, contentHeight, paint);
                }
            }
        } else {
            if (expired) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                paint.setStrokeWidth(0);
                canvas.drawRect(x, y, contentWidth, contentHeight, paint);
            } else if (closeToExpire) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                paint.setStrokeWidth(0);
                canvas.drawRect(x, y, contentWidth, contentHeight, paint);
            } else {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                paint.setStrokeWidth(0);
                canvas.drawRect(x, y, contentWidth, contentHeight, paint);
            }
            if (count != 0) {
                int countPercent = (int) (count * 100 / max);
                if (countPercent <= 10)
                    countPercent = 20;
                int countHeight = contentHeight * countPercent / 100;
                paint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
                paint.setStrokeWidth(0);
                canvas.drawRect(x, y, contentWidth, countHeight, paint);
            }
        }

    }
}
