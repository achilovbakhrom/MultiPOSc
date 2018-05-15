package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpButtonLong extends android.support.v7.widget.AppCompatTextView {

    private boolean pressed = false;

    public MpButtonLong(Context context) {
        super(context);
        init(context, null);
    }

    public MpButtonLong(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpButtonLong(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        setBackgroundResource(R.drawable.long_mp_button);
        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        if(getGravity()!=Gravity.START)
        setGravity(Gravity.CENTER);
        pressed = false;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!pressed){
                            VibrateManager.startVibrate(context, 50);
                            pressed= true;
                        }
                        setBackgroundResource(R.drawable.long_mp_button_pressed);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        setBackgroundResource(R.drawable.long_mp_button);
                        return false;
                }
                return false;
            }
        });
    }

}
