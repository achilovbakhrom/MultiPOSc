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
        mpLeftBtn = (LinearLayout) findViewById(R.id.mpLeftSide);
        mpRightBtn = (LinearLayout) findViewById(R.id.mpRightSide);
        mpLeftImage = (ImageView) findViewById(R.id.mpLeftImage);
        mpRightImage = (ImageView) findViewById(R.id.mpRightImage);
        mpLeftText = (TextView) findViewById(R.id.mpLeftText);
        mpRightText = (TextView) findViewById(R.id.mpRightText);
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

    public interface OnSwitcherStateChangedListener{
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
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public LinearLayout getMpLeftBtn(){
        return mpLeftBtn;
    }

    public LinearLayout getMpRightBtn(){
        return mpRightBtn;
    }

    public void setText(String left, String right) {
        mpLeftText.setText(left);
        mpRightText.setText(right);
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
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValueFirst = this.right;
        savedState.boolValueSecond = this.left;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        boolean boolValueFirst;
        boolean boolValueSecond;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValueFirst = in.readInt() != 0;
            this.boolValueSecond = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValueFirst ? 1 : 0);
            out.writeInt(boolValueSecond ? 1 : 0);
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
