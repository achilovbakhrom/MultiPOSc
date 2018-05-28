package com.jim.mpviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Created by developer on 18.10.2017.
 */

public class MpActionButton extends android.support.v7.widget.AppCompatButton {
    public MpActionButton(Context context) {
        super(context);
        init(context);
    }

    public MpActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.action_button);
        buttonDrawable.mutate();
        setBackgroundDrawable(buttonDrawable);
        setGravity(Gravity.CENTER);
        setAllCaps(false);
    }
}
