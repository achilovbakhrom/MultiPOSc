package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

import static com.jim.mpviews.utils.Utils.convertDpToPixel;

/**
 * Created by Пользователь on 24.05.2017.
 */

public class MpNumPad extends FrameLayout {

    private TextView textView;
    private RelativeLayout layout;
    private ImageView mpNumPadImg;
    private int mode;
    private final int NUMBER_MODE = 0;
    private final int ACTION_MODE = 1;

    public MpNumPad(Context context) {
        super(context);
        init(context, null);
    }

    public MpNumPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpNumPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    boolean pressed = false;

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.mp_num_pad, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        textView = (TextView) findViewById(R.id.mpNumPadText);
        layout = (RelativeLayout) findViewById(R.id.mpNumPadBg);
        mpNumPadImg = (ImageView) findViewById(R.id.mpNumPadImg);
        TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MpNumPad);
        textView.setText(attributeArray.getText(R.styleable.MpNumPad_value));
        mpNumPadImg.setImageDrawable(attributeArray.getDrawable(R.styleable.MpNumPad_img));
        mode = attributeArray.getInt(R.styleable.MpNumPad_mode, NUMBER_MODE);
        switch (mode) {
            case NUMBER_MODE:
                layout.setBackgroundResource(R.drawable.num_pad_btn);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.twenty_three_dp));
                textView.setTextColor(getResources().getColor(R.color.colorBlue));
                break;
            case ACTION_MODE:
                layout.setBackgroundResource(R.drawable.num_pad_blue);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.fourteen_dp));
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                        }
                        if (mode == NUMBER_MODE) {
                            layout.setBackgroundResource(R.drawable.item_pressed_bg);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.twenty_two_dp));
                        } else layout.setBackgroundResource(R.drawable.num_pad_blue_pressed);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        if (mode == NUMBER_MODE) {
                            layout.setBackgroundResource(R.drawable.num_pad_btn);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.twenty_three_dp));
                        } else layout.setBackgroundResource(R.drawable.num_pad_blue);
                        return false;
                }
                return false;
            }
        });
        attributeArray.recycle();
    }

    public ImageView getNumPadImageView(){ return mpNumPadImg;}
    public TextView getNumPadTextView(){return textView;}

}
