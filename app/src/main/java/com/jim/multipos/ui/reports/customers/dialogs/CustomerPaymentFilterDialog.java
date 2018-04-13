package com.jim.multipos.ui.reports.customers.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class CustomerPaymentFilterDialog extends Dialog {

    @BindView(R.id.chbClosedOrders)
    MpCheckbox chbClosedOrders;
    @BindView(R.id.chbHeldOrders)
    MpCheckbox chbHeldOrders;
    @BindView(R.id.chbCanceled)
    MpCheckbox chbCanceled;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private CustomerPaymentFilterListener customerPaymentFilterListener;

    public interface CustomerPaymentFilterListener{
        void onApply(int[] config);
    }
    public CustomerPaymentFilterDialog(@NonNull Context context, int[] filterConfig, CustomerPaymentFilterListener customerPaymentFilterListener) {
        super(context);
        this.customerPaymentFilterListener = customerPaymentFilterListener;
        View dialogView = getLayoutInflater().inflate(R.layout.customer_payment_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(160);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(260);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        if(filterConfig[0] == 1 ) chbClosedOrders.setChecked(true);
        else chbClosedOrders.setChecked(false);
        if(filterConfig[1] == 1 ) chbHeldOrders.setChecked(true);
        else chbHeldOrders.setChecked(false);
        if(filterConfig[2] == 1 ) chbCanceled.setChecked(true);
        else chbCanceled.setChecked(false);


        chbClosedOrders.setTextColor(Color.parseColor("#212121"));
        chbHeldOrders.setTextColor(Color.parseColor("#212121"));
        chbCanceled.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            customerPaymentFilterListener.onApply(new int[]{chbClosedOrders.isChecked()?1:0,chbHeldOrders.isChecked()?1:0,chbCanceled.isChecked()?1:0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
