package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        relativeLayout.setBackgroundResource(R.drawable.light_button);
        pressed = false;
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.performClick();
                    if (!pressed) {
                        VibrateManager.startVibrate(context, 50);
                        pressed = true;
                    }
                    relativeLayout.setBackgroundResource(R.drawable.light_button_pressed);
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    relativeLayout.setBackgroundResource(R.drawable.light_button);
                    return false;
            }
            return false;
        });
        attributeArray.recycle();
    }

    private String key = null;

    public void setState(String key) {
        boolean state = StateSaver.getInstance(getContext()).getStateSaver().getBoolean(key, false);
        this.key = key;
        if (state) {
            setBackgroundResource(R.drawable.pressed_btn);
            isPressed = true;
        } else {
            setBackgroundResource(R.drawable.button_bg);
            isPressed = false;
        }
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

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.isPressed = savedState.boolValue;
        this.mode = savedState.intValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValue = this.isPressed;
        savedState.intValue = this.mode;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        boolean boolValue;
        int intValue;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValue = in.readInt() != 0;
            this.intValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValue ? 1 : 0);
            out.writeInt(this.intValue);
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
