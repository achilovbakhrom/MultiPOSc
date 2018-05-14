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

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.adapter.CustomerDebtsAdapter;
import com.jim.multipos.ui.customer_debt.adapter.PaymentsListAdapter;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_ORDER_NUMBER;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_ORDER_NUMBER_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_STATUS;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_STATUS_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_DEBT_AMOUNT;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_DEBT_AMOUNT_INVERT;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_TAKEN_DATE_TYPE;
import static com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog.CustomerHistorySortingStates.SORTED_BY_TAKEN_DATE_INVERT;

/**
 * Created by developer on 04.12.2017.
 */

public class CustomerHistoryDialog extends Dialog {

    @BindView(R.id.llOrderNumber)
    LinearLayout llOrderNumber;
    @BindView(R.id.ivOrderSort)
    ImageView ivOrderSort;
    @BindView(R.id.llTakenDate)
    LinearLayout llTakenDate;
    @BindView(R.id.ivTakenDateSort)
    ImageView ivTakenDateSort;
    @BindView(R.id.llDebtAmount)
    LinearLayout llDebtAmount;
    @BindView(R.id.ivDebtAmountSort)
    ImageView ivDebtAmountSort;
    @BindView(R.id.llStatus)
    LinearLayout llStatus;
    @BindView(R.id.ivStatusSort)
    ImageView ivStatusSort;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.rvDebtsList)
    RecyclerView rvDebtsList;
    private CustomerDebtsAdapter adapter;
    private Context context;
    private List<Debt> debtList;

    public enum CustomerHistorySortingStates {
        SORTED_BY_ORDER_NUMBER, SORTED_BY_ORDER_NUMBER_INVERT, SORTED_BY_TAKEN_DATE_TYPE, SORTED_BY_TAKEN_DATE_INVERT, SORTED_BY_DEBT_AMOUNT, SORTED_BY_DEBT_AMOUNT_INVERT, SORTED_BY_STATUS, SORTED_BY_STATUS_INVERT
    }

    private CustomerHistorySortingStates filterMode = SORTED_BY_ORDER_NUMBER;

    public CustomerHistoryDialog(@NonNull Context context, Customer customer, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(context);
        this.context = context;
        View dialogView = getLayoutInflater().inflate(R.layout.customer_debts_history_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        adapter = new CustomerDebtsAdapter(context, decimalFormat, databaseManager.getMainCurrency());
        rvDebtsList.setLayoutManager(new LinearLayoutManager(context));
        rvDebtsList.setAdapter(adapter);
        customer.resetDebtList();
        debtList = customer.getDebtList();
        adapter.setItems(debtList);

        llOrderNumber.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_ORDER_NUMBER) {
                filterMode = SORTED_BY_ORDER_NUMBER;
                sortList();
                ivOrderSort.setVisibility(View.VISIBLE);
                ivOrderSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_ORDER_NUMBER_INVERT;
                ivOrderSort.setVisibility(View.VISIBLE);
                ivOrderSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llTakenDate.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_TAKEN_DATE_TYPE) {
                filterMode = SORTED_BY_TAKEN_DATE_TYPE;
                sortList();
                ivTakenDateSort.setVisibility(View.VISIBLE);
                ivTakenDateSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_TAKEN_DATE_INVERT;
                ivTakenDateSort.setVisibility(View.VISIBLE);
                ivTakenDateSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llDebtAmount.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_DEBT_AMOUNT) {
                filterMode = SORTED_BY_DEBT_AMOUNT;
                sortList();
                ivDebtAmountSort.setVisibility(View.VISIBLE);
                ivDebtAmountSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_DEBT_AMOUNT_INVERT;
                ivDebtAmountSort.setVisibility(View.VISIBLE);
                ivDebtAmountSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llStatus.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_STATUS) {
                filterMode = SORTED_BY_STATUS;
                sortList();
                ivStatusSort.setVisibility(View.VISIBLE);
                ivStatusSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_STATUS_INVERT;
                ivStatusSort.setVisibility(View.VISIBLE);
                ivStatusSort.setImageResource(R.drawable.sorting_invert);
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
        ivStatusSort.setVisibility(View.GONE);
        ivDebtAmountSort.setVisibility(View.GONE);
        ivTakenDateSort.setVisibility(View.GONE);
        ivOrderSort.setVisibility(View.GONE);
    }

    private void sortList() {
        switch (filterMode) {
            case SORTED_BY_ORDER_NUMBER:
                Collections.sort(debtList, (debt, t1) -> debt.getOrderId().compareTo(t1.getOrderId()));
                break;
            case SORTED_BY_ORDER_NUMBER_INVERT:
                Collections.sort(debtList, (debt, t1) -> debt.getOrderId().compareTo(t1.getOrderId()) * -1);
                break;
            case SORTED_BY_TAKEN_DATE_TYPE:
                Collections.sort(debtList, (debt, t1) -> debt.getTakenDate().compareTo(t1.getTakenDate()));
                break;
            case SORTED_BY_TAKEN_DATE_INVERT:
                Collections.sort(debtList, (debt, t1) -> debt.getTakenDate().compareTo(t1.getTakenDate()) * -1);
                break;
            case SORTED_BY_DEBT_AMOUNT:
                Collections.sort(debtList, (debt, t1) -> debt.getDebtAmount().compareTo(t1.getDebtAmount()));
                break;
            case SORTED_BY_DEBT_AMOUNT_INVERT:
                Collections.sort(debtList, (debt, t1) -> debt.getDebtAmount().compareTo(t1.getDebtAmount()) * -1);
                break;
            case SORTED_BY_STATUS:
                Collections.sort(debtList, (debt, t1) -> debt.getStatus().compareTo(t1.getStatus()));
                break;
            case SORTED_BY_STATUS_INVERT:
                Collections.sort(debtList, (debt, t1) -> debt.getStatus().compareTo(t1.getStatus()) * -1);
                break;
        }
        adapter.notifyDataSetChanged();
    }

}
