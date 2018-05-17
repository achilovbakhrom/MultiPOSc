package com.jim.mpviews;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpButtonWihCheckbox extends android.support.v7.widget.AppCompatTextView {

    private boolean state = false;

    public MpButtonWihCheckbox(Context context) {
        super(context);
        init(context, null);
    }

    public MpButtonWihCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpButtonWihCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        setBackgroundResource(R.drawable.unchecked_btn);
        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        setGravity(Gravity.CENTER);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!state) {
                            setBackgroundResource(R.drawable.checked_btn);
                            state = true;
                            setTextColor(getResources().getColor(R.color.colorBlue));
                        } else {
                            setBackgroundResource(R.drawable.unchecked_btn);
                            state = false;
                            setTextColor(getResources().getColor(R.color.colorMainText));
                        }
                        break;
                }
                return false;
            }
        });
    }


    public void setChecked(boolean state) {
        if (state) {
            setBackgroundResource(R.drawable.checked_btn);
            this.state = true;
        } else {
            setBackgroundResource(R.drawable.unchecked_btn);
            this.state = false;
        }
    }

    public boolean isChecked(){
        return state;
    }


}
