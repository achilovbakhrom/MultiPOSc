package com.jim.mpviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ExpandableView extends LinearLayout {

    private int size;
    private int[] weight;
    private Context context;
    private boolean hasTitle = false;
    private int[] align;

    public ExpandableView(Context context) {
        super(context);
        this.context = context;
    }

    public void create() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(HORIZONTAL);

        LayoutParams rowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setLayoutParams(rowParams);

        for (int i = 0; i < size; i++) {
            LinearLayout col = new LinearLayout(context);
            col.setOrientation(HORIZONTAL);
            LayoutParams colParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            colParams.weight = weight[i];
            col.setGravity(align == null ? Gravity.CENTER : align[i] | Gravity.CENTER_VERTICAL);
            col.setLayoutParams(colParams);
            col.setPadding(0,10,0,10);
            row.addView(col);

            TextView textView = new TextView(context);
            LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorReportText));
            switch (align[i]) {
                case Gravity.LEFT:
                    if (i == 0)
                        textView.setPadding(30, 0, 10, 0);
                    else
                        textView.setPadding(20, 0, 10, 0);
                    break;
                case Gravity.RIGHT:
                    if (!hasTitle) {
                        if (i == size - 1)
                            textView.setPadding(10, 0, 30, 0);
                        else
                            textView.setPadding(10, 0, 20, 0);
                    }
                    break;
                case Gravity.CENTER:
                    textView.setPadding(10, 0, 10, 0);
                    break;
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.sixteen_dp));
            textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
            textView.setLayoutParams(textParams);
//            textView.setEllipsize(TextUtils.TruncateAt.END);
//            textView.setMaxLines(1);
            col.addView(textView);
            if (hasTitle) {
                LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageParams.width = 17;
                imageParams.height = 15;
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(imageParams);
                imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTintGrey)));
                imageView.setImageResource(R.drawable.sorting);
                imageView.setVisibility(View.GONE);
                if (align[i] == Gravity.RIGHT && i == size - 1) {
                    imageParams.setMargins(10, 0, 30, 0);
                } else imageParams.setMargins(10, 0, 20, 0);
                col.addView(imageView);
            }

            if (col.getChildCount() == 2 && hasTitle && align[i] == Gravity.RIGHT) {
                ImageView imageView = (ImageView) col.getChildAt(1);
                if (imageView.getVisibility() == GONE && i == size - 1)
                    textView.setPadding(0, 0, 30, 0);
                else if (imageView.getVisibility() == GONE && i != size - 1) {
                    textView.setPadding(0, 0, 20, 0);
                }

            }

            if (i != size - 1) {
                FrameLayout borderLine = new FrameLayout(context);
                LayoutParams borderParams = new LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT);
                borderLine.setLayoutParams(borderParams);
                borderLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorReportBorderLine));
                row.addView(borderLine);
            }
        }
        addView(row);
        FrameLayout bottomLine = new FrameLayout(context);
        LayoutParams borderParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        bottomLine.setLayoutParams(borderParams);
        bottomLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorReportBorderLine));
        addView(bottomLine);
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHasTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }

    public void setAlign(int[] align) {
        this.align = align;
    }

    public void sorted(int sorting, int finalCount) {
        if (getChildAt(0) instanceof LinearLayout) {
            LinearLayout row = (LinearLayout) getChildAt(0);
            int count = 0;
            for (int i = 0; i < row.getChildCount(); i++) {
                if (row.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout col = (LinearLayout) row.getChildAt(i);
                    for (int j = 0; j < col.getChildCount(); j++) {
                        if (col.getChildAt(j) instanceof TextView) {
                            TextView textView = (TextView) col.getChildAt(j);
                            if (align[count] == Gravity.RIGHT) {
                                if (count == finalCount) {
                                    textView.setPadding(0, 0, 10, 0);
                                } else if (count == size - 1)
                                    textView.setPadding(0, 0, 30, 0);
                                else if (count != size - 1) {
                                    textView.setPadding(0, 0, 20, 0);
                                }

                            }
                        }
                        if (col.getChildAt(j) instanceof ImageView) {
                            ImageView imageView = (ImageView) col.getChildAt(j);
                            if (count == finalCount) {
                                imageView.setVisibility(VISIBLE);
                            } else imageView.setVisibility(GONE);

                            if (sorting == 1) {
                                imageView.setImageResource(R.drawable.sorting);
                            } else {
                                imageView.setImageResource(R.drawable.sorting_invert);
                            }
                            count++;
                        }
                    }
                }
            }

        }
    }
}
