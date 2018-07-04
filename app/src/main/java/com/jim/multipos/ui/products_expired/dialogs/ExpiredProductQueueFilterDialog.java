package com.jim.multipos.ui.products_expired.dialogs;

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

public class ExpiredProductQueueFilterDialog extends Dialog {

    @BindView(R.id.chbClosedOrders)
    MpCheckbox chbClosedOrders;
    @BindView(R.id.chbHeldOrders)
    MpCheckbox chbHeldOrders;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private ProductQueueFilterListener listener;

    public interface ProductQueueFilterListener {
        void onApply(int[] config);
    }

    public ExpiredProductQueueFilterDialog(@NonNull Context context, int[] filterConfig, ProductQueueFilterListener listener) {
        super(context);
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.expried_product_queue_dialog, null);
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

        if (filterConfig[0] == 1) chbClosedOrders.setChecked(true);
        else chbClosedOrders.setChecked(false);
        if (filterConfig[1] == 1) chbHeldOrders.setChecked(true);
        else chbHeldOrders.setChecked(false);

        chbClosedOrders.setTextColor(Color.parseColor("#212121"));
        chbHeldOrders.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            listener.onApply(new int[]{chbClosedOrders.isChecked() ? 1 : 0, chbHeldOrders.isChecked() ? 1 : 0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
