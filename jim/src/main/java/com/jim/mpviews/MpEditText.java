package com.jim.mpviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Utils;

/**
 * Created by developer on 16.05.2017.
 */

public class MpEditText extends android.support.v7.widget.AppCompatEditText {
    public MpEditText(Context context) {
        super(context);
        init(context);
    }

    public MpEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLines(1);
        setMaxLines(1);
        setSingleLine();
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.edit_text_bg));
        } else {
            setBackgroundResource(R.drawable.edit_text_bg);
        }
        Resources r = getResources();
        if(getPaddingTop()==0 && getPaddingBottom()==0 && getPaddingRight()==0 && getPaddingLeft()==0) {
            int topPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
            int sidePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            setPadding(sidePadding, topPadding, sidePadding, topPadding);
        }
        setHintTextColor(ContextCompat.getColor(context, R.color.colorTextHint));
        setTextColor(ContextCompat.getColor(context, R.color.colorMainText));
    }
}
