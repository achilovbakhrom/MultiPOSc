package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

import static com.jim.mpviews.utils.Utils.convertDpToPixel;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpButton extends android.support.v7.widget.AppCompatButton {
    boolean pressed = false;

    public MpButton(Context context) {
        super(context);
        init(context, null);
    }

    public MpButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpButton);
        if (attributeArray.getBoolean(R.styleable.MpButton_isLong, false)) {
            Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.mp_button_long);
            buttonDrawable.mutate();
            setBackgroundDrawable(buttonDrawable);
        } else {
            Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.mp_button);
            buttonDrawable.mutate();
            setBackgroundDrawable(buttonDrawable);
        }
        setAllCaps(false);
        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        setGravity(Gravity.CENTER);
        pressed = false;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            VibrateManager.startVibrate(context, 50);
                            pressed = true;
                        }
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        return false;
                }
                return false;
            }
        });
        attributeArray.recycle();
    }


}

