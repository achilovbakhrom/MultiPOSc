package com.jim.mpviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class MpButtonWithImg extends LinearLayout {

    private boolean isPressed = false;

    public MpButtonWithImg(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MpButtonWithImg(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpButtonWithImg(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.btn_with_img, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpButtonWithImg);

        String text = attributeArray.getString(R.styleable.MpButtonWithImg_bwi_text);
        ((TextView) findViewById(R.id.btnWithText)).setText(text);
        ((TextView) findViewById(R.id.btnWithText)).setTextColor(ColorStateList.valueOf(attributeArray.getColor(R.styleable.MpButtonWithImg_bwi_txt_color, getResources().getColor(R.color.colorBlue))));
        ((ImageView) findViewById(R.id.btnWithImg)).setImageTintList(ColorStateList.valueOf(attributeArray.getColor(R.styleable.MpButtonWithImg_bwi_img_color, getResources().getColor(R.color.colorBlue))));
        ((ImageView) findViewById(R.id.btnWithImg)).setImageResource(attributeArray.getResourceId(R.styleable.MpButtonWithImg_bwi_img, 0));

        setBackgroundResource(R.drawable.long_mp_button);
        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        setGravity(Gravity.CENTER);
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isPressed) {
                        setBackgroundResource(R.drawable.long_mp_button_pressed);
                        isPressed = true;
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    isPressed = false;
                    setBackgroundResource(R.drawable.long_mp_button);
                    return false;
            }
            return false;
        });
        attributeArray.recycle();
    }

}
