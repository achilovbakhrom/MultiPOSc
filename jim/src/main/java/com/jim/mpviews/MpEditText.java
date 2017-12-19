package com.jim.mpviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
        init(context, null);
    }

    public MpEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLines(1);
        setMaxLines(1);
        setSingleLine();
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.edit_text_bg));
        } else {
            setBackgroundResource(R.drawable.edit_text_bg);
        }
        //check attributes you need, for example all paddings
        int[] attributes = new int[]{android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingBottom, android.R.attr.paddingRight};

        //then obtain typed array
        TypedArray arr = context.obtainStyledAttributes(attrs, attributes);

        //You can check if attribute exists (in this examle checking paddingRight)
        //int paddingRight = arr.hasValue(3) ? arr.getDimensionPixelOffset(3, -1) : 10;

        Resources r = getResources();
        if (!arr.hasValue(0) && !arr.hasValue(1) && !arr.hasValue(2) && !arr.hasValue(3)) {
            int topPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
            int sidePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            setPadding(sidePadding, topPadding, sidePadding, topPadding);
        } else {
            setPadding(arr.getDimensionPixelOffset(0, -1), arr.getDimensionPixelOffset(1, -1), arr.getDimensionPixelOffset(3, -1), arr.getDimensionPixelOffset(2, -1));
        }
        setHintTextColor(ContextCompat.getColor(context, R.color.colorTextHint));
        setTextColor(ContextCompat.getColor(context, R.color.colorMainText));
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
    }
}
