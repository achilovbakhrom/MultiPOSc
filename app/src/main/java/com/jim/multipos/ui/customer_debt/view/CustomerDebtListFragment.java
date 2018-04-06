package com.jim.multipos.ui.customer_debt.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.adapter.DebtListAdapter;
import com.jim.multipos.ui.customer_debt.connection.CustomerDebtConnection;
import com.jim.multipos.ui.customer_debt.dialog.CustomerHistoryDialog;
import com.jim.multipos.ui.customer_debt.dialog.PayToDebtDialog;
import com.jim.multipos.ui.customer_debt.dialog.PaymentHistoryDialog;
import com.jim.multipos.ui.customer_debt.presenter.CustomerDebtListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.main_order_events.DebtEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_DUE_DATE;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_DUE_DATE_INVERT;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_ORDER_NUMBER;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_ORDER_NUMBER_INVERT;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_TAKEN_DATE;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_TAKEN_DATE_INVERT;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_TOTAL_DEBT;
import static com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment.DebtSortingStates.SORTED_BY_TOTAL_DEBT_INVERT;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public class CustomerDebtListFragment extends BaseFragment implements CustomerDebtListView {

    @Inject
    CustomerDebtListPresenter presenter;
    @Inject
    DebtListAdapter debtListAdapter;
    @Inject
    CustomerDebtConnection customerDebtConnection;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    RxBus rxBus;
    @BindView(R.id.rvDebts)
    RecyclerView rvDebts;
    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.tvTakenDate)
    TextView tvTakenDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.tvLateDate)
    TextView tvLateDate;
    @BindView(R.id.tvDebtType)
    TextView tvDebtType;
    @BindView(R.id.tvDebtFee)
    TextView tvDebtFee;
    @BindView(R.id.tvDebtAmount)
    TextView tvDebtAmount;
    @BindView(R.id.tvTotalDebt)
    TextView tvTotalDebt;
    @BindView(R.id.tvFeeSum)
    TextView tvFeeSum;
    @BindView(R.id.tvDueSum)
    TextView tvDueSum;
    @BindView(R.id.tvPaidSum)
    TextView tvPaidSum;
    @BindView(R.id.btnPayToDebt)
    TextView btnPayToDebt;
    @BindView(R.id.llLowGround)
    LinearLayout llLowGround;
    @BindView(R.id.llTopGround)
    LinearLayout llTopGround;
    @BindView(R.id.flSeparateLine)
    FrameLayout flSeparateLine;
    @BindView(R.id.tvNoDebts)
    TextView tvNoDebts;
    @BindView(R.id.llOrderNumber)
    LinearLayout llOrderNumber;
    @BindView(R.id.llTakenDate)
    LinearLayout llTakenDate;
    @BindView(R.id.llDueDate)
    LinearLayout llDueDate;
    @BindView(R.id.llTotalDebt)
    LinearLayout llTotalDebt;
    @BindView(R.id.ivOrderSort)
    ImageView ivOrderSort;
    @BindView(R.id.ivTakenDateSort)
    ImageView ivTakenDateSort;
    @BindView(R.id.ivDueDateSort)
    ImageView ivDueDateSort;
    @BindView(R.id.ivTotalDebtSort)
    ImageView ivTotalDebtSort;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.tvCustomerTotalDebt)
    TextView tvCustomerTotalDebt;

    private boolean payToAll = false;
    private int selectedPosition = 0;

    public enum DebtSortingStates {
        SORTED_BY_ORDER_NUMBER, SORTED_BY_ORDER_NUMBER_INVERT, SORTED_BY_TAKEN_DATE, SORTED_BY_TAKEN_DATE_INVERT,
        SORTED_BY_DUE_DATE, SORTED_BY_DUE_DATE_INVERT, SORTED_BY_TOTAL_DEBT, SORTED_BY_TOTAL_DEBT_INVERT
    }

    DebtSortingStates sortMode = DebtSortingStates.SORTED_BY_TAKEN_DATE;

    @Override
    protected int getLayout() {
        return R.layout.debt_list_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        customerDebtConnection.setDebtListView(this);
        rvDebts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDebts.setAdapter(debtListAdapter);
        ((SimpleItemAnimator) rvDebts.getItemAnimator()).setSupportsChangeAnimations(false);
        debtListAdapter.setListener(new DebtListAdapter.OnDebtClickListener() {
            @Override
            public void onItemClicked(Debt item, int position) {
                if (selectedPosition == position) {
                    if (payToAll) {
                        llLowGround.setVisibility(View.GONE);
                        llTopGround.setVisibility(View.VISIBLE);
                        payToAll = false;
                        presenter.initTotalDataOfCustomer();
                    } else {
                        llLowGround.setVisibility(View.VISIBLE);
                        llTopGround.setVisibility(View.GONE);
                        payToAll = true;
                        presenter.initDebtDetails(item, position);
                    }
                } else {
                    selectedPosition = position;
                    llLowGround.setVisibility(View.VISIBLE);
                    llTopGround.setVisibility(View.GONE);
                    payToAll = true;
                    presenter.initDebtDetails(item, position);
                }
            }

            @Override
            public void onCloseDebt(Debt item) {
                presenter.closeDebtWithPayingAllAmount(item);
            }
        });

        llOrderNumber.setOnClickListener(view -> {
            deselectAll();
            if (sortMode != SORTED_BY_ORDER_NUMBER) {
                sortMode = SORTED_BY_ORDER_NUMBER;
                presenter.filterBy(SORTED_BY_ORDER_NUMBER);
                ivOrderSort.setVisibility(View.VISIBLE);
                ivOrderSort.setImageResource(R.drawable.sorting);
            } else {
                sortMode = SORTED_BY_ORDER_NUMBER_INVERT;
                ivOrderSort.setVisibility(View.VISIBLE);
                ivOrderSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llTakenDate.setOnClickListener(view -> {
            deselectAll();
            if (sortMode != SORTED_BY_TAKEN_DATE) {
                sortMode = SORTED_BY_TAKEN_DATE;
                presenter.filterBy(SORTED_BY_TAKEN_DATE);
                ivTakenDateSort.setVisibility(View.VISIBLE);
                ivTakenDateSort.setImageResource(R.drawable.sorting);
            } else {
                sortMode = SORTED_BY_TAKEN_DATE_INVERT;
                ivTakenDateSort.setVisibility(View.VISIBLE);
                ivTakenDateSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llTotalDebt.setOnClickListener(view -> {
            deselectAll();
            if (sortMode != SORTED_BY_TOTAL_DEBT) {
                sortMode = SORTED_BY_TOTAL_DEBT;
                presenter.filterBy(SORTED_BY_TOTAL_DEBT);
                ivTotalDebtSort.setVisibility(View.VISIBLE);
                ivTotalDebtSort.setImageResource(R.drawable.sorting);
            } else {
                sortMode = SORTED_BY_TOTAL_DEBT_INVERT;
                ivTotalDebtSort.setVisibility(View.VISIBLE);
                ivTotalDebtSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });
        llDueDate.setOnClickListener(view -> {
            deselectAll();
            if (sortMode != SORTED_BY_DUE_DATE) {
                sortMode = SORTED_BY_DUE_DATE;
                presenter.filterBy(SORTED_BY_DUE_DATE);
                ivDueDateSort.setVisibility(View.VISIBLE);
                ivDueDateSort.setImageResource(R.drawable.sorting);
            } else {
                sortMode = SORTED_BY_DUE_DATE_INVERT;
                ivDueDateSort.setVisibility(View.VISIBLE);
                ivDueDateSort.setImageResource(R.drawable.sorting_invert);
                presenter.filterInvert();
            }
        });

        llPay.setOnClickListener(view -> presenter.openPayToAllDialog());
    }

    @Override
    public void initCustomerWithDebt(Customer customer) {
        presenter.initData(customer);
    }

    @Override
    public void fillRecyclerView(List<Debt> debtList, Currency currency) {
        debtListAdapter.setItems(debtList, currency);
        debtListAdapter.setSelectedPosition(-1);
    }

    @Override
    public void fillDebtInfo(Long orderNumber, String takenDate, String endDate, String leftDate, int debtType, double fee, double feeAmount, double total, double paidAmount, double dueAmount, Currency mainCurrency, Double debtAmount) {
        tvOrderNumber.setText(String.valueOf(orderNumber));
        tvTakenDate.setText(takenDate);
        tvEndDate.setText(endDate);
        if (leftDate.equals("Overdue"))
            tvLateDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
        else tvLateDate.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainText));
        tvLateDate.setText(leftDate);
        if (debtType == Debt.PARTICIPLE)
            tvDebtType.setText(getContext().getString(R.string.can_participle));
        else tvDebtType.setText(getContext().getString(R.string.all));
        tvDebtFee.setText(fee + " %");
        tvFeeSum.setText(decimalFormat.format(feeAmount) + " " + mainCurrency.getAbbr());
        tvTotalDebt.setText(decimalFormat.format(total) + " " + mainCurrency.getAbbr());
        tvPaidSum.setText(decimalFormat.format(paidAmount) + " " + mainCurrency.getAbbr());
        tvDueSum.setText(decimalFormat.format(dueAmount) + " " + mainCurrency.getAbbr());
        tvDebtAmount.setText(decimalFormat.format(debtAmount) + " " + mainCurrency.getAbbr());
    }

    @Override
    public void openPayToDebt(Debt debt, DatabaseManager databaseManager, boolean closeDebt, boolean payToAll, Customer customer) {
        PayToDebtDialog dialog = new PayToDebtDialog(getContext(), customer, debt, databaseManager, closeDebt, payToAll, customer1 -> {
            presenter.initData(customer1);
            customerDebtConnection.updateCustomersList();
            rxBus.send(new DebtEvent(debt, GlobalEventConstants.UPDATE));
        });
        dialog.show();
    }

    @Override
    public void openPaymentHistoryDialog(Debt debt, DatabaseManager databaseManager) {
        PaymentHistoryDialog paymentHistoryDialog = new PaymentHistoryDialog(getContext(), debt, databaseManager, decimalFormat);
        paymentHistoryDialog.show();
    }

    @Override
    public void openCustomerDebtsHistoryDialog(Customer customer, DatabaseManager databaseManager) {
        CustomerHistoryDialog dialog = new CustomerHistoryDialog(getContext(), customer, databaseManager, decimalFormat);
        dialog.show();
    }

    @Override
    public void setCustomerDebtListVisibility(int visibility) {
        llLowGround.setVisibility(visibility);
        llTopGround.setVisibility(visibility);
        flSeparateLine.setVisibility(visibility);
        if (visibility == View.GONE)
            tvNoDebts.setVisibility(View.VISIBLE);
        else tvNoDebts.setVisibility(View.GONE);
    }

    @Override
    public void notifyList() {
        debtListAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillTotalInfo(double total, Currency currency) {
        tvCustomerTotalDebt.setText(decimalFormat.format(total) + " " + currency.getAbbr());
        llLowGround.setVisibility(View.GONE);
        llTopGround.setVisibility(View.VISIBLE);
    }

    @Override
    public void openWarningDialog() {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), getString(R.string.customer_has_not_got_debts), () -> {

        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customerDebtConnection.setDebtListView(null);
    }

    @OnClick(R.id.btnPayToDebt)
    public void onPayToDebtClicked() {
        presenter.onPayToDebt();
    }

    @OnClick(R.id.btnPaymentHistory)
    public void onPaymentHistoryClicked() {
        presenter.onPaymentHistoryClicked();
    }

    @OnClick(R.id.btnDebtsHistory)
    public void onDebtsHistoryClicked() {
        presenter.onCustomerDebtsHistoryClicked();
    }

    private void deselectAll() {
        ivOrderSort.setVisibility(View.GONE);
        ivDueDateSort.setVisibility(View.GONE);
        ivTakenDateSort.setVisibility(View.GONE);
        ivTotalDebtSort.setVisibility(View.GONE);
    }
}
