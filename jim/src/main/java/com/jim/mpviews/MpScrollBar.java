package com.jim.mpviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Sirojiddin on 14.11.2017.
 */

public class MpScrollBar extends RelativeLayout {

    private Context context;
    private ImageView thumb;

    public MpScrollBar(Context context) {
        super(context);
        init(context);
    }

    public MpScrollBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MpScrollBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_scrollbar, this);
        thumb = (ImageView) findViewById(R.id.ivThumb);
    }

    public void setThumbY(float y){
        thumb.setY(y);
    }
}
