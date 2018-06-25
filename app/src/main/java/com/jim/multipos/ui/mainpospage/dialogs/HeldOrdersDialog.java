package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.cash_management.dialog.CloseOrderWithPayDialog;
import com.jim.multipos.ui.mainpospage.adapter.HeldOrdersAdapter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.OrderEvent;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_DATE;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_DATE_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_NAME;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_NAME_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_ORDER_NUMBER;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_ORDER_NUMBER_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_TOTAL;
import static com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog.HeldOrderSortingState.SORTED_BY_TOTAL_INVERT;

/**
 * Created by Sirojiddin on 06.01.2018.
 */

public class HeldOrdersDialog extends Dialog {

    @BindView(R.id.rvHoldOrders)
    RecyclerView rvHoldOrders;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.ivOrderSort)
    ImageView ivOrderSort;
    @BindView(R.id.ivDateSort)
    ImageView ivDateSort;
    @BindView(R.id.ivCustomerSort)
    ImageView ivCustomerSort;
    @BindView(R.id.ivDueAmountSort)
    ImageView ivDueAmountSort;
    @BindView(R.id.llOrderNumber)
    LinearLayout llOrderNumber;
    @BindView(R.id.llCustomerName)
    LinearLayout llCustomerName;
    @BindView(R.id.llDateOpened)
    LinearLayout llDateOpened;
    @BindView(R.id.llDueAmount)
    LinearLayout llDueAmount;
    private HeldOrdersAdapter adapter;

    public enum HeldOrderSortingState {
        SORTED_BY_ORDER_NUMBER, SORTED_BY_ORDER_NUMBER_INVERT, SORTED_BY_NAME, SORTED_BY_NAME_INVERT, SORTED_BY_DATE, SORTED_BY_DATE_INVERT, SORTED_BY_TOTAL, SORTED_BY_TOTAL_INVERT
    }

    private List<Order> orderList;
    private HeldOrderSortingState filterMode = SORTED_BY_ORDER_NUMBER;

    public HeldOrdersDialog(Context context, DatabaseManager databaseManager, OnHeldOrderSelected callback, PreferencesHelper preferencesHelper, RxBus rxBus) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.hold_order_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        orderList = databaseManager.getAllHoldOrders().blockingGet();

        rvHoldOrders.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HeldOrdersAdapter(context, databaseManager.getMainCurrency(), new HeldOrdersAdapter.onExtraOptionItemClicked() {
            @Override
            public void onCancel(Order order) {
                AccessToCancelDialog dialog = new AccessToCancelDialog(getContext(), new AccessToCancelDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess(String reason) {
                        order.setStatus(Order.CANCELED_ORDER);
                        OrderChangesLog orderChangesLog = new OrderChangesLog();
                        orderChangesLog.setToStatus(Order.CANCELED_ORDER);
                        orderChangesLog.setChangedAt(System.currentTimeMillis());
                        orderChangesLog.setReason(reason);
                        orderChangesLog.setChangedCauseType(OrderChangesLog.HAND_AT_CLOSE_TILL);
                        orderChangesLog.setOrderId(order.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
                        order.setLastChangeLogId(orderChangesLog.getId());

                        databaseManager.cancelOutcomeProductWhenOrderProductCanceled(order.getOrderProducts()).subscribe();


                        if (order.getDebt() != null) {
                            order.getDebt().setIsDeleted(true);
                            databaseManager.addDebt(order.getDebt());
                        }
                        databaseManager.insertOrder(order).blockingGet();
                        orderList.remove(order);
                        adapter.notifyDataSetChanged();
                        rxBus.send(new InventoryStateEvent(GlobalEventConstants.UPDATE));
                        rxBus.send(new OrderEvent(GlobalEventConstants.CANCEL, order));
                    }

                    @Override
                    public void onBruteForce() {

                    }
                }, preferencesHelper);
                dialog.show();
            }

            @Override
            public void onClose(Order order) {
                CloseOrderWithPayDialog dialog = new CloseOrderWithPayDialog(getContext(), databaseManager, order, () -> {
                    order.setStatus(Order.CLOSED_ORDER);
                    order.setIsArchive(false);
                    OrderChangesLog orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.CLOSED_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason("");
                    orderChangesLog.setChangedCauseType(OrderChangesLog.PAYED);
                    orderChangesLog.setOrderId(order.getId());
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
                    order.setLastChangeLogId(orderChangesLog.getId());
                    double totalPayedSum = 0;
                    order.resetPayedPartitions();
                    for (int i = 0; i < order.getPayedPartitions().size(); i++) {
                        totalPayedSum += order.getPayedPartitions().get(i).getValue();
                    }
                    order.setTotalPayed(totalPayedSum);
                    databaseManager.insertOrder(order).blockingGet();
                    orderList.remove(order);
                    adapter.notifyDataSetChanged();
                    rxBus.send(new OrderEvent(GlobalEventConstants.CLOSE, order));
                });
                dialog.show();
            }

            @Override
            public void onSelect(Order order) {
                callback.onSelect(order);
                dismiss();
            }
        });
        adapter.setData(orderList);
        rvHoldOrders.setAdapter(adapter);

        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            dismiss();
        });

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
        llCustomerName.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_NAME) {
                filterMode = SORTED_BY_NAME;
                sortList();
                ivCustomerSort.setVisibility(View.VISIBLE);
                ivCustomerSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_NAME_INVERT;
                ivCustomerSort.setVisibility(View.VISIBLE);
                ivCustomerSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });
        llDateOpened.setOnClickListener(view -> {
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
        llDueAmount.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_TOTAL) {
                filterMode = SORTED_BY_TOTAL;
                sortList();
                ivDueAmountSort.setVisibility(View.VISIBLE);
                ivDueAmountSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_TOTAL_INVERT;
                ivDueAmountSort.setVisibility(View.VISIBLE);
                ivDueAmountSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

    }

    public interface OnHeldOrderSelected {
        void onSelect(Order order);
    }

    private void deselectAll() {
        ivDueAmountSort.setVisibility(View.GONE);
        ivOrderSort.setVisibility(View.GONE);
        ivCustomerSort.setVisibility(View.GONE);
        ivDateSort.setVisibility(View.GONE);
    }

    private void sortList() {
        List<Order> orderListTemp = orderList;
        switch (filterMode) {
            case SORTED_BY_ORDER_NUMBER:
                Collections.sort(orderListTemp, (order, t1) -> order.getId().compareTo(t1.getId()));
                break;
            case SORTED_BY_DATE:
                Collections.sort(orderListTemp, (order, t1) -> order.getCreateAt().compareTo(t1.getCreateAt()));
                break;
            case SORTED_BY_NAME:
                Collections.sort(orderListTemp, (order, t1) -> {
                    String firstName = "", secondName = "";
                    if (order.getCustomer() != null)
                        firstName = order.getCustomer().getName();
                    if (t1.getCustomer() != null)
                        secondName = t1.getCustomer().getName();
                    return firstName.toUpperCase().compareTo(secondName.toUpperCase());
                });
                break;
            case SORTED_BY_TOTAL:
                Collections.sort(orderListTemp, (order, t1) -> order.getTotalPayed().compareTo(t1.getTotalPayed()));
                break;
            case SORTED_BY_DATE_INVERT:
                Collections.sort(orderListTemp, (order, t1) -> order.getCreateAt().compareTo(t1.getCreateAt()) * -1);
                break;
            case SORTED_BY_ORDER_NUMBER_INVERT:
                Collections.sort(orderListTemp, (order, t1) -> order.getId().compareTo(t1.getId()) * -1);
                break;
            case SORTED_BY_NAME_INVERT:
                Collections.sort(orderListTemp, (order, t1) -> {
                    String firstName = "", secondName = "";
                    if (order.getCustomer() != null)
                        firstName = order.getCustomer().getName();
                    if (t1.getCustomer() != null)
                        secondName = t1.getCustomer().getName();
                    return firstName.toUpperCase().compareTo(secondName.toUpperCase()) * -1;
                });
                break;
            case SORTED_BY_TOTAL_INVERT:
                Collections.sort(orderListTemp, (order, t1) -> order.getTotalPayed().compareTo(t1.getTotalPayed()) * -1);
                break;
        }
        adapter.notifyDataSetChanged();
    }
}
