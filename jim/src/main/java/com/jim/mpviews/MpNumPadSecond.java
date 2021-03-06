package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpNumPadSecond extends FrameLayout {

    private TextView mpValue, mpCurr;
    private LinearLayout mpNumPadbg;
    private boolean pressed = false;

    public MpNumPadSecond(Context context) {
        super(context);
        init(context, null);
    }

    public MpNumPadSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpNumPadSecond(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.mp_num_pad_second, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpCurr = (TextView) findViewById(R.id.mpCurr);
        mpValue = (TextView) findViewById(R.id.mpValue);
        mpNumPadbg = (LinearLayout) findViewById(R.id.mpNumPadBg);
        mpNumPadbg.setBackgroundResource(R.drawable.num_pad_blue);
        TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MpNumPadSecond);
        mpCurr.setText(attributeArray.getText(R.styleable.MpNumPadSecond_currency));
        mpValue.setText(attributeArray.getText(R.styleable.MpNumPadSecond_values));
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!pressed) {
                        pressed = true;
                    }
                    mpNumPadbg.setBackgroundResource(R.drawable.num_pad_blue_pressed);
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    mpNumPadbg.setBackgroundResource(R.drawable.num_pad_blue);
                    return false;
            }
            return false;
        });
        attributeArray.recycle();
    }

    public void setCurrency(String currency) {
        mpCurr.setText(currency);
    }
    public void setValue(String value){
        mpValue.setText(value);
    }

}
