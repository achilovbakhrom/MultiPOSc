package com.jim.mpviews;

import android.content.Context;
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
 * Created by Пользователь on 26.05.2017.
 */

public class MpVendorItem extends RelativeLayout {

    private TextView mpVendor, mpItem, mpName;
    private LinearLayout llVendor;
    private boolean isPressed = false;
    private VibrateManager VibrateManager;

    public MpVendorItem(Context context) {
        super(context);
        init(context);
    }

    public MpVendorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpVendorItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpVendorItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        VibrateManager = new VibrateManager(getContext());
        LayoutInflater.from(context).inflate(R.layout.mp_vendor_item, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpVendor = (TextView) findViewById(R.id.tvVendor);
        mpItem = (TextView) findViewById(R.id.tvItemQty);
        mpName = (TextView) findViewById(R.id.tvVendorName);
        llVendor = (LinearLayout) findViewById(R.id.llVendor);
        llVendor.setBackgroundResource(R.drawable.vendor_item_bg);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate();
                        if (!isPressed) {
                            llVendor.setBackgroundResource(R.drawable.pressed_vendor_item);
                            isPressed = true;
                        } else {
                            llVendor.setBackgroundResource(R.drawable.vendor_item_bg);
                            isPressed = false;
                        }
                        break;
                }
                return false;
            }
        });

    }

    public void setVendor(String vendor) {
        mpVendor.setText(vendor);
    }

    public void setVendorItem(String item) {
        mpItem.setText("Item: " + item);
    }

    public void setVendorName(String name) {
        mpName.setText(name);
    }

    public void setVisibility(int vendor, int item, int name)
    {
        mpVendor.setVisibility(vendor);
        mpItem.setVisibility(item);
        mpName.setVisibility(name);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        //end

        this.isPressed = savedState.boolValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);

        savedState.boolValue = this.isPressed;

        return savedState;

    }

    static class SavedState extends BaseSavedState{
        boolean boolValue;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValue = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValue ? 1 : 0);
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
