package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.billing_vendor.adapter.BillingInfoAdapter;
import com.jim.multipos.ui.cash_management.CashManagementActivity;
import com.jim.multipos.ui.mainpospage.dialogs.ReturnsDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class MainMenuDialog extends Dialog {

    @BindView(R.id.llSettingsMenu)
    LinearLayout llSettingsMenu;
    @BindView(R.id.tvCashManagement)
    TextView tvCashManagement;
    @BindView(R.id.tvReturns)
    TextView tvReturns;
    @BindView(R.id.tvLogOut)
    TextView tvLogOut;
    @BindView(R.id.tvSettings)
    TextView tvSettings;
    @BindView(R.id.tvClose)
    TextView tvClose;
    @BindView(R.id.tvBarcodeScanner)
    TextView tvBarcodeScanner;

    public MainMenuDialog(Context context, DatabaseManager databaseManager, DecimalFormat decimalFormat, boolean isBarcodeShown, onMenuItemClickListener listener) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.settings_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(30);
        wlp.x = CommonUtils.dpToPx(60);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(290);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        llSettingsMenu.setOnClickListener(view -> dismiss());
        tvReturns.setOnClickListener(view -> {
            listener.onReturn();
            dismiss();
        });
        tvCashManagement.setOnClickListener(view -> {
            listener.onCashManagement();
            dismiss();
        });
        tvLogOut.setOnClickListener(view -> {
            listener.onLogOut();
            dismiss();
        });
        tvSettings.setOnClickListener(view -> {
            listener.onSettings();
            dismiss();
        });
        tvClose.setOnClickListener(view -> dismiss());

        if (!isBarcodeShown)
            tvBarcodeScanner.setText(context.getString(R.string.barcode_scanner));
        else tvBarcodeScanner.setText(context.getString(R.string.turn_off_barcode_scanner));

        tvBarcodeScanner.setOnClickListener(view -> {
            if (!isBarcodeShown)
                listener.onTurnOnBarcodeScanner();
            else listener.onTurnOffBarcodeScanner();
            dismiss();
        });
    }

    public interface onMenuItemClickListener {
        void onCashManagement();
        void onTurnOnBarcodeScanner();
        void onTurnOffBarcodeScanner();
        void onReturn();
        void onSettings();
        void onLogOut();
    }

}
