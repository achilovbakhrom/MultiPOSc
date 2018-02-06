package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductHistoryAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
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
            //TODO ON EDIT BUTTON CLICKED
        });
        llPaymentDetials.setOnClickListener(view -> {
            presenter.onClickPaymentDetials();
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
}
