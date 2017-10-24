package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by bakhrom on 10/23/17.
 */

public class MPAddItemView extends LinearLayout {

    private boolean isActive = false;
    private String text = "";
    private Drawable drawable;
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
        setOrientation(VERTICAL);

        drawable = ContextCompat.getDrawable(context, R.drawable.plus);
        textSize = Utils.dpToPx(8);
        if (attributeSet != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MPAddItemView);
            text = attributeArray.getString(R.styleable.MPAddItemView_add_item_text);
            isActive = attributeArray.getBoolean(R.styleable.MPAddItemView_add_item_active, false);
            drawable = attributeArray.getDrawable(R.styleable.MPAddItemView_add_item_image);
            textSize = attributeArray.getDimensionPixelSize(R.styleable.MPAddItemView_add_item_text_size, textSize);
            attributeArray.recycle();
        }
        ImageView imageView = new ImageView(context);
        imageView.setId(R.id.add_item_plus);
        LayoutParams lp = new LayoutParams(Utils.dpToPx(20), Utils.dpToPx(20));
        lp.gravity = Gravity.CENTER;
        lp.topMargin = Utils.dpToPx(6);
        lp.bottomMargin = Utils.dpToPx(6);
        lp.weight = 1.5f;
        imageView.setLayoutParams(lp);
        imageView.setImageDrawable(drawable);
        addView(imageView);

        TextView textView = new TextView(context);
        textView.setId(R.id.add_item_text);
        LayoutParams txtLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtLp.gravity = Gravity.CENTER;
        txtLp.bottomMargin = Utils.dpToPx(6);
        txtLp.weight = 0.5f;
        textView.setTextSize(textSize);
        textView.setSingleLine();
        textView.setLayoutParams(txtLp);
        addView(textView);
        setText(text);
        initActivation(context, isActive);
    }

    public void setActivate(boolean isActive) {
        int color;
        if (isActive) {
            color = R.color.colorBlue;
        }
        else {
            color = R.color.colorTintGrey;
        }
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.add_item_bg);
        drawable.setTint(ContextCompat.getColor(getContext(), color));
        setBackground(null);
        setBackground(drawable);
        ((TextView) findViewById(R.id.add_item_text)).setTextColor(ContextCompat.getColor(getContext(), color));
        ((ImageView) findViewById(R.id.add_item_plus)).setColorFilter(ContextCompat.getColor(getContext(), color));
        this.isActive = isActive;
    }

    private void initActivation(Context context, boolean isActive) {
        int color;
        if (isActive) { color = R.color.colorBlueSecond; }
        else { color = R.color.colorTintGrey; }
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.add_item_bg);
        drawable.setTint(color);
        setBackground(null);
        setBackground(drawable);
        ((TextView) findViewById(R.id.add_item_text)).setTextColor(color);
        ((ImageView) findViewById(R.id.add_item_plus)).setColorFilter(color);
        this.isActive = isActive;
    }

    public void setText(String text) {
        ((TextView) findViewById(R.id.add_item_text)).setText(text);
    }

    public void setTextSize(int size) {
        ((TextView) findViewById(R.id.add_item_text)).setTextSize(size);
    }


}
