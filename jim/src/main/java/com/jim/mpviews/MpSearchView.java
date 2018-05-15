package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jim.mpviews.utils.Utils;

/**
 * Created by DEV on 30.06.2017.
 */

public class MpSearchView extends RelativeLayout {
    private MpEditText mpSearchEditText;
    private boolean visibility_status = true;
    private ImageView mpBarcodeImage;

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
        mpBarcodeImage = (ImageView) findViewById(R.id.mpBarcodeImage);
        mpSearchEditText = (MpEditText) findViewById(R.id.mpSearchEditText);
        mpSearchEditText.setLines(1);
        mpSearchEditText.setMaxLines(1);
        mpSearchEditText.setSingleLine();
        mpSearchEditText.setTextColor(ContextCompat.getColor(context, R.color.colorMainText));
        mpSearchEditText.setHintTextColor(ContextCompat.getColor(context, R.color.colorTextHint));
        mpSearchEditText.setHint(attributeArray.getString(R.styleable.MpSearchView_hint_text));
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

    public ImageView getBarcodeView() {
        return mpBarcodeImage;
    }


}
