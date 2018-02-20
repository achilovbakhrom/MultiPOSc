package com.jim.multipos.ui.mainpospage.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductHistoryAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.AccessToCancelDialog;
import com.jim.multipos.ui.mainpospage.dialogs.AccessToEditDialog;
import com.jim.multipos.ui.mainpospage.dialogs.AccessWithEditPasswordDialog;
import com.jim.multipos.ui.mainpospage.dialogs.PaymentDetialDialog;
import com.jim.multipos.ui.mainpospage.presenter.OrderListHistoryPresenter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryFragment extends BaseFragment implements OrderListHistoryView{

    @Inject
    OrderListHistoryPresenter presenter;
    @Inject
    MainPageConnection mainPageConnection;
    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvDiscountAmount)
    TextView tvDiscountAmount;
    @BindView(R.id.tvServiceAmount)
    TextView tvServiceAmount;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTotalToPay)
    TextView tvTotalToPay;
    @BindView(R.id.tvCreatedDate)
    TextView tvCreatedDate;
    @BindView(R.id.tvCreatedTime)
    TextView tvCreatedTime;
    @BindView(R.id.tvDebtAmmount)
    TextView tvDebtAmmount;
    @BindView(R.id.tvTotalPayed)
    TextView tvTotalPayed;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.rvOrderProducts)
    RecyclerView rvOrderProducts;
    @BindView(R.id.llEdit)
    LinearLayout llEdit;
    @BindView(R.id.llPaymentDetials)
    LinearLayout llPaymentDetials;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvOrderNumber)
    TextView tvOrderNumber;
    @BindView(R.id.llCancelOrder)
    LinearLayout llCancelOrder;
    @BindView(R.id.ivDeactivateCancel)
    ImageView ivDeactivateCancel;
    @BindView(R.id.tvCancelOrder)
    TextView tvCancelOrder;
    @BindView(R.id.tvCauseDelete)
    TextView tvCauseDelete;
    @BindView(R.id.rvDeleteCurtain)
    RelativeLayout rvDeleteCurtain;
    @BindView(R.id.tvOrderCancelLable)
    TextView tvOrderCancelLable;

    OrderProductHistoryAdapter orderProductHistoryAdapter;
    DecimalFormat decimalFormat;
    SimpleDateFormat sdfDate;
    SimpleDateFormat sdfTime;
    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setOrderListHistoryView(this);
        presenter.onCreateView(savedInstanceState);
        decimalFormat = new DecimalFormat("#,###.##");
        sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        sdfTime = new SimpleDateFormat("HH:mm");
        llEdit.setOnClickListener(view -> {
            presenter.onEditClicked();
        });
        llPaymentDetials.setOnClickListener(view -> {
            presenter.onClickPaymentDetials();
        });

        llCancelOrder.setOnClickListener(view -> {
           presenter.onCancelClicked();
        });
    }

    public void refreshData(){
        presenter.refreshData();
    }
    public void onNextOrder(){
        presenter.onNextOrder();
    }
    public void onPrevOrder(){
        presenter.onPrevOrder();
    }
    @Override
    public void onDetach() {
        mainPageConnection.setOrderListHistoryView(null);

        super.onDetach();
    }

    @Override
    public void hideMeAndShowOrderList() {
        ((MainPosPageActivity)getActivity()).showOrderListFragmentWhenOrderHistoryEnds();
    }

    @Override
    public void updateDetials(Order order) {
        ((MainPosPageActivity)getActivity()).setOrderNo(order.getId());
        tvSubTotal.setText(decimalFormat.format(order.getSubTotalValue()));
        tvDiscountAmount.setText(decimalFormat.format(order.getDiscountTotalValue()));
        if(order.getServiceTotalValue()!=0)
            tvServiceAmount.setText("+"+decimalFormat.format(order.getServiceTotalValue()));
        else
            tvServiceAmount.setText("0");

        tvTips.setText(decimalFormat.format(order.getTips()));
        tvTotalToPay.setText(decimalFormat.format(order.getForPayAmmount()));
        tvCreatedDate.setText(sdfDate.format(order.getCreateAt()));
        tvCreatedTime.setText(sdfTime.format(order.getCreateAt()));
        tvDebtAmmount.setText(decimalFormat.format(order.getToDebtValue()));
        tvTotalPayed.setText(decimalFormat.format(order.getTotalPayed()));
        tvChange.setText(decimalFormat.format(order.getForPayAmmount()-order.getTotalPayed()));
        if(order.getCustomer() != null) {
            tvCustomerName.setVisibility(View.VISIBLE);
            tvCustomerName.setText("Customer: " + order.getCustomer().getName());
        }
        else tvCustomerName.setVisibility(View.GONE);
        tvOrderNumber.setText("Order #"+String.valueOf(order.getId()));
        if(order.getStatus()==Order.CANCELED_ORDER ){
            if(order.getLastChangeLog().getChangedCauseType() == OrderChangesLog.EDITED){
                llEdit.setVisibility(View.INVISIBLE);
                llEdit.setEnabled(false);
                ivDeactivateCancel.setImageResource(R.drawable.recive_order);
                tvCancelOrder.setText("Restore order");
                tvOrderCancelLable.setText("Order Edited to #"+String.valueOf(order.getLastChangeLog().getRelationOrder().getId()));
                tvOrderNumber.setText("Canceled: Order #" + String.valueOf(order.getId()));
                rvDeleteCurtain.setVisibility(View.VISIBLE);
                tvCauseDelete.setText("Cause: " + order.getLastChangeLog().getReason());
            }else {
                llEdit.setVisibility(View.INVISIBLE);
                llEdit.setEnabled(false);
                ivDeactivateCancel.setImageResource(R.drawable.recive_order);
                tvCancelOrder.setText("Restore order");
                tvOrderNumber.setText("Canceled: Order #" + String.valueOf(order.getId()));
                tvOrderCancelLable.setText("Order canceled");
                rvDeleteCurtain.setVisibility(View.VISIBLE);
                tvCauseDelete.setText("Cause: " + order.getLastChangeLog().getReason());
            }
        }else {
            llEdit.setVisibility(View.VISIBLE);
            llEdit.setEnabled(true);
            ivDeactivateCancel.setImageResource(R.drawable.deactive_order);
            tvCancelOrder.setText(getContext().getString(R.string.cancel_order));
            rvDeleteCurtain.setVisibility(View.GONE);
        }
    }

    @Override
    public void initOrderListRecycler(List<Object> objectList) {
        orderProductHistoryAdapter = new OrderProductHistoryAdapter(objectList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvOrderProducts.setLayoutManager(linearLayoutManager);
        rvOrderProducts.setAdapter(orderProductHistoryAdapter);
    }

    @Override
    public void notifyList() {
        orderProductHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void openPaymentDetailDialog(List<PayedPartitions> payedPartitions, Currency mainCurrency) {
        PaymentDetialDialog paymentDetialDialog = new PaymentDetialDialog(getContext(),payedPartitions,decimalFormat,mainCurrency);
        paymentDetialDialog.show();
    }

    @Override
    public void openEditFragment(String reason, Order order) {
        mainPageConnection.openEditFragment(reason,order);
    }

    @Override
    public void openEditAccsessDialog() {
        AccessToEditDialog accessToEditDialog = new AccessToEditDialog(getContext(), new AccessToEditDialog.OnAccsessListner() {
            @Override
            public void accsessSuccess(String reason) {
                presenter.onEditOrder(reason);
            }

            @Override
            public void onBruteForce() {
                presenter.onBruteForce();
            }
        },preferencesHelper);
        accessToEditDialog.show();
    }

    @Override
    public void openCancelAccsessDialog() {
        AccessToCancelDialog accessToCancelDialog = new AccessToCancelDialog(getContext(), new AccessToCancelDialog.OnAccsessListner() {
            @Override
            public void accsessSuccess(String reason) {
                presenter.onCancelOrder(reason);
            }
            @Override
            public void onBruteForce() {
                presenter.onBruteForce();
            }
        },preferencesHelper);
        accessToCancelDialog.show();
    }

    @Override
    public void openRestoreAccsessDialog() {
        AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(getContext(), new AccessWithEditPasswordDialog.OnAccsessListner() {
            @Override
            public void accsessSuccess() {
                presenter.onRestoreDialog();
            }

            @Override
            public void onBruteForce() {

            }
        },preferencesHelper);
        accessWithEditPasswordDialog.show();
    }

    @Override
    public void onEditComplete(String reason,Long orderId) {
        presenter.onEditComplete(reason,orderId);
    }


}
