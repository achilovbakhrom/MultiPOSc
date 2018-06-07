package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpLightButton extends FrameLayout {

    private boolean isPressed = false;
    private TextView mpLightBtnText;
    private RelativeLayout relativeLayout;
    private ImageView mpLightBtnImage;
    private ImageView mpLightBtnBg;
    private final int IMAGE_MODE = 0;
    private final int TEXT_MODE = 1;
    int mode;

    public MpLightButton(Context context) {
        super(context);
        init(context, null);
    }

    public MpLightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpLightButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    boolean pressed;

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_light_button, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpLightButton);
        mpLightBtnText = (TextView) findViewById(R.id.mpLightBtnText);
        mpLightBtnImage = (ImageView) findViewById(R.id.mpLightBtnImage);
        relativeLayout = (RelativeLayout) findViewById(R.id.mpRelativeL);
        mpLightBtnBg = (ImageView) findViewById(R.id.mpLightBtnBg);
        mode = attributeArray.getInt(R.styleable.MpLightButton_btn_mode, TEXT_MODE);
        mpLightBtnText.setText(attributeArray.getText(R.styleable.MpLightButton_text_content));
        mpLightBtnImage.setImageResource(attributeArray.getResourceId(R.styleable.MpLightButton_image_content, 0));
        switch (mode) {
            case TEXT_MODE:
                mpLightBtnImage.setVisibility(GONE);
                mpLightBtnText.setVisibility(VISIBLE);
                break;
            case IMAGE_MODE:
                mpLightBtnImage.setVisibility(VISIBLE);
                mpLightBtnText.setVisibility(GONE);
        }
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.mp_button);
        buttonDrawable.mutate();
        mpLightBtnBg.setImageDrawable(buttonDrawable);
        mpLightBtnBg.setClickable(true);
        attributeArray.recycle();
    }

    public void setOnLightButtonClickListener(OnClickListener clickListener){
        mpLightBtnBg.setOnClickListener(clickListener);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setText(String text) {
        mpLightBtnText.setText(text);
    }

    public void setImage(int resId) {
        mpLightBtnImage.setImageResource(resId);
    }


}
