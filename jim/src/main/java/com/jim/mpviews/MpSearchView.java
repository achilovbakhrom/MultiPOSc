package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by DEV on 30.06.2017.
 */

public class MpSearchView extends RelativeLayout {
    private EditText mpSearchEditText;
    private boolean visibility_status = true;

    public MpSearchView(Context context) {
        super(context);
        init(context, null);
    }

    public MpSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.mp_search_view, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.edit_text_bg);
        TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.MpSearchView);
        ImageView mpBarcodeImage = (ImageView) findViewById(R.id.mpBarcodeImage);
        mpSearchEditText = (EditText) findViewById(R.id.mpSearchEditText);
        mpSearchEditText.setLines(1);
        mpSearchEditText.setMaxLines(1);
        mpSearchEditText.setSingleLine();
        visibility_status = attributeArray.getBoolean(R.styleable.MpSearchView_barcode_visibility, true);
        if (visibility_status) {
            mpBarcodeImage.setVisibility(VISIBLE);
        } else {
            mpBarcodeImage.setVisibility(GONE);
        }

        attributeArray.recycle();
    }

    public EditText getSearchView() {
        return mpSearchEditText;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.visibility_status = savedState.boolValue;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValue = this.visibility_status;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
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
