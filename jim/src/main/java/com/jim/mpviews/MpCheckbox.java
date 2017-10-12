package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Пользователь on 23.05.2017.
 */

public class MpCheckbox extends RelativeLayout {

    public MpCheckbox(Context context) {
        super(context);
        init(context, null);
    }

    public MpCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpCheckbox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_checkbox, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpCheckbox);
        boolean state = attributeArray.getBoolean(R.styleable.MpCheckbox_checked, false);

        String text = attributeArray.getString(R.styleable.MpCheckbox_text);
        setText(text);

        if (state) {
            findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.checked);
            ((CheckBox) findViewById(R.id.mpCheckbox)).setChecked(true);
        } else {
            findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.unchecked);
            ((CheckBox) findViewById(R.id.mpCheckbox)).setChecked(false);
        }

        ((CheckBox) findViewById(R.id.mpCheckbox)).setButtonDrawable(null);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.mpCheckbox).isEnabled())
                    if (((CheckBox) findViewById(R.id.mpCheckbox)).isChecked()) {
                        findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.unchecked);
                        ((CheckBox) findViewById(R.id.mpCheckbox)).setChecked(false);
                        animateCheckbox();
                    } else {
                        findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.checked);
                        ((CheckBox) findViewById(R.id.mpCheckbox)).setChecked(true);
                        animateCheckbox();
                    }
            }
        });
        attributeArray.recycle();
    }

    public void animateCheckbox() {
        Animation anim = new ScaleAnimation(
                0.5f, 1f,
                0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(200);
        findViewById(R.id.mpCheckbox).startAnimation(anim);
    }

    public void setText(String text) {
        ((TextView)findViewById(R.id.tvCheckbox)).setText(text);
    }

    public void setChecked(Boolean state) {
        if (state) {
            animateCheckbox();
            findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.checked);
            ((CheckBox)findViewById(R.id.mpCheckbox)).setChecked(true);
        } else {
            animateCheckbox();
            findViewById(R.id.mpCheckbox).setBackgroundResource(R.drawable.unchecked);
            ((CheckBox)findViewById(R.id.mpCheckbox)).setChecked(false);
        }
    }

    public boolean isCheckboxChecked() {
        return ((CheckBox)findViewById(R.id.mpCheckbox)).isChecked();
    }

    public void setCheckBoxEnabled(boolean state) {
        findViewById(R.id.mpCheckbox).setEnabled(state);
    }

    public void setCheckBoxClickable(boolean clickable) {
        findViewById(R.id.mpCheckbox).setClickable(clickable);
    }

    public CheckBox getCheckBox() {
        return ((CheckBox)findViewById(R.id.mpCheckbox));
    }
}
