package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by bakhrom on 10/23/17.
 */

public class MPListItemView extends FrameLayout {

    private boolean isActive = false;
    private String text = "";
    private Drawable drawable;
    private int textSize;


    public MPListItemView(Context context) {
        super(context);
        init(context, null);
    }

    public MPListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MPListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MPListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }




    private void init(Context context, AttributeSet attributeSet) {

        textSize = Utils.dpToPx(12);
        if (attributeSet != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MPListItemView);
            text = attributeArray.getString(R.styleable.MPListItemView_list_item_text);
            isActive = attributeArray.getBoolean(R.styleable.MPListItemView_list_item_active, false);
            textSize = attributeArray.getDimensionPixelSize(R.styleable.MPListItemView_list_item_text_size, textSize);
            attributeArray.recycle();
        }
        if (isActive) {
            drawable = ContextCompat.getDrawable(context, R.drawable.item_pressed_bg);
        } else {
            drawable = ContextCompat.getDrawable(context, R.drawable.item_bg);
        }
        TextView textView = new TextView(context);
        textView.setId(R.id.list_item_text);
        LayoutParams txtLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        txtLp.gravity = Gravity.CENTER;
        txtLp.leftMargin = Utils.dpToPx(10);
        txtLp.rightMargin = Utils.dpToPx(10);
        txtLp.topMargin = Utils.dpToPx(10);
        txtLp.bottomMargin = Utils.dpToPx(10);
        textView.setLayoutParams(txtLp);
        textView.setTextSize(textSize);
        textView.setLines(4);
        textView.setGravity(Gravity.CENTER);
        addView(textView);
        setText(text);
        initActivation(context, isActive);
    }

    public void setActivate(boolean isActive) {
        Drawable drawable;
        if (isActive) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.item_pressed_bg);
        }
        else {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.item_bg);
        }
        setBackground(null);
        setBackground(drawable);
        this.isActive = isActive;
    }

    private void initActivation(Context context, boolean isActive) {
        Drawable drawable;
        if (isActive) {
            drawable = ContextCompat.getDrawable(context, R.drawable.item_pressed_bg);
        }
        else {
            drawable = ContextCompat.getDrawable(context, R.drawable.item_bg);
        }
        setBackground(null);
        setBackground(drawable);
        this.isActive = isActive;
    }


    public void setText(String text) {
        ((TextView) findViewById(R.id.list_item_text)).setText(text);
    }

    public void setTextSize(int size) {
        ((TextView) findViewById(R.id.list_item_text)).setTextSize(size);
    }

    public void makeDeleteable(boolean isDeleteable) {
        if (isDeleteable) {
            setAlpha(0.3f);
        }
        else {
            setAlpha(1.0f);
        }
    }

}
