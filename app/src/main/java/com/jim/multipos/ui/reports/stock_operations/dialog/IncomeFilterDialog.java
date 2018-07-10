package com.jim.multipos.ui.reports.stock_operations.dialog;

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

public class IncomeFilterDialog extends Dialog {

    @BindView(R.id.chbInvoiceProduct)
    MpCheckbox chbInvoiceProduct;
    @BindView(R.id.chbSurplusProduct)
    MpCheckbox chbSurplusProduct;
    @BindView(R.id.chbReturnProduct)
    MpCheckbox chbReturnProduct;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private OrderHistoryFilterListner orderHistoryFilterListner;

    public interface OrderHistoryFilterListner{
        void onApply(int[] config);
    }
    public IncomeFilterDialog(@NonNull Context context, int[] filterConfig, OrderHistoryFilterListner orderHistoryFilterListner) {
        super(context);
        this.orderHistoryFilterListner = orderHistoryFilterListner;
        View dialogView = getLayoutInflater().inflate(R.layout.income_filter_dialog, null);
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

        if(filterConfig[0] == 1 ) chbInvoiceProduct.setChecked(true);
        else chbInvoiceProduct.setChecked(false);
        if(filterConfig[1] == 1 ) chbSurplusProduct.setChecked(true);
        else chbSurplusProduct.setChecked(false);
        if(filterConfig[2] == 1 ) chbReturnProduct.setChecked(true);
        else chbReturnProduct.setChecked(false);


        chbInvoiceProduct.setTextColor(Color.parseColor("#212121"));
        chbSurplusProduct.setTextColor(Color.parseColor("#212121"));
        chbReturnProduct.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            orderHistoryFilterListner.onApply(new int[]{chbInvoiceProduct.isChecked()?1:0,chbSurplusProduct.isChecked()?1:0,chbReturnProduct.isChecked()?1:0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
