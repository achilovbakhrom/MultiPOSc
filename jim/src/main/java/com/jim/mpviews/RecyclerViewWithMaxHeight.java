package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jim.mpviews.utils.Utils;

/**
 * Created by DEV on 15.09.2017.
 */

public class RecyclerViewWithMaxHeight extends RecyclerView {

    private int maxHeight = 350;

    public RecyclerViewWithMaxHeight(Context context) {
        super(context);
    }

    public RecyclerViewWithMaxHeight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewWithMaxHeight(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(Utils.dpToPx(maxHeight), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        MpNumPad.SavedState savedState = (MpNumPad.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.maxHeight = savedState.intValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        MpNumPad.SavedState savedState = new MpNumPad.SavedState(superState);
        savedState.intValue = this.maxHeight;
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
            this.intValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
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
