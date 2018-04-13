package com.jim.multipos.ui.reports.inventory.dialogs;

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

public class InventoryFilterDialog extends Dialog {

    @BindView(R.id.chbCanceledOrder)
    MpCheckbox chbCanceledOrder;
    @BindView(R.id.chbConsignmentCanceled)
    MpCheckbox chbConsignmentCanceled;
    @BindView(R.id.chbIncomeFromVendor)
    MpCheckbox chbIncomeFromVendor;
    @BindView(R.id.chbReturnHold)
    MpCheckbox chbReturnHold;
    @BindView(R.id.chbReturnFromCustomer)
    MpCheckbox chbReturnFromCustomer;
    @BindView(R.id.chbReturnToVendor)
    MpCheckbox chbReturnToVendor;
    @BindView(R.id.chbSold)
    MpCheckbox chbSold;
    @BindView(R.id.chbVoidIncome)
    MpCheckbox chbVoidIncome;
    @BindView(R.id.chbWaste)
    MpCheckbox chbWaste;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private InventoryFilterListener listener;

    public interface InventoryFilterListener {
        void onApply(int[] config);
    }

    public InventoryFilterDialog(@NonNull Context context, int[] filterConfig, InventoryFilterListener listener) {
        super(context);
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.inventory_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(190);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(500);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        if (filterConfig[0] == 1) chbCanceledOrder.setChecked(true);
        else chbCanceledOrder.setChecked(false);
        if (filterConfig[1] == 1) chbConsignmentCanceled.setChecked(true);
        else chbConsignmentCanceled.setChecked(false);
        if (filterConfig[2] == 1) chbIncomeFromVendor.setChecked(true);
        else chbIncomeFromVendor.setChecked(false);
        if (filterConfig[3] == 1) chbReturnHold.setChecked(true);
        else chbReturnHold.setChecked(false);
        if (filterConfig[4] == 1) chbReturnFromCustomer.setChecked(true);
        else chbReturnFromCustomer.setChecked(false);
        if (filterConfig[5] == 1) chbReturnToVendor.setChecked(true);
        else chbReturnToVendor.setChecked(false);
        if (filterConfig[6] == 1) chbSold.setChecked(true);
        else chbSold.setChecked(false);
        if (filterConfig[7] == 1) chbVoidIncome.setChecked(true);
        else chbVoidIncome.setChecked(false);
        if (filterConfig[8] == 1) chbWaste.setChecked(true);
        else chbWaste.setChecked(false);


        chbCanceledOrder.setTextColor(Color.parseColor("#212121"));
        chbConsignmentCanceled.setTextColor(Color.parseColor("#212121"));
        chbIncomeFromVendor.setTextColor(Color.parseColor("#212121"));
        chbReturnHold.setTextColor(Color.parseColor("#212121"));
        chbReturnFromCustomer.setTextColor(Color.parseColor("#212121"));
        chbReturnToVendor.setTextColor(Color.parseColor("#212121"));
        chbSold.setTextColor(Color.parseColor("#212121"));
        chbVoidIncome.setTextColor(Color.parseColor("#212121"));
        chbWaste.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            listener.onApply(new int[]{chbCanceledOrder.isChecked() ? 1 : 0, chbConsignmentCanceled.isChecked() ? 1 : 0,
                    chbIncomeFromVendor.isChecked() ? 1 : 0, chbReturnHold.isChecked() ? 1 : 0,
                    chbReturnFromCustomer.isChecked() ? 1 : 0, chbReturnToVendor.isChecked() ? 1 : 0,
                    chbSold.isChecked() ? 1 : 0, chbVoidIncome.isChecked() ? 1 : 0,
                    chbWaste.isChecked() ? 1 : 0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
