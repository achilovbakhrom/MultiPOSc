package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.adapter.ToNextListAdapter;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.presenter.CloseTillThirdStepPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseTillThirdStepFragment extends BaseFragment implements CloseTillThirdStepView {

    @BindView(R.id.swCloseTill)
    MpSwitcher swCloseTill;
    @BindView(R.id.rvHoldAmount)
    RecyclerView rvHoldAmount;
    @Inject
    CloseTillThirdStepPresenter presenter;
    @Inject
    CashManagementConnection connection;
    private ToNextListAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.to_next_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCloseTillThirdStepView(this);
        swCloseTill.setRight(true);
        rvHoldAmount.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.initData();
        swCloseTill.setSwitcherStateChangedListener((isRight, isLeft) -> {
            adapter.setLeft(isLeft);
            checkCompletion();
        });
    }

    @Override
    public void checkCompletion() {
        presenter.checkCompletion(swCloseTill.isLeft());
    }

    @Override
    public void setDataToRecyclerView(List<TillManagementOperation> operations, Currency currency) {
        adapter = new ToNextListAdapter(getContext(), operations, currency);
        rvHoldAmount.setAdapter(adapter);
        adapter.setLeft(swCloseTill.isLeft());
    }

    @Override
    public void sendThirdStepCompletionStatus(boolean status) {
        connection.setThirdStepCompletionStatus(status);
    }

    @Override
    public void collectData() {
        presenter.collectData();
    }

    @Override
    public void sendAllDataToParent(List<TillManagementOperation> operations) {
        connection.sendAllThirdStepDataToParent(operations);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCloseTillThirdStepView(null);
    }
}
