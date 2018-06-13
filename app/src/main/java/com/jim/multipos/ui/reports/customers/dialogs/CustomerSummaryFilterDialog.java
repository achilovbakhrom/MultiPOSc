package com.jim.multipos.ui.reports.customers.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class CustomerSummaryFilterDialog extends Dialog {

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    @BindView(R.id.radioBtnCustomer)
    RadioButton radioBtnCustomer;
    @BindView(R.id.radioBtnCustomerGroup)
    RadioButton radioBtnCustomerGroup;
    private CustomerSummaryListener listener;

    public interface CustomerSummaryListener {
        void onApply(int config);
    }

    public CustomerSummaryFilterDialog(@NonNull Context context, int filterConfig, CustomerSummaryListener listener) {
        super(context);
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.customer_summary_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(190);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(240);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        if (filterConfig == 0)
            radioBtnCustomer.setChecked(true);
        else radioBtnCustomerGroup.setChecked(true);
        btnApply.setOnClickListener((view -> {
            listener.onApply(radioBtnCustomer.isChecked() ? 0 : 1);
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
