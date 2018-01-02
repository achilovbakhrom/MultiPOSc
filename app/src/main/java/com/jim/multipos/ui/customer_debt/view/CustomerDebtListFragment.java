package com.jim.multipos.ui.customer_debt.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.adapter.DebtListAdapter;
import com.jim.multipos.ui.customer_debt.connection.CustomerDebtConnection;
import com.jim.multipos.ui.customer_debt.presenter.CustomerDebtListPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

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
    public void onDetach() {
        super.onDetach();
        customerDebtConnection.setDebtListView(null);
    }
}
