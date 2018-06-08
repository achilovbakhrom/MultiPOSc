package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 17.10.17.
 */

public class MpCompletedStateView extends AppCompatImageView {
    public static final int EMPTY_STATE = 0;
    public static final int COMPLETED_STATE = 1;
    public static final int WARNING_STATE = 2;

    private int state = EMPTY_STATE;

    public MpCompletedStateView(Context context) {
        super(context);
    }

    public MpCompletedStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MpCompletedStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(int state) {
        switch (state) {
            case EMPTY_STATE:
                setImageResource(R.drawable.ic_empty_stroke);
                break;
            case COMPLETED_STATE:
                setImageResource(R.drawable.ic_checked);
                break;
            case WARNING_STATE:
                setImageResource(R.drawable.ic_warning_stroke);
                break;
        }
        this.state = state;
    }

    public int getState(){
        return this.state;
    }


}
