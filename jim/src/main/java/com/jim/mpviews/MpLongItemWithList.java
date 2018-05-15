package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.Utils;


/**
 * Created by Пользователь on 26.05.2017.
 */

public class MpLongItemWithList extends RelativeLayout {

    private TextView firstItem, secondItem, thirdItem;
    private LinearLayout mainContainer;
    private boolean isPressed = false;

    public MpLongItemWithList(Context context) {
        super(context);
        init(context, null);
    }

    public MpLongItemWithList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpLongItemWithList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MpLongItemWithList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_long_item_with_list, this);
        firstItem = (TextView) findViewById(R.id.tvFirstItem);
        secondItem = (TextView) findViewById(R.id.tvSecondItem);
        thirdItem = (TextView) findViewById(R.id.tvThirdItem);
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MpLongItemWithList);
            firstItem.setTextSize(attributeArray.getDimensionPixelSize(R.styleable.MpLongItemWithList_long_item_text_size, Utils.dpToPx(12)));
            secondItem.setTextSize(attributeArray.getDimensionPixelSize(R.styleable.MpLongItemWithList_long_item_text_size, Utils.dpToPx(12)));
            thirdItem.setTextSize(attributeArray.getDimensionPixelSize(R.styleable.MpLongItemWithList_long_item_text_size, Utils.dpToPx(12)));
            firstItem.setText(attributeArray.getString(R.styleable.MpLongItemWithList_long_item_first_text));
            secondItem.setText(attributeArray.getString(R.styleable.MpLongItemWithList_long_item_second_text));
            thirdItem.setText(attributeArray.getString(R.styleable.MpLongItemWithList_long_item_third_text));
            isPressed = attributeArray.getBoolean(R.styleable.MPListItemView_list_item_active, false);
            attributeArray.recycle();
        }
        mainContainer = (LinearLayout) findViewById(R.id.llVendor);
        mainContainer.setBackgroundResource(R.drawable.vendor_item_bg);
        if (isPressed) {
            mainContainer.setBackgroundResource(R.drawable.vendor_pressed_item_bg);
            firstItem.setTextColor(getResources().getColor(R.color.colorWhite));
            secondItem.setTextColor(getResources().getColor(R.color.colorWhite));
            thirdItem.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            mainContainer.setBackgroundResource(R.drawable.vendor_item_bg);
            if(!isAddButton)
                firstItem.setTextColor(getResources().getColor(R.color.colorMainText));
            else
                firstItem.setTextColor(getResources().getColor(R.color.colorGreenSecond));
            secondItem.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            thirdItem.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }

    }

    public void setFirstItemText(String firstItemText) {
        firstItem.setText(firstItemText);
    }

    public void setSecondItemText(String secondItemText) {
        secondItem.setText(secondItemText);
    }

    public void setThirdItemText(String thirdItemText) {
        thirdItem.setText(thirdItemText);
    }

    public void setActivate(boolean isPressed) {
        if (isPressed) {
            mainContainer.setBackgroundResource(R.drawable.vendor_pressed_item_bg);
            firstItem.setTextColor(getResources().getColor(R.color.colorWhite));
            secondItem.setTextColor(getResources().getColor(R.color.colorWhite));
            thirdItem.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            mainContainer.setBackgroundResource(R.drawable.vendor_item_bg);
            if(!isAddButton)
                firstItem.setTextColor(getResources().getColor(R.color.colorMainText));
            else
                firstItem.setTextColor(getResources().getColor(R.color.colorGreenSecond));
            secondItem.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            thirdItem.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
        this.isPressed = isPressed;
    }

    public void setVisibility(int first, int second, int third) {
        firstItem.setVisibility(first);
        secondItem.setVisibility(second);
        thirdItem.setVisibility(third);
    }

    public void setTextSize(int size) {
        firstItem.setTextSize(size);
        secondItem.setTextSize(size);
        thirdItem.setTextSize(size);
    }

    public void makeDeletable(boolean isDeletable) {
        if (isDeletable) {
            setAlpha(0.3f);
        } else {
            setAlpha(1.0f);
        }
    }

    public void setTextColor(int color) {
        firstItem.setTextColor(getResources().getColor(color));
        secondItem.setTextColor(getResources().getColor(color));
        thirdItem.setTextColor(getResources().getColor(color));
    }


    boolean isAddButton = false;
    public void itIsAddButton(boolean isPressed){
        isAddButton = isPressed;
    }


}
