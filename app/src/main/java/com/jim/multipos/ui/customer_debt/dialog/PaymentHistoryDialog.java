package com.jim.multipos.ui.customer_debt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.adapter.PaymentsListAdapter;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_DATE;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_DATE_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_DUE;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_DUE_AMOUNT_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_PAYMENT_AMOUNT;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_PAYMENT_AMOUNT_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_PAYMENT_TYPE;
import static com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog.PaymentHistorySortingStates.SORTED_BY_PAYMENT_TYPE_INVERT;

/**
 * Created by developer on 04.12.2017.
 */

public class PaymentHistoryDialog extends Dialog {

    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.ivDateSort)
    ImageView ivDateSort;
    @BindView(R.id.llPaymentType)
    LinearLayout llPaymentType;
    @BindView(R.id.ivPaymentTypeSort)
    ImageView ivPaymentTypeSort;
    @BindView(R.id.llPayment)
    LinearLayout llPayment;
    @BindView(R.id.ivPaymentSort)
    ImageView ivPaymentSort;
    @BindView(R.id.llDue)
    LinearLayout llDue;
    @BindView(R.id.ivDueSort)
    ImageView ivDueSort;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.rvPaymentsList)
    RecyclerView rvPaymentsList;
    @BindView(R.id.tvNoDebts)
    TextView tvNoDebts;
    private PaymentsListAdapter adapter;
    private Context context;
    private List<CustomerPayment> customerPayments;

    public enum PaymentHistorySortingStates {
        SORTED_BY_DATE, SORTED_BY_DATE_INVERT, SORTED_BY_PAYMENT_TYPE, SORTED_BY_PAYMENT_TYPE_INVERT, SORTED_BY_PAYMENT_AMOUNT, SORTED_BY_PAYMENT_AMOUNT_INVERT, SORTED_BY_DUE, SORTED_BY_DUE_AMOUNT_INVERT
    }

    private PaymentHistorySortingStates filterMode = SORTED_BY_DATE;

    public PaymentHistoryDialog(@NonNull Context context, Debt debt, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(context);
        this.context = context;
        View dialogView = getLayoutInflater().inflate(R.layout.customer_payment_history_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        adapter = new PaymentsListAdapter(context, decimalFormat, databaseManager.getMainCurrency());
        rvPaymentsList.setLayoutManager(new LinearLayoutManager(context));
        rvPaymentsList.setAdapter(adapter);
        customerPayments = debt.getCustomerPayments();
        if (customerPayments.size() == 0) {
            tvNoDebts.setVisibility(View.VISIBLE);
        } else tvNoDebts.setVisibility(View.GONE);
        adapter.setItems(customerPayments);

        llDate.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_DATE) {
                filterMode = SORTED_BY_DATE;
                sortList();
                ivDateSort.setVisibility(View.VISIBLE);
                ivDateSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_DATE_INVERT;
                ivDateSort.setVisibility(View.VISIBLE);
                ivDateSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llPaymentType.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_PAYMENT_TYPE) {
                filterMode = SORTED_BY_PAYMENT_TYPE;
                sortList();
                ivPaymentTypeSort.setVisibility(View.VISIBLE);
                ivPaymentTypeSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_PAYMENT_TYPE_INVERT;
                ivPaymentTypeSort.setVisibility(View.VISIBLE);
                ivPaymentTypeSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llPayment.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_PAYMENT_AMOUNT) {
                filterMode = SORTED_BY_PAYMENT_AMOUNT;
                sortList();
                ivPaymentSort.setVisibility(View.VISIBLE);
                ivPaymentSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_PAYMENT_AMOUNT_INVERT;
                ivPaymentSort.setVisibility(View.VISIBLE);
                ivPaymentSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llDue.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_DUE) {
                filterMode = SORTED_BY_DUE;
                sortList();
                ivDueSort.setVisibility(View.VISIBLE);
                ivDueSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_DUE_AMOUNT_INVERT;
                ivDueSort.setVisibility(View.VISIBLE);
                ivDueSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
    }

    @OnClick(R.id.btnBack)
    public void onBackPressed() {
        UIUtils.closeKeyboard(btnBack, context);
        dismiss();
    }

    private void deselectAll() {
        ivDueSort.setVisibility(View.GONE);
        ivPaymentSort.setVisibility(View.GONE);
        ivPaymentTypeSort.setVisibility(View.GONE);
        ivDateSort.setVisibility(View.GONE);
    }

    private void sortList() {
        switch (filterMode) {
            case SORTED_BY_DATE:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentDate().compareTo(t1.getPaymentDate()));
                break;
            case SORTED_BY_DATE_INVERT:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentDate().compareTo(t1.getPaymentDate()) * -1);
                break;
            case SORTED_BY_PAYMENT_TYPE:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentType().getName().toUpperCase().compareTo(t1.getPaymentType().getName().toUpperCase()));
                break;
            case SORTED_BY_PAYMENT_TYPE_INVERT:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentType().getName().toUpperCase().compareTo(t1.getPaymentType().getName().toUpperCase()) * -1);
                break;
            case SORTED_BY_PAYMENT_AMOUNT:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentAmount().compareTo(t1.getPaymentAmount()));
                break;
            case SORTED_BY_PAYMENT_AMOUNT_INVERT:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getPaymentAmount().compareTo(t1.getPaymentAmount()) * -1);
                break;
            case SORTED_BY_DUE:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getDebtDue().compareTo(t1.getDebtDue()));
                break;
            case SORTED_BY_DUE_AMOUNT_INVERT:
                Collections.sort(customerPayments, (customerPayment, t1) -> customerPayment.getDebtDue().compareTo(t1.getDebtDue()) * -1);
                break;
        }
        adapter.notifyDataSetChanged();
    }

}
