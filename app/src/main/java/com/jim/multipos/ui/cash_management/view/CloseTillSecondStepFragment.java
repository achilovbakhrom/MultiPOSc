package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.adapter.CloseAmountAdapter;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.presenter.CloseTillSecondStepPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillSecondStepFragment extends BaseFragment implements CloseTillSecondStepView {

    @BindView(R.id.rvCloseAmount)
    RecyclerView rvCloseAmount;

    @Inject
    CloseTillSecondStepPresenter presenter;
    @Inject
    CashManagementConnection connection;
    private CloseAmountAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.close_amount_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCloseTillSecondStepView(this);
        presenter.initData();
        rvCloseAmount.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setDataToRecyclerView(List<TillManagementOperation> operations, Currency currency) {
        adapter = new CloseAmountAdapter(getContext(), operations, currency);
        rvCloseAmount.setAdapter(adapter);
    }

    @Override
    public void checkCompletion() {
        presenter.checkCompletion();
    }

    @Override
    public void sendSecondStepCompletionStatus(boolean status) {
        connection.setSecondStepCompletionStatus(status);
    }

    @Override
    public void collectData() {
        presenter.collectData();
    }

    @Override
    public void sendAllDataToParent(List<TillManagementOperation> operations) {
        connection.sendAllSecondStepDataToParent(operations);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCloseTillSecondStepView(null);
    }
}
