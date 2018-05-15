package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.jim.mpviews.utils.EllipsizingTextView;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpItem extends EllipsizingTextView {

    private boolean isPressed = false;

    public MpItem(Context context) {
        super(context);
        init();
    }

    public MpItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MpItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setBackgroundResource(R.drawable.item_bg);
        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        setMaxLines(4);
        setGravity(Gravity.CENTER);
    }


    public void setState(boolean state) {
        if (state) {
            setBackgroundResource(R.drawable.item_pressed_bg);
            isPressed = true;
        } else {
            setBackgroundResource(R.drawable.item_bg);
            isPressed = false;
        }
    }

    public boolean isPressed() {
        return isPressed;
    }


}
