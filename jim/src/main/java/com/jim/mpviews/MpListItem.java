package com.jim.mpviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by developer on 18.10.2017.
 */

public class MpListItem extends android.support.v7.widget.AppCompatButton {
    public MpListItem(Context context) {
        super(context);
        init(context);
    }

    public MpListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.list_item);
        buttonDrawable.mutate();
        setBackgroundDrawable(buttonDrawable);
        setAllCaps(false);
        setPadding(8,0,8,0);
        setGravity(Gravity.CENTER);
        setLines(4);
        setTextColor(context.getResources().getColorStateList(R.color.item_txt_color));
    }
}
