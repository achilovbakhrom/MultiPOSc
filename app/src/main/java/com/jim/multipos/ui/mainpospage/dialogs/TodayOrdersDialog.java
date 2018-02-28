package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.ui.mainpospage.adapter.ReturnsListAdapter;
import com.jim.multipos.ui.mainpospage.adapter.TodayOrdersAdapter;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_DATE;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_DATE_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_NAME;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_NAME_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_ORDER_NUMBER;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_ORDER_NUMBER_INVERT;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_TOTAL;
import static com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog.TodayOrderSortingState.SORTED_BY_TOTAL_INVERT;

/**
 * Created by Sirojiddin on 06.01.2018.
 */

public class TodayOrdersDialog extends Dialog {

    @BindView(R.id.rvTodayOrders)
    RecyclerView rvTodayOrders;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.ivOrderSort)
    ImageView ivOrderSort;
    @BindView(R.id.ivDateSort)
    ImageView ivDateSort;
    @BindView(R.id.ivCustomerSort)
    ImageView ivCustomerSort;
    @BindView(R.id.ivTotalSort)
    ImageView ivTotalSort;
    @BindView(R.id.llOrderNumber)
    LinearLayout llOrderNumber;
    @BindView(R.id.llCustomerName)
    LinearLayout llCustomerName;
    @BindView(R.id.llDateOpened)
    LinearLayout llDateOpened;
    @BindView(R.id.llTotal)
    LinearLayout llTotal;
    private TodayOrdersAdapter adapter;

    public enum TodayOrderSortingState {
        SORTED_BY_ORDER_NUMBER, SORTED_BY_ORDER_NUMBER_INVERT, SORTED_BY_NAME, SORTED_BY_NAME_INVERT, SORTED_BY_DATE, SORTED_BY_DATE_INVERT, SORTED_BY_TOTAL, SORTED_BY_TOTAL_INVERT
    }

    private List<Order> orderList;
    private TodayOrderSortingState filterMode = SORTED_BY_ORDER_NUMBER;

    public TodayOrdersDialog(Context context, DatabaseManager databaseManager, onOrderSelect callback) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.today_order_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        orderList = databaseManager.getAllTillClosedOrders().blockingGet();

        rvTodayOrders.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TodayOrdersAdapter(databaseManager.getMainCurrency(), order -> {
            callback.onSelect(order);
            dismiss();
        });
        adapter.setData(orderList);
        rvTodayOrders.setAdapter(adapter);

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
        llTotal.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_TOTAL) {
                filterMode = SORTED_BY_TOTAL;
                sortList();
                ivTotalSort.setVisibility(View.VISIBLE);
                ivTotalSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_TOTAL_INVERT;
                ivTotalSort.setVisibility(View.VISIBLE);
                ivTotalSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

    }

    private void deselectAll() {
        ivTotalSort.setVisibility(View.GONE);
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

    public interface onOrderSelect{
        void onSelect(Order order);
    }
}
