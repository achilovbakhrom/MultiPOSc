package com.jim.mpviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 17.10.17.
 */

public class MpCompletedStateView extends AppCompatImageView {
    public static final int EMPTY_STATE = 0;
    public static final int COMPLETED_STATE = 1;
    public static final int WARNING_STATE = 2;

    private int state;

    public MpCompletedStateView(Context context) {
        super(context);
    }

    public MpCompletedStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MpCompletedStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(int state) {
        switch (state) {
            case EMPTY_STATE:
                setImageResource(R.drawable.empty_checkbox);
                break;
            case COMPLETED_STATE:
                setImageResource(R.drawable.checked_checkbox);
                break;
            case WARNING_STATE:
                setImageResource(R.drawable.warning_checkbox);
                break;
        }

        this.state = state;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        MpCompletedStateView.SavedState savedState = new MpCompletedStateView.SavedState(superState);

        savedState.state = this.state;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpCompletedStateView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        MpCompletedStateView.SavedState savedState = (MpCompletedStateView.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        //end

        this.state = savedState.state;
    }

    static class SavedState extends View.BaseSavedState {
        int state;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.state = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(state);
            super.writeToParcel(out, flags);
        }

        public static final Parcelable.Creator<MpCompletedStateView.SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new MpCompletedStateView.SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new MpCompletedStateView.SavedState[size];
            }
        };
    }
}
