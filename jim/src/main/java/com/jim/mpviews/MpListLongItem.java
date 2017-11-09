package com.jim.mpviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by developer on 18.10.2017.
 */

public class MpListLongItem extends android.support.v7.widget.AppCompatButton {
    public MpListLongItem(Context context) {
        super(context);
        init(context);
    }

    public MpListLongItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpListLongItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.list_long_item);
        buttonDrawable.mutate();
        setBackgroundDrawable(buttonDrawable);
        setAllCaps(false);
        setLines(4);
        setTextColor(context.getResources().getColorStateList(R.color.item_txt_color));
    }
}
