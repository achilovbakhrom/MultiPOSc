package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class OrderMenuDialog extends Dialog {

    @BindView(R.id.tvHeldOrders)
    TextView tvHeldOrders;
    @BindView(R.id.tvTodayOrders)
    TextView tvTodayOrders;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.llSettingsMenu)
    LinearLayout llSettingsMenu;
    @BindView(R.id.tvNewOrders)
    TextView tvNewOrders;


    public OrderMenuDialog(Context context, onOrderMenuItemClickListener listener) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.order_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(30);
        wlp.x = CommonUtils.dpToPx(210);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(250);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        llSettingsMenu.setOnClickListener(view -> dismiss());
        tvTodayOrders.setOnClickListener(view -> {
            listener.onTodayOrderClick();
            dismiss();
        });
        tvHeldOrders.setOnClickListener(view -> {
            listener.onHeldOrderClick();
            dismiss();
        });
        tvNewOrders.setOnClickListener(view -> {
            listener.onNewOrderClick();
            dismiss();
        });
        tvClose.setOnClickListener(view -> {
            dismiss();
        });
    }

    public interface onOrderMenuItemClickListener {
        void onTodayOrderClick();

        void onHeldOrderClick();

        void onNewOrderClick();
    }
}
