package com.jim.multipos.ui.customer_debt.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.adapter.DebtListAdapter;
import com.jim.multipos.ui.customer_debt.connection.CustomerDebtConnection;
import com.jim.multipos.ui.customer_debt.dialog.PayToDebtDialog;
import com.jim.multipos.ui.customer_debt.presenter.CustomerDebtListPresenter;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tvDebtStatus)
    TextView tvDebtStatus;
    @BindView(R.id.tvOrderSum)
    TextView tvOrderSum;
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

    @Override
    protected int getLayout() {
        return R.layout.debt_list_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        customerDebtConnection.setDebtListView(this);
        rvDebts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDebts.setAdapter(debtListAdapter);
        debtListAdapter.setListener((item, position) -> {
            presenter.initDebtDetails(item, position);
        });
    }

    @Override
    public void initCustomerWithDebt(Customer customer) {
        presenter.initData(customer);
    }

    @Override
    public void fillRecyclerView(List<Debt> debtList, Currency currency) {
        debtListAdapter.setItems(debtList, currency);
    }

    @Override
    public void fillDebtInfo(Long orderNumber, String takenDate, String endDate, String leftDate, String debtType, double fee, String status, double orderSum, double feeAmount, double total, double paidAmount, double feeAmount1, double dueAmount, Currency mainCurrency) {
        tvOrderNumber.setText(String.valueOf(orderNumber));
        tvTakenDate.setText(takenDate);
        tvEndDate.setText(endDate);
        tvLateDate.setText(leftDate);
        tvDebtType.setText(debtType);
        tvDebtFee.setText(fee + " %");
        tvDebtStatus.setText(status);
        tvOrderSum.setText(decimalFormat.format(orderSum) + " " +  mainCurrency.getAbbr());
        tvFeeSum.setText(decimalFormat.format(feeAmount) + " " + mainCurrency.getAbbr());
        tvTotalDebt.setText(decimalFormat.format(total) + " " + mainCurrency.getAbbr());
        tvPaidSum.setText(decimalFormat.format(paidAmount) + " " + mainCurrency.getAbbr());
        tvDueSum.setText(decimalFormat.format(dueAmount) + " " + mainCurrency.getAbbr());
    }

    @Override
    public void openPayToDebt(Debt debt, DatabaseManager databaseManager) {
        PayToDebtDialog dialog = new PayToDebtDialog(getContext(), debt, databaseManager);
        dialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        customerDebtConnection.setDebtListView(null);
    }

    @OnClick(R.id.btnPayToDebt)
    public void onPayToDebt(){
        presenter.onPayToDebt();
    }
}
