package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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

public class MpTripleSwitcher extends LinearLayout {

    private LinearLayout mpLeftBtn, mpRightBtn, mpCenterBtn;
    private ImageView mpLeftImage, mpRightImage, mpCenterImage;
    private TextView mpLeftText, mpRightText, mpCenterText;
    private boolean right = false;
    private boolean center = false;
    private boolean left = false;

    public MpTripleSwitcher(Context context) {
        super(context);
        init(context, null);
    }

    public MpTripleSwitcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpTripleSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpTripleSwitcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_triple_switch_btn, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        mpLeftBtn = (LinearLayout) findViewById(R.id.mpLeftSide);
        mpRightBtn = (LinearLayout) findViewById(R.id.mpRightSide);
        mpCenterBtn = (LinearLayout) findViewById(R.id.mpCenterBtn);
        mpLeftImage = (ImageView) findViewById(R.id.mpLeftImage);
        mpRightImage = (ImageView) findViewById(R.id.mpRightImage);
        mpCenterImage = (ImageView) findViewById(R.id.mpCenterImage);
        mpLeftText = (TextView) findViewById(R.id.mpLeftText);
        mpRightText = (TextView) findViewById(R.id.mpRightText);
        mpCenterText = (TextView) findViewById(R.id.mpCenterText);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpTripleSwitcher);
        mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
        mpLeftImage.setImageResource(R.drawable.ellipse);
        mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
        mpRightImage.setImageResource(R.drawable.ellipse_not_active);
        mpCenterImage.setImageResource(R.drawable.ellipse_not_active);
        mpCenterBtn.setBackgroundResource(R.drawable.unpressed_rect);
        mpCenterText.setText(attributeArray.getText(R.styleable.MpTripleSwitcher_center_text));
        mpLeftText.setText(attributeArray.getText(R.styleable.MpTripleSwitcher_left_text));
        mpRightText.setText(attributeArray.getText(R.styleable.MpTripleSwitcher_right_text));

        mpLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateManager.startVibrate(context, 50);
                mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_pressed_bg);
                mpLeftImage.setImageResource(R.drawable.ellipse);
                mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
                mpRightImage.setImageResource(R.drawable.ellipse_not_active);
                mpCenterImage.setImageResource(R.drawable.ellipse_not_active);
                mpCenterBtn.setBackgroundResource(R.drawable.unpressed_rect);
                right = false;
                center = false;
                left = true;
            }
        });

        mpRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateManager.startVibrate(context, 50);
                mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_bg);
                mpLeftImage.setImageResource(R.drawable.ellipse_not_active);
                mpRightBtn.setBackgroundResource(R.drawable.matrix_right_pressed_bg);
                mpRightImage.setImageResource(R.drawable.ellipse);
                mpCenterImage.setImageResource(R.drawable.ellipse_not_active);
                mpCenterBtn.setBackgroundResource(R.drawable.unpressed_rect);
                right = true;
                center = false;
                left = false;
            }
        });

        mpCenterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                VibrateManager.startVibrate(context, 50);
                mpLeftBtn.setBackgroundResource(R.drawable.matrix_left_bg);
                mpLeftImage.setImageResource(R.drawable.ellipse_not_active);
                mpRightBtn.setBackgroundResource(R.drawable.matrix_right_bg);
                mpRightImage.setImageResource(R.drawable.ellipse_not_active);
                mpCenterImage.setImageResource(R.drawable.ellipse);
                mpCenterBtn.setBackgroundResource(R.drawable.pressed_rect);
                right = false;
                center = true;
                left = false;
            }
        });

        attributeArray.recycle();
    }

    public boolean isRight() {
        return right;
    }

    public boolean isCenter() {
        return center;
    }

    public boolean isLeft() {
        return left;
    }

    public void setText(String left, String right, String center) {
        mpLeftText.setText(left);
        mpRightText.setText(right);
        mpCenterText.setText(center);
    }


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.right = savedState.boolValueFirst;
        this.left = savedState.boolValueSecond;
        this.center = savedState.boolValueThird;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValueFirst = this.right;
        savedState.boolValueSecond = this.left;
        savedState.boolValueThird = this.center;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        boolean boolValueFirst;
        boolean boolValueSecond;
        boolean boolValueThird;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValueFirst = in.readInt() != 0;
            this.boolValueSecond = in.readInt() != 0;
            this.boolValueThird = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValueFirst ? 1 : 0);
            out.writeInt(boolValueSecond ? 1 : 0);
            out.writeInt(boolValueThird ? 1 : 0);
            super.writeToParcel(out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
