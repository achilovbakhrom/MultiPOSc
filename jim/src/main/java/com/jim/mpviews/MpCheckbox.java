package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;

/**
 * Created by Пользователь on 23.05.2017.
 */

public class MpCheckbox extends LinearLayout {

    public static final int NO_TEXT = 0, LEFT_TEXT = 1, RIGHT_TEXT = 2, BOTTOM_TEXT = 3, TOP_TEXT = 4;

    private int type = NO_TEXT;
    private boolean checked = false;
    private String text = null;
    private CheckedChangeListener listener;
    private Context context;

    public MpCheckbox(Context context) {
        super(context);
        init(context, null);
    }

    public MpCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpCheckbox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void init(final Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_checkbox, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpCheckbox);

        text = attributeArray.getString(R.styleable.MpCheckbox_text);
        checked = attributeArray.getBoolean(R.styleable.MpCheckbox_checked, false);
        type = attributeArray.getInteger(R.styleable.MpCheckbox_chb_type, NO_TEXT);
        changeType(context, type);
        setText(text);
        setChecked(checked);
        attributeArray.recycle();

        setOnClickListener(v -> {
            checked = !checked;
            setChecked(checked);
        });
    }



    public void setType(int type) {
        changeType(getContext(), type);
        setChecked(checked);
        setText(text);
        this.type = type;
    }

    private void changeType(Context context, int type) {
        removeAllViews();
        switch (type) {
            case NO_TEXT:
                setOrientation(VERTICAL);
                addImageView(context, 4, 4, 4, 4, Gravity.CENTER);
                break;
            case LEFT_TEXT:
                setOrientation(HORIZONTAL);
                addTextView(context, 4, 2, 4 , 2, Gravity.CENTER_VERTICAL);
                addImageView(context, 2, 4, 4, 4, Gravity.CENTER_VERTICAL);
                break;
            case RIGHT_TEXT:
                setOrientation(HORIZONTAL);
                addImageView(context, 1, 4, 2, 4, Gravity.CENTER_VERTICAL);
                addTextView(context, 12, 2, 4 , 2, Gravity.CENTER_VERTICAL);
                break;
            case TOP_TEXT:
                setOrientation(VERTICAL);
                addTextView(context, 4, 4, 4, 2, Gravity.CENTER_HORIZONTAL);
                addImageView(context, 4, 2, 4, 4, Gravity.CENTER_HORIZONTAL);
                break;
            case BOTTOM_TEXT:
                setOrientation(VERTICAL);
                addImageView(context, 4, 4, 4, 2, Gravity.CENTER_HORIZONTAL);
                addTextView(context, 4, 2, 4, 4, Gravity.CENTER_HORIZONTAL);
                break;
        }

    }

    public void setCheckboxColor(int resId){
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageView){
                ImageView imageView = (ImageView) getChildAt(i);
                imageView.setImageTintList(ContextCompat.getColorStateList(context, resId));
            }
        }
    }

    private void addTextView(Context context, int left, int top, int right, int bottom, int gravity) {
        TextView textView = new TextView(context);
        LayoutParams tvLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.leftMargin = Utils.dpToPx(left);
        tvLp.rightMargin = Utils.dpToPx(right);
        tvLp.topMargin = Utils.dpToPx(top);
        tvLp.bottomMargin = Utils.dpToPx(bottom);
        tvLp.gravity = gravity;
        textView.setLayoutParams(tvLp);
        textView.setId(R.id.chb_tv);
        addView(textView);
    }

    private void addImageView(Context context, int left, int top, int right, int bottom, int gravity) {
        ImageView imageView = new ImageView(context);
        int size = Utils.dpToPx(20);
        LayoutParams ivLp = new LayoutParams(size, size);
        ivLp.gravity = gravity;
        ivLp.topMargin = Utils.dpToPx(top);
        ivLp.bottomMargin = Utils.dpToPx(bottom);
        ivLp.leftMargin = Utils.dpToPx(left);
        ivLp.rightMargin = Utils.dpToPx(right);
        imageView.setLayoutParams(ivLp);
        imageView.setId(R.id.chb_iv);
        addView(imageView);
    }

    public void setText(String text) {
        TextView textView = (TextView) findViewById(R.id.chb_tv);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setChecked(boolean checked) {
        ImageView imageView = ((ImageView) findViewById(R.id.chb_iv));
        if (imageView != null) {
            int imageId = checked ? R.drawable.checked_blue : R.drawable.unchecked;
            imageView.setImageResource(imageId);
        }
        this.checked = checked;
        if (listener != null)
            listener.onCheckedChange(this.checked);
    }

    public void setCheckedChangeListener(CheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setTextSize(int size) {
        TextView textView = (TextView) findViewById(R.id.chb_tv);
        if (textView != null) {
            textView.setTextSize(size);
        }
    }

    public void setTextColor(int color) {
        TextView textView = (TextView) findViewById(R.id.chb_tv);
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public interface CheckedChangeListener {
        void onCheckedChange(boolean isChecked);
    }

}
