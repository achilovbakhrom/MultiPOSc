package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.cash_management.adapter.ReconcileOrdersAdapter;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.dialog.CloseOrderWithPayDialog;
import com.jim.multipos.ui.cash_management.presenter.CloseTillFirstStepPresenter;
import com.jim.multipos.ui.mainpospage.dialogs.AccessToCancelDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.OrderEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillFirstStepFragment extends BaseFragment implements CloseTillFirstStepView {

    @BindView(R.id.rvReconcileOrders)
    RecyclerView rvReconcileOrders;
    @BindView(R.id.tvEmptyText)
    TextView tvEmptyText;

    @Inject
    CloseTillFirstStepPresenter presenter;
    @Inject
    CashManagementConnection connection;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    RxBus rxBus;
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
                AccessToCancelDialog dialog = new AccessToCancelDialog(getContext(), new AccessToCancelDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess(String reason) {
                        presenter.onReturn(order, position, reason);
                    }

                    @Override
                    public void onBruteForce() {

                    }
                }, preferencesHelper);
                dialog.show();
            }

            @Override
            public void onClose(Order order, int position) {
                CloseOrderWithPayDialog dialog = new CloseOrderWithPayDialog(getContext(), databaseManager, order, () -> presenter.onClose(order, position));
                dialog.show();
            }
        });

        rvReconcileOrders.setAdapter(adapter);
        presenter.initAdapterData();
    }

    @Override
    public void setAdapterList(List<Order> orderList) {
        adapter.setData(orderList);
        if (orderList.size() > 0) {
            tvEmptyText.setVisibility(View.GONE);
            rvReconcileOrders.setVisibility(View.VISIBLE);
        } else {
            tvEmptyText.setVisibility(View.VISIBLE);
            rvReconcileOrders.setVisibility(View.GONE);
        }
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
    public void sendEvent(int event, Order order) {
        rxBus.send(new OrderEvent(event, order));
    }

    @Override
    public void sendInventoryStateChangeEvent(int type) {
        rxBus.send(new InventoryStateEvent(type));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCloseTillFirstStepView(null);
    }
}
