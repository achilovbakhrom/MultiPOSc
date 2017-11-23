package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by Achilov Bakhrom on 10/23/17.
 */

public class MPAddItemView extends FrameLayout {

    private boolean isActive = false;
    private String text = "";

    private int textSize;

    public MPAddItemView(Context context) {
        super(context);
        init(context, null);
    }

    public MPAddItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MPAddItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MPAddItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        textSize = Utils.dpToPx(9);
        if (attributeSet != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MPAddItemView);
            text = attributeArray.getString(R.styleable.MPAddItemView_add_item_text);
            isActive = attributeArray.getBoolean(R.styleable.MPAddItemView_add_item_active, false);
            textSize = attributeArray.getDimensionPixelSize(R.styleable.MPAddItemView_add_item_text_size, textSize);
            attributeArray.recycle();
        }

        TextView textView = new TextView(context);
        textView.setId(R.id.add_item_text);
        textView.setGravity(Gravity.CENTER);
        LayoutParams txtLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txtLp.gravity = Gravity.CENTER;
        textView.setTextSize(textSize);
        textView.setTextColor(Color.parseColor("#27af27"));
        textView.setLayoutParams(txtLp);
        addView(textView);
        setText(text);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.item_bg);
        setBackground(null);
        setBackground(drawable);
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Drawable drawable1 = ContextCompat.getDrawable(context, R.drawable.item_pressed_bg);
                    setBackground(drawable1);
                    return false;
                case MotionEvent.ACTION_UP:
                    setBackground(drawable);
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    setBackground(drawable);
                    return false;
            }
            return false;
        });
        initActivation(context, isActive);
    }

    private void initActivation(Context context, boolean isActive) {
        this.isActive = isActive;
    }

    public void setText(String text) {
        ((TextView) findViewById(R.id.add_item_text)).setText(text);
    }

    public void setTextSize(int size) {
        ((TextView) findViewById(R.id.add_item_text)).setTextSize(size);
    }


}
