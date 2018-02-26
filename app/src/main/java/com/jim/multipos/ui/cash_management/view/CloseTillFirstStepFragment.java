package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.cash_management.adapter.ReconcileOrdersAdapter;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.dialog.CloseOrderWithPayDialog;
import com.jim.multipos.ui.cash_management.presenter.CloseTillFirstStepPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillFirstStepFragment extends BaseFragment implements CloseTillFirstStepView {

    @BindView(R.id.rvReconcileOrders)
    RecyclerView rvReconcileOrders;

    @Inject
    CloseTillFirstStepPresenter presenter;
    @Inject
    CashManagementConnection connection;
    @Inject
    DatabaseManager databaseManager;
    private ReconcileOrdersAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.reconcile_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCloseTillFirstStepView(this);
        rvReconcileOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReconcileOrdersAdapter(getContext(), new ReconcileOrdersAdapter.onExtraOptionItemClicked() {
            @Override
            public void onReturn(Order order, int position) {
                presenter.onReturn(order, position);
            }

            @Override
            public void onClose(Order order, int position) {
                CloseOrderWithPayDialog dialog = new CloseOrderWithPayDialog(getContext(), databaseManager, order, () -> presenter.onClose(order, position));
                dialog.show();
            }

            @Override
            public void onGoToOrder(Order order) {
                presenter.onGoToOrder(order);
            }
        });
        rvReconcileOrders.setAdapter(adapter);
        presenter.initAdapterData();
    }

    @Override
    public void setAdapterList(List<Order> orderList) {
        adapter.setData(orderList);
    }

    @Override
    public void checkCompletion() {
        presenter.checkCompletion();
    }

    @Override
    public void firstStepCompletionStatus(boolean status) {
        connection.setFirstStepCompletionStatus(status);
    }

    @Override
    public void collectData() {

    }

    @Override
    public void updateOrderList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCloseTillFirstStepView(null);
    }
}
