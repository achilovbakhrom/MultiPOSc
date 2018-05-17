package com.jim.multipos.ui.cash_management.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.cash_management.adapter.PaymentTypesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 14.02.2018.
 */

public class CloseOrderWithPayDialog extends Dialog {

    @BindView(R.id.rvPaymentTypes)
    RecyclerViewWithMaxHeight rvPaymentTypes;
    private PaymentTypesAdapter adapter;

    public CloseOrderWithPayDialog(@NonNull Context context, DatabaseManager databaseManager, Order order, OnPaymentTypeSelected onPaymentTypeSelected) {
        super(context);

        View dialogView = getLayoutInflater().inflate(R.layout.payments_list_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        rvPaymentTypes.setLayoutManager(new LinearLayoutManager(context));
        rvPaymentTypes.setMaxHeight(300);
        adapter = new PaymentTypesAdapter();
        rvPaymentTypes.setAdapter(adapter);
        adapter.setData(databaseManager.getPaymentTypes());
        double dueSum = order.getForPayAmmount() - order.getTotalPayed();

        adapter.setListener(paymentType -> {
            if (dueSum == 0) {
                onPaymentTypeSelected.onPaymentTypeSelect();
            } else {
                PayedPartitions partitions = new PayedPartitions();
                partitions.setOrderId(order.getId());
                partitions.setPaymentType(paymentType);
                partitions.setValue(dueSum);
                List<PayedPartitions> payedPartitions = new ArrayList<>();
                payedPartitions.add(partitions);
                databaseManager.insertPayedPartitions(payedPartitions).blockingGet();
                onPaymentTypeSelected.onPaymentTypeSelect();
            }
            dismiss();
        });

    }

    public interface OnPaymentTypeSelected {
        void onPaymentTypeSelect();
    }
}
