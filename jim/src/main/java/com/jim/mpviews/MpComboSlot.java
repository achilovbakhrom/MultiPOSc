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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.StateSaver;
import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpComboSlot extends RelativeLayout {

    private TextView mpComboName, mpComboAmount;
    private LinearLayout mpAmountBG;
    private RelativeLayout mpSlot;

    private boolean isPressed = false;

    public MpComboSlot(Context context) {
        super(context);
        init(context, null);
    }

    public MpComboSlot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpComboSlot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpComboSlot(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_combo_slot, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpComboSlot);

        mpComboAmount = (TextView) findViewById(R.id.mpComboAmount);
        mpComboName = (TextView) findViewById(R.id.mpComboName);
        mpSlot = (RelativeLayout) findViewById(R.id.mpSlot);
        mpAmountBG = (LinearLayout) findViewById(R.id.mpAmountBG);
        String text = attributeArray.getString(R.styleable.MpComboSlot_combo_name);
        mpComboName.setText(text);
        mpSlot.setBackgroundResource(R.drawable.item_bg);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate(context, 50);
                        if (!isPressed) {
                            mpSlot.setBackgroundResource(R.drawable.item_pressed_bg);
                            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10, 5,3,0);
                            mpAmountBG.setLayoutParams(params);
                            isPressed = true;
                        } else {
                            mpSlot.setBackgroundResource(R.drawable.item_bg);
                            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10, 3,0,0);
                            mpAmountBG.setLayoutParams(params);
                            isPressed = false;
                        }
                        break;
                }
                return false;
            }
        });
        attributeArray.recycle();
    }

    public void setName(String name) {
        mpComboName.setText(name);
    }

    public void setAmount(String amount) {
        mpComboAmount.setText(amount);
    }


}
