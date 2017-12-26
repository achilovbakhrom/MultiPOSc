package com.jim.mpviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Пользователь on 22.06.2017.
 */

public class MpHorizontalScroller extends LinearLayout {

    private TextView mpCenterText, mpCounter;
    private ImageView mpCenter;
    private List<String> arrayList;
    int counter = 0;
    private ImageView mpLeftArrow, mpRightArrow;
    boolean pressed = false;

    public MpHorizontalScroller(Context context) {
        super(context);
        init(context);
    }

    public MpHorizontalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpHorizontalScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MpHorizontalScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.mp_horizontal_scroller, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        arrayList = new ArrayList<>();
        mpCenterText = (TextView) findViewById(R.id.mpCenterText);
        mpCounter = (TextView) findViewById(R.id.mpCounter);
        mpCounter.setText("0000");
        mpCenter = (ImageView) findViewById(R.id.mpCenter);
        mpLeftArrow = (ImageView) findViewById(R.id.mpLeftArrow);
        mpRightArrow = (ImageView) findViewById(R.id.mpRightArrow);
        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.right_arrow_selector);
        buttonDrawable.mutate();
        mpRightArrow.setImageDrawable(buttonDrawable);
        Drawable drawable = context.getResources().getDrawable(R.drawable.left_arrow_selector);
        buttonDrawable.mutate();
        mpLeftArrow.setImageDrawable(drawable);
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.center_btn_selector);
        buttonDrawable.mutate();
        mpCenter.setImageDrawable(drawable1);


//        mpLeftArrow.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (counter > 0) {
//                    counter--;
//                } else {
//                    counter = 0;
//                }
//                if (!arrayList.isEmpty())
//                    mpCounter.setText(arrayList.get(counter));
//            }
//        });
//        mpRightArrow.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (counter < arrayList.size() - 1) {
//                    counter++;
//                } else {
//                    counter = arrayList.size() - 1;
//                }
//                if (!arrayList.isEmpty())
//                    mpCounter.setText(arrayList.get(counter));
//            }
//        });
//        mpCenter.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void setItems(ArrayList<String> items) {
        arrayList = items;
        mpCounter.setText(arrayList.get(0));
        invalidate();
    }

    public void setItems(String[] items) {
        arrayList.addAll(Arrays.asList(items));
        mpCounter.setText(arrayList.get(0));
        invalidate();
    }


    public void setOnItemClickListener(OnClickListener onItemClickListener) {
        mpCenter.setOnClickListener(onItemClickListener);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.arrayList = savedState.list;
        this.pressed = savedState.boolValue;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.list = this.arrayList;
        savedState.boolValue = this.pressed;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        List<String> list;
        boolean boolValue;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            in.readStringList(this.list);
            this.boolValue = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeStringList(this.list);
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
