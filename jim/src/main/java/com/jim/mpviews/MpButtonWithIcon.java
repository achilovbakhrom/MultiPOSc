package com.jim.mpviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.Utils;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpButtonWithIcon extends RelativeLayout {

    private boolean isPressed = false;

    public MpButtonWithIcon(Context context) {
        super(context);
        init(context, null);
    }

    public MpButtonWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpButtonWithIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_btn_with_icon, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.mp_button);
        buttonDrawable.mutate();
        setBackgroundDrawable(buttonDrawable);
        setClickable(true);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpButtonWithIcon);

        String text = attributeArray.getString(R.styleable.MpButtonWithIcon_btn_text);
        ((TextView) findViewById(R.id.mpBtnText)).setText(text);
        ((ImageView)findViewById(R.id.mpBtnIcon)).setImageTintList(ColorStateList.valueOf(attributeArray.getColor(R.styleable.MpButtonWithIcon_tint_color, getResources().getColor(R.color.colorBlue))));
        ((ImageView) findViewById(R.id.mpBtnIcon)).setImageResource(attributeArray.getResourceId(R.styleable.MpButtonWithIcon_src, 0));

        setPadding((int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(10));
        setGravity(Gravity.CENTER);
        attributeArray.recycle();
    }

}
