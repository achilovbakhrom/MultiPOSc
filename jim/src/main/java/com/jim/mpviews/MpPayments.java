package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;


/**
 * Created by Пользователь on 26.05.2017.
 */

public class MpPayments extends RelativeLayout {

    private TextView mpType, mpPercent;
    private LinearLayout mpPayment;
    private boolean isPressed = false;
    private OnTouchCustom touchCustom;

    public interface OnTouchCustom{
        void onPressed();
    }
    public void setTouchCustom(OnTouchCustom touchCustom){

        this.touchCustom = touchCustom;
    }
    public MpPayments(Context context) {
        super(context);
        init(context, null);
    }

    public MpPayments(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpPayments(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpPayments(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_payments, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpPayments);
        mpType = (TextView) findViewById(R.id.mpType);
        mpPercent = (TextView) findViewById(R.id.mpPercent);
        mpPayment = (LinearLayout) findViewById(R.id.mpPayment);
        mpPayment.setBackgroundResource(R.drawable.rounded_btn);
        mpType.setTextColor(getResources().getColor(R.color.colorBlue));
        mpType.setText(attributeArray.getText(R.styleable.MpPayments_type));
        mpPercent.setText(attributeArray.getText(R.styleable.MpPayments_percent));
        mpPercent.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrateManager.startVibrate(context, 50);
                    mpPayment.setBackgroundResource(R.drawable.rounded_blue_btn);
                    mpType.setTextColor(getResources().getColor(R.color.colorWhite));
                    mpPercent.setTextColor(getResources().getColor(R.color.colorWhite));
                    touchCustom.onPressed();
            }
            return false;
        });
        attributeArray.recycle();
    }

    public void setType(String type) {
        mpType.setText(type);
    }

    public void setPercent(String percent) {
        mpPercent.setText(percent);
    }

    private String key = null;

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            mpPayment.setBackgroundResource(R.drawable.rounded_blue_btn);
            mpType.setTextColor(getResources().getColor(R.color.colorWhite));
            mpPercent.setTextColor(getResources().getColor(R.color.colorWhite));
            isPressed = true;
        } else {
            mpPayment.setBackgroundResource(R.drawable.rounded_btn);
            mpType.setTextColor(getResources().getColor(R.color.colorBlue));
            mpPercent.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            isPressed = false;
        }
    }

}
