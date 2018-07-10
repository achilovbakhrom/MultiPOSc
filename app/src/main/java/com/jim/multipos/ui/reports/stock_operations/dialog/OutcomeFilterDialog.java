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

public class OutcomeFilterDialog extends Dialog {

    @BindView(R.id.chbOrderSales)
    MpCheckbox chbOrderSales;
    @BindView(R.id.chbOutvoice)
    MpCheckbox chbOutvoice;
    @BindView(R.id.chbWaste)
    MpCheckbox chbWaste;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private OrderHistoryFilterListner orderHistoryFilterListner;

    public interface OrderHistoryFilterListner{
        void onApply(int[] config);
    }
    public OutcomeFilterDialog(@NonNull Context context, int[] filterConfig, OrderHistoryFilterListner orderHistoryFilterListner) {
        super(context);
        this.orderHistoryFilterListner = orderHistoryFilterListner;
        View dialogView = getLayoutInflater().inflate(R.layout.outcome_filter_dialog, null);
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

        if(filterConfig[0] == 1 ) chbOrderSales.setChecked(true);
        else chbOrderSales.setChecked(false);
        if(filterConfig[1] == 1 ) chbOutvoice.setChecked(true);
        else chbOutvoice.setChecked(false);
        if(filterConfig[2] == 1 ) chbWaste.setChecked(true);
        else chbWaste.setChecked(false);


        chbOrderSales.setTextColor(Color.parseColor("#212121"));
        chbOutvoice.setTextColor(Color.parseColor("#212121"));
        chbWaste.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            orderHistoryFilterListner.onApply(new int[]{chbOrderSales.isChecked()?1:0,chbOutvoice.isChecked()?1:0,chbWaste.isChecked()?1:0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
