package com.jim.mpviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by developer on 18.10.2017.
 */

public class MpMiniActionButton extends android.support.v7.widget.AppCompatImageView {
    public MpMiniActionButton(Context context) {
        super(context);
        init(context);
    }

    public MpMiniActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpMiniActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.mini_action_button);
        buttonDrawable.mutate();
        setBackgroundDrawable(buttonDrawable);
        setClickable(true);
    }
    public void disable(){
        setColorFilter(Color.parseColor("#d1cccc"));
        setEnabled(false);
    }
    public void enable(){
        setColorFilter(null);
        setEnabled(true);
    }
}
