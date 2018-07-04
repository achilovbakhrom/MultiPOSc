package com.jim.multipos.ui.product_queue_list.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.inventory.StockQueue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockQueueInfoDialog extends Dialog {

    private StockQueue stockQueue;
    @BindView(R.id.etDateCreatead)
    TextView etDateCreated;
    @BindView(R.id.etDateExpired)
    TextView etDateExpired;
    @BindView(R.id.btnOk)
    MpButton btnOk;

    public StockQueueInfoDialog(@NonNull Context context, StockQueue stockQueue) {
        super(context);
        this.stockQueue = stockQueue;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = getLayoutInflater().inflate(R.layout.stock_queue_info_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Calendar created = Calendar.getInstance();
        Calendar expired = Calendar.getInstance();

        if (stockQueue.getCreatedProductDate() != 0) {
            created.setTimeInMillis(stockQueue.getCreatedProductDate());
            etDateCreated.setText(simpleDateFormat.format(stockQueue.getCreatedProductDate()));
        } else etDateCreated.setText(getContext().getString(R.string.none));
        if (stockQueue.getExpiredProductDate() != 0) {
            expired.setTimeInMillis(stockQueue.getCreatedProductDate());
            etDateExpired.setText(simpleDateFormat.format(stockQueue.getExpiredProductDate()));
        } else etDateExpired.setText(getContext().getString(R.string.none));

        btnOk.setOnClickListener(v -> {
            dismiss();
        });
    }
}
