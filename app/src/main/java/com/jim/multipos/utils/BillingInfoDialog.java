package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.billing_vendor.adapter.BillingInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class BillingInfoDialog extends Dialog {
    private View dialogView;
    @BindView(R.id.btnWarningYES)
    MpButton btnWarningYES;
    @BindView(R.id.btnWarningNO)
    MpButton btnWarningNO;
    @BindView(R.id.rvPayments)
    RecyclerViewWithMaxHeight rvPayments;
    BillingInfoAdapter adapter;
    List<Object> billingOperations;

    public interface BillingInfoCallback {
        void onEdit(BillingOperations operations);
    }

    public BillingInfoDialog(@NonNull Context context, BillingOperations operations, DatabaseManager databaseManager, BillingInfoCallback billingInfoCallback) {
        super(context);
        dialogView = getLayoutInflater().inflate(R.layout.billing_info_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        rvPayments.setLayoutManager(new LinearLayoutManager(context));
        billingOperations = new ArrayList<>();
        Currency currency = databaseManager.getMainCurrency();
        adapter = new BillingInfoAdapter(context, currency);

        rvPayments.setAdapter(adapter);
        operations.resetOperationsHistoryList();
        if (operations.getOperationsHistoryList() != null && operations.getOperationsHistoryList().size() != 0) {
            billingOperations.add(operations);
            billingOperations.addAll(operations.getOperationsHistoryList());
            adapter.setData(billingOperations);
        } else {
            billingOperations.add(operations);
            adapter.setData(billingOperations);
        }

        btnWarningYES.setOnClickListener(view -> {
            dismiss();
        });

        if (operations.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT) {
            btnWarningNO.setVisibility(View.VISIBLE);
        } else {
            btnWarningNO.setVisibility(View.GONE);
        }

        btnWarningNO.setOnClickListener(view -> {
            billingInfoCallback.onEdit(operations);
            dismiss();
        });


        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
}
