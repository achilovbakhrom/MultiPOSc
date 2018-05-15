package com.jim.mpviews;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Пользователь on 26.05.2017.
 */

public class MpVendorItem extends RelativeLayout {

    private TextView mpVendor, mpItem, mpName;
    private LinearLayout llVendor;
    private boolean isPressed = false;

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
        LayoutInflater.from(context).inflate(R.layout.mp_vendor_item, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpVendor = (TextView) findViewById(R.id.tvVendor);
        mpItem = (TextView) findViewById(R.id.tvItemQty);
        mpName = (TextView) findViewById(R.id.tvVendorName);
        llVendor = (LinearLayout) findViewById(R.id.llVendor);

        mpVendor.setTextColor(context.getResources().getColorStateList(R.color.item_txt_color));
        mpItem.setTextColor(context.getResources().getColorStateList(R.color.item_txt_second_color));
        mpName.setTextColor(context.getResources().getColorStateList(R.color.item_txt_second_color));


    }

    public void setVendor(String vendor) {
        mpVendor.setText(vendor);
    }
    public TextView getVendorTv(){
        return mpVendor;
    }
    public TextView getVendorItemTv(){
        return mpItem;
    }
    public TextView getVendorNameTv(){
        return mpName;
    }
    public void setVendorItem(String item) {
        mpItem.setText( item);
    }

    public void setVendorName(String name) {
        mpName.setText(name);
    }
    public void setOnClickItemCustom(OnClickListener onClickItemCustom){
        findViewById(R.id.llVendor).setOnClickListener(onClickItemCustom);
    }
    public void setVisibility(int vendor, int item, int name)
    {
        mpVendor.setVisibility(vendor);
        mpItem.setVisibility(item);
        mpName.setVisibility(name);
    }


}
