package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by DEV on 05.07.2017.
 */

public class MpSecondSwticher extends LinearLayout {

    private LinearLayout mpLeftBtn, mpRightBtn;
    private TextView mpLeftText, mpRightText;
    private boolean right = false;
    private boolean left = false;
    private CallbackFromMpSecondSwitcher clickListener;

    public MpSecondSwticher(Context context) {
        super(context);
        init(context, null);
    }

    public MpSecondSwticher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpSecondSwticher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpSecondSwticher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    public interface CallbackFromMpSecondSwitcher{
        void onLeftSideClick();
        void onRightSideClick();
    }
    public void setClickListener(CallbackFromMpSecondSwitcher clickListener){
        this.clickListener = clickListener;
    }
    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_second_switch_btn, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        mpLeftBtn = (LinearLayout) findViewById(R.id.mpLeftSide);
        mpRightBtn = (LinearLayout) findViewById(R.id.mpRightSide);
        mpLeftText = (TextView) findViewById(R.id.mpLeftText);
        mpRightText = (TextView) findViewById(R.id.mpRightText);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpSwitcher);

        mpLeftBtn.setBackgroundResource(R.drawable.left_side_pressed);
        mpRightBtn.setBackgroundResource(R.drawable.right_side);
        mpLeftText.setText(attributeArray.getText(R.styleable.MpSwitcher_text_left));
        mpRightText.setText(attributeArray.getText(R.styleable.MpSwitcher_text_right));

        mpLeftBtn.setOnClickListener(view -> {
            mpLeftBtn.setBackgroundResource(R.drawable.left_side_pressed);
            mpRightBtn.setBackgroundResource(R.drawable.right_side);
            right = false;
            left = true;
            clickListener.onLeftSideClick();
        });

        mpRightBtn.setOnClickListener(view -> {
            mpLeftBtn.setBackgroundResource(R.drawable.left_side);
            mpRightBtn.setBackgroundResource(R.drawable.right_side_pressed);
            right = true;
            left = false;
            clickListener.onRightSideClick();
        });
        attributeArray.recycle();

    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setText(String left, String right) {
        mpLeftText.setText(left);
        mpRightText.setText(right);
    }


}
