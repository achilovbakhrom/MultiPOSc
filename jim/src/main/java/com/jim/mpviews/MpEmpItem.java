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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jim.mpviews.utils.VibrateManager;


/**
 * Created by Пользователь on 26.05.2017.
 */

public class MpEmpItem extends RelativeLayout {

    private TextView mpName, mpRole, mpDepartment;
    private ImageView mpPhoto;
    private LinearLayout mpEmpBackground;
    private boolean isPressed = false;

    public MpEmpItem(Context context) {
        super(context);
        init(context);
    }

    public MpEmpItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpEmpItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpEmpItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.mp_emp_item, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpRole = (TextView) findViewById(R.id.tvRole);
        mpDepartment = (TextView) findViewById(R.id.tvDepartment);
        mpName = (TextView) findViewById(R.id.tvEmpName);
        mpEmpBackground = (LinearLayout) findViewById(R.id.llEmp);
        mpPhoto = (ImageView) findViewById(R.id.ivEmpPhoto);
        mpEmpBackground.setBackgroundResource(R.drawable.vendor_item_bg);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate(context, 50);
                        if (!isPressed) {
                            mpEmpBackground.setBackgroundResource(R.drawable.pressed_vendor_item);
                            isPressed = true;
                        } else {
                            mpEmpBackground.setBackgroundResource(R.drawable.vendor_item_bg);
                            isPressed = false;
                        }
                        break;
                }
                return false;
            }
        });

    }

    public void setRole(String role) {
        mpRole.setText(role);
    }

    public void setDepartment(String department) {
        mpDepartment.setText(department);
    }

    public void setEmployerName(String name) {
        mpName.setText(name);
    }

    public void setPhoto(int resID){ mpPhoto.setImageResource(resID);}

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

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
