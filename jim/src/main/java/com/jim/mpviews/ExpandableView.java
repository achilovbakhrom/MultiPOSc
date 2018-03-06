package com.jim.mpviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ExpandableView extends LinearLayout {

    private int size;
    private int[] weight;
    private Context context;

    public ExpandableView(Context context) {
        super(context);
        this.context = context;
    }

    public void create() {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 5);
        setLayoutParams(layoutParams);
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(context);
            LayoutParams textParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.weight = weight[i];
            textView.setPadding(0, 0, 0, 0);
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorMainText));
            textView.setTextSize(18);
            textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setLayoutParams(textParams);
            addView(textView);
        }
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
