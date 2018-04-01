package com.jim.multipos.ui.reports.order_history.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class OrderHistoryFilterDialog extends Dialog {

    @BindView(R.id.chbClosedOrders)
    MpCheckbox chbClosedOrders;
    @BindView(R.id.chbHeldOrders)
    MpCheckbox chbHeldOrders;
    @BindView(R.id.chbCanceled)
    MpCheckbox chbCanceled;


    public OrderHistoryFilterDialog(@NonNull Context context) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.order_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(170);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(270);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
    }
}
