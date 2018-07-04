package com.jim.multipos.ui.products_expired.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.inventory.StockQueue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpiredProductQueueInfoDialog extends Dialog {

    private StockQueue stockQueue;
    @BindView(R.id.etDateCreatead)
    TextView etDateCreated;
    @BindView(R.id.etDateExpired)
    TextView etDateExpired;
    @BindView(R.id.tvCost)
    TextView tvCost;
    @BindView(R.id.btnOk)
    MpButton btnOk;

    public ExpiredProductQueueInfoDialog(@NonNull Context context, StockQueue stockQueue) {
        super(context);
        this.stockQueue = stockQueue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = getLayoutInflater().inflate(R.layout.expired_stock_queue_info_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Calendar expired = Calendar.getInstance();

        if (stockQueue.getStockId() != null) {
            etDateCreated.setText(stockQueue.getStockId());
        } else etDateCreated.setText(getContext().getString(R.string.none));
        DecimalFormat decimalFormat = BaseAppModule.getFormatterGrouping();
        tvCost.setText(decimalFormat.format(stockQueue.getCost()));
        if (stockQueue.getCreatedProductDate() != 0) {
            expired.setTimeInMillis(stockQueue.getCreatedProductDate());
            etDateExpired.setText(simpleDateFormat.format(stockQueue.getCreatedProductDate()));
        } else etDateExpired.setText(getContext().getString(R.string.none));

        btnOk.setOnClickListener(v -> {
            dismiss();
        });
    }
}
