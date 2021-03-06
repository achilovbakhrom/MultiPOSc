package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by DEV on 05.07.2017.
 */

public class MpSwitcher extends LinearLayout {

    private LinearLayout mpLeftBtn, mpRightBtn;
    private ImageView mpLeftImage, mpRightImage;
    private TextView mpLeftText, mpRightText;
    private boolean right = false;
    private boolean left = false;
    private OnSwitcherStateChangedListener listener;

    public MpSwitcher(Context context) {
        super(context);
        init(context, null);
    }

    public MpSwitcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpSwitcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setSwitcherStateChangedListener(OnSwitcherStateChangedListener listener) {
        this.listener = listener;
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_switch_btn, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        mpLeftBtn = findViewById(R.id.mpLeftSide);
        mpRightBtn = findViewById(R.id.mpRightSide);
        mpLeftImage = findViewById(R.id.mpLeftImage);
        mpRightImage = findViewById(R.id.mpRightImage);
        mpLeftText = findViewById(R.id.mpLeftText);
        mpRightText = findViewById(R.id.mpRightText);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpSwitcher);

        mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
        mpLeftImage.setImageResource(R.drawable.ellipse);
        mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
        mpRightImage.setImageResource(R.drawable.ellipse_not_active);
        mpLeftText.setText(attributeArray.getText(R.styleable.MpSwitcher_text_left));
        mpRightText.setText(attributeArray.getText(R.styleable.MpSwitcher_text_right));
        left = true;
        mpLeftBtn.setOnClickListener(view -> {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
            mpRightImage.setImageResource(R.drawable.ellipse_not_active);
            right = false;
            left = true;
            listener.onStateChange(right, left);
        });

        mpRightBtn.setOnClickListener(view -> {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse_not_active);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_pressed_bg);
            mpRightImage.setImageResource(R.drawable.ellipse);
            right = true;
            left = false;
            listener.onStateChange(right, left);
        });

        attributeArray.recycle();
    }

    public interface OnSwitcherStateChangedListener {
        void onStateChange(boolean isRight, boolean isLeft);
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setRight(boolean right) {
        this.right = right;
        this.left = !right;
        if (right) {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse_not_active);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_pressed_bg);
            mpRightImage.setImageResource(R.drawable.ellipse);
        } else {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
            mpRightImage.setImageResource(R.drawable.ellipse_not_active);
        }
    }

    public void setLeft(boolean left) {
        this.left = left;
        this.right = !left;
        if (left) {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
            mpRightImage.setImageResource(R.drawable.ellipse_not_active);
        } else {
            mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_bg);
            mpLeftImage.setImageResource(R.drawable.ellipse_not_active);
            mpRightBtn.setBackgroundResource(R.drawable.matrix_right_pressed_bg);
            mpRightImage.setImageResource(R.drawable.ellipse);
        }
    }

    public LinearLayout getMpLeftBtn() {
        return mpLeftBtn;
    }

    public LinearLayout getMpRightBtn() {
        return mpRightBtn;
    }

    public void setText(String left, String right) {
        mpLeftText.setText(left);
        mpRightText.setText(right);
    }

}
