package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    @BindView(R.id.tvCreatedDate)
    TextView tvCreatedDate;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvPaymentDate)
    TextView tvPaymentDate;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvEditedCreatedDate)
    TextView tvEditedCreatedDate;
    @BindView(R.id.tvEditedAccount)
    TextView tvEditedAccount;
    @BindView(R.id.tvEditedAmount)
    TextView tvEditedAmount;
    @BindView(R.id.tvEditedPaymentDate)
    TextView tvEditedPaymentDate;
    @BindView(R.id.llEditedDetails)
    LinearLayout llEditedDetails;

    private SimpleDateFormat simpleDateFormat;

    public interface BillingInfoCallback {
        void onEdit(BillingOperations operations);
    }

    public BillingInfoDialog(@NonNull Context context, BillingOperations operations, DatabaseManager databaseManager, BillingInfoCallback billingInfoCallback) {
        super(context);
        simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        dialogView = getLayoutInflater().inflate(R.layout.billing_info_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Date createdDate = new Date(operations.getCreateAt());
        Date paymentDate = new Date(operations.getPaymentDate());
        Currency currency = databaseManager.getMainCurrency();
        tvCreatedDate.setText(simpleDateFormat.format(createdDate));
        tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
        if (operations.getAccount() != null)
            tvAccount.setText(operations.getAccount().getName());
        else {
            tvAccount.setText("None");
        }
        tvAmount.setText(String.valueOf(operations.getAmount()) + " " + currency.getAbbr());

        if (operations.getRootId() != null) {
            llEditedDetails.setVisibility(View.VISIBLE);
            BillingOperations rootOperation = databaseManager.getBillingOperationByRootId(operations.getRootId()).blockingGet();
            Date editedDate = new Date(rootOperation.getCreateAt());
            Date editedPaymentDate = new Date(rootOperation.getPaymentDate());
            tvEditedCreatedDate.setText(simpleDateFormat.format(editedDate));
            tvEditedPaymentDate.setText(simpleDateFormat.format(editedPaymentDate));
            if (rootOperation.getAccount() != null)
                tvEditedAccount.setText(rootOperation.getAccount().getName());
            else {
                tvEditedAccount.setText("None");
            }
            tvEditedAmount.setText(String.valueOf(rootOperation.getAmount()) + " " + currency.getAbbr());
        }

        btnWarningYES.setOnClickListener(view -> {
            dismiss();
        });

        btnWarningNO.setOnClickListener(view -> {
            billingInfoCallback.onEdit(operations);
            dismiss();
        });

        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
}
