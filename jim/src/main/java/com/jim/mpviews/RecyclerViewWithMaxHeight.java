package com.jim.mpviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jim.mpviews.utils.Utils;

/**
 * Created by DEV on 15.09.2017.
 */

public class RecyclerViewWithMaxHeight extends RecyclerView {

    private int maxHeight = 100;

    public RecyclerViewWithMaxHeight(Context context) {
        super(context);
    }

    public RecyclerViewWithMaxHeight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithMaxHeight(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(Utils.dpToPx(maxHeight), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
