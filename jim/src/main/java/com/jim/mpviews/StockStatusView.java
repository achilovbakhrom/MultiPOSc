package com.jim.mpviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

public class StockStatusView extends FrameLayout {

    private int available = 100;
    private int sold = 0;
    private int count = 100;
    private Paint paint;

    public StockStatusView(@NonNull Context context) {
        super(context);
        paint = new Paint();
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        canvas.drawRect(30, 30, 80, 80, paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(33, 60, 77, 77, paint );
        paint.setColor(Color.YELLOW);
        canvas.drawRect(33, 33, 77, 60, paint );
    }
}
