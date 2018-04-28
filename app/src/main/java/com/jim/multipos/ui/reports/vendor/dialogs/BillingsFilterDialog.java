package com.jim.multipos.ui.reports.vendor.dialogs;

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

public class BillingsFilterDialog extends Dialog {

    @BindView(R.id.chbPay)
    MpCheckbox chbPay;
    @BindView(R.id.chbReturn)
    MpCheckbox chbReturn;
    @BindView(R.id.chbDebt)
    MpCheckbox chbDebt;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private BillingsFilterListener billingsFilterListener;

    public interface BillingsFilterListener {
        void onApply(int[] config);
    }

    public BillingsFilterDialog(@NonNull Context context, int[] filterConfig, BillingsFilterListener billingsFilterListener) {
        super(context);
        this.billingsFilterListener = billingsFilterListener;
        View dialogView = getLayoutInflater().inflate(R.layout.billings_filter_dialog, null);
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

        if (filterConfig[0] == 1) chbPay.setChecked(true);
        else chbPay.setChecked(false);
        if (filterConfig[1] == 1) chbReturn.setChecked(true);
        else chbReturn.setChecked(false);
        if (filterConfig[2] == 1) chbDebt.setChecked(true);
        else chbDebt.setChecked(false);


        chbPay.setTextColor(Color.parseColor("#212121"));
        chbReturn.setTextColor(Color.parseColor("#212121"));
        chbDebt.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            this.billingsFilterListener.onApply(new int[]{chbPay.isChecked() ? 1 : 0, chbReturn.isChecked() ? 1 : 0, chbDebt.isChecked() ? 1 : 0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
