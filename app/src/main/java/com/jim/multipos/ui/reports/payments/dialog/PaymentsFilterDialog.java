package com.jim.multipos.ui.reports.payments.dialog;

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

public class PaymentsFilterDialog extends Dialog {

    @BindView(R.id.chbPaymentToOrder)
    MpCheckbox chbPaymentToOrder;
    @BindView(R.id.chbChange)
    MpCheckbox chbChange;
    @BindView(R.id.chbPayIn)
    MpCheckbox chbPayIn;
    @BindView(R.id.chbPayOut)
    MpCheckbox chbPayOut;
    @BindView(R.id.chbPayToVendor)
    MpCheckbox chbPayToVendor;
    @BindView(R.id.chbBankDrop)
    MpCheckbox chbBankDrop;
    @BindView(R.id.chbDebtIn)
    MpCheckbox chbDebtIn;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private PaymentsFilterListner orderHistoryFilterListner;

    public interface PaymentsFilterListner{
        void onApply(int[] config);
    }
    public PaymentsFilterDialog(@NonNull Context context, int[] filterConfig, PaymentsFilterListner orderHistoryFilterListner) {
        super(context);
        this.orderHistoryFilterListner = orderHistoryFilterListner;
        View dialogView = getLayoutInflater().inflate(R.layout.payments_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(160);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(440);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        if(filterConfig[0] == 1 ) chbPaymentToOrder.setChecked(true);
        else chbPaymentToOrder.setChecked(false);
        if(filterConfig[1] == 1 ) chbChange.setChecked(true);
        else chbChange.setChecked(false);
        if(filterConfig[2] == 1 ) chbPayIn.setChecked(true);
        else chbPayIn.setChecked(false);
        if(filterConfig[3] == 1 ) chbPayOut.setChecked(true);
        else chbPayOut.setChecked(false);
        if(filterConfig[4] == 1 ) chbPayToVendor.setChecked(true);
        else chbPayToVendor.setChecked(false);
        if(filterConfig[5] == 1 ) chbBankDrop.setChecked(true);
        else chbBankDrop.setChecked(false);
        if(filterConfig[6] == 1 ) chbDebtIn.setChecked(true);
        else chbDebtIn.setChecked(false);

        chbPaymentToOrder.setTextColor(Color.parseColor("#212121"));
        chbChange.setTextColor(Color.parseColor("#212121"));
        chbPayIn.setTextColor(Color.parseColor("#212121"));
        chbPayOut.setTextColor(Color.parseColor("#212121"));
        chbPayToVendor.setTextColor(Color.parseColor("#212121"));
        chbBankDrop.setTextColor(Color.parseColor("#212121"));
        chbDebtIn.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            orderHistoryFilterListner.onApply(new int[]{
                    chbPaymentToOrder.isChecked()?1:0,
                    chbChange.isChecked()?1:0,
                    chbPayIn.isChecked()?1:0,
                    chbPayOut.isChecked()?1:0,
                    chbPayToVendor.isChecked()?1:0,
                    chbBankDrop.isChecked()?1:0,
                    chbDebtIn.isChecked()?1:0,
            });
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
