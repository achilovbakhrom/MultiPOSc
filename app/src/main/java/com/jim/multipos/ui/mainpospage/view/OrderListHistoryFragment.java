package com.jim.multipos.ui.mainpospage.view;

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
import com.jim.multipos.data.DatabaseManager;
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
import com.jim.multipos.ui.mainpospage.dialogs.PaymentDetialDialog;
import com.jim.multipos.ui.mainpospage.presenter.OrderListHistoryPresenter;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryFragment extends BaseFragment implements OrderListHistoryView {

    @Inject
    OrderListHistoryPresenter presenter;
    @Inject
    MainPageConnection mainPageConnection;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DatabaseManager databaseManager;

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
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;
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
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.tvPay)
    TextView tvPay;
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
        presenter.onCreateView(getArguments());
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
        llPrintCheck.setOnClickListener(view -> {
            presenter.reprintOrder();
        });
    }

    public void refreshData(Order order) {
        presenter.refreshData(order);
    }

    @Override
    public void onDetach() {
        mainPageConnection.setOrderListHistoryView(null);

        super.onDetach();
    }


    @Override
    public void updateDetials(Order order) {
        tvSubTotal.setText(decimalFormat.format(order.getSubTotalValue()));
        tvDiscountAmount.setText(decimalFormat.format(order.getDiscountTotalValue()));
        if (order.getServiceTotalValue() != 0)
            tvServiceAmount.setText("+" + decimalFormat.format(order.getServiceTotalValue()));
        else
            tvServiceAmount.setText("0");

        //bundo qiganimi sababi UI chizilganda tepadigi indecatorni ozgariwi
        rvDeleteCurtain.post(() -> {
            ((MainPosPageActivity) getActivity()).updateIndicator(order.getId());
        });
        tvTips.setText(decimalFormat.format(order.getTips()));
        tvTotalToPay.setText(decimalFormat.format(order.getForPayAmmount()));
        tvCreatedDate.setText(sdfDate.format(order.getCreateAt()));
        tvCreatedTime.setText(sdfTime.format(order.getCreateAt()));
        tvDebtAmmount.setText(decimalFormat.format(order.getToDebtValue()));
        tvTotalPayed.setText(decimalFormat.format(order.getTotalPayed()));
        tvChange.setText(decimalFormat.format(order.getChange()));
        if (order.getCustomer() != null) {
            tvCustomerName.setVisibility(View.VISIBLE);
            tvCustomerName.setText(getContext().getString(R.string.customer_) + " " + order.getCustomer().getName());
        } else tvCustomerName.setVisibility(View.GONE);
        tvOrderNumber.setText(getContext().getString(R.string.order_num) + String.valueOf(order.getId()));
        if (order.getStatus() == Order.CANCELED_ORDER) {
            if (order.getLastChangeLog().getChangedCauseType() == OrderChangesLog.EDITED) {
                llEdit.setVisibility(View.INVISIBLE);
                llEdit.setEnabled(false);
                ivDeactivateCancel.setVisibility(View.GONE);
                tvCancelOrder.setVisibility(View.GONE);
                tvOrderCancelLable.setText(getContext().getString(R.string.order_edited_to_num) + String.valueOf(order.getLastChangeLog().getRelationOrder().getId()));
                tvOrderNumber.setText(getContext().getString(R.string.canceled_order_num) + String.valueOf(order.getId()));
                rvDeleteCurtain.setVisibility(View.VISIBLE);
                tvCauseDelete.setText(getContext().getString(R.string.cause_) + order.getLastChangeLog().getReason());
            } else {
                llEdit.setVisibility(View.INVISIBLE);
                llEdit.setEnabled(false);
                ivDeactivateCancel.setVisibility(View.GONE);
                tvCancelOrder.setVisibility(View.GONE);
                tvOrderNumber.setText(getContext().getString(R.string.canceled_order_num) + String.valueOf(order.getId()));
                tvOrderCancelLable.setText(getContext().getString(R.string.order_canceled));
                rvDeleteCurtain.setVisibility(View.VISIBLE);
                tvCauseDelete.setText(getContext().getString(R.string.cause_) + order.getLastChangeLog().getReason());
            }
        } else if (order.getStatus() == Order.HOLD_ORDER) {
            ivEdit.setImageResource(R.drawable.contunie);
            tvPay.setText(getContext().getString(R.string.order_continue));
            ivDeactivateCancel.setVisibility(View.VISIBLE);
            tvCancelOrder.setVisibility(View.VISIBLE);
            tvOrderNumber.setText(getString(R.string.held_order_num) + String.valueOf(order.getId()));
            llEdit.setVisibility(View.VISIBLE);
            llEdit.setEnabled(true);
            ivDeactivateCancel.setImageResource(R.drawable.deactive_order);
            tvCancelOrder.setText(getContext().getString(R.string.cancel_order));
            rvDeleteCurtain.setVisibility(View.GONE);
        } else {
            ivEdit.setImageResource(R.drawable.edit);
            tvPay.setText(getContext().getString(R.string.edit));
            llEdit.setVisibility(View.VISIBLE);
            llEdit.setEnabled(true);
            ivDeactivateCancel.setVisibility(View.VISIBLE);
            tvCancelOrder.setVisibility(View.VISIBLE);
            ivDeactivateCancel.setImageResource(R.drawable.deactive_order);
            tvCancelOrder.setText(getContext().getString(R.string.cancel_order));
            rvDeleteCurtain.setVisibility(View.GONE);
        }
    }

    @Override
    public void initOrderListRecycler(List<Object> objectList) {
        orderProductHistoryAdapter = new OrderProductHistoryAdapter(objectList, getActivity());
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
        PaymentDetialDialog paymentDetialDialog = new PaymentDetialDialog(getContext(), payedPartitions, decimalFormat, mainCurrency);
        paymentDetialDialog.show();
    }


    @Override
    public void openEditAccsessDialog() {
        if (preferencesHelper.isEditOrderProtected()) {
            AccessToEditDialog accessToEditDialog = new AccessToEditDialog(getContext(), new AccessToEditDialog.OnAccsessListner() {
                @Override
                public void accsessSuccess(String reason) {
                    ((MainPosPageActivity) getActivity()).onEditOrder(reason);
                }

                @Override
                public void onBruteForce() {
                    presenter.onBruteForce();
                }
            }, preferencesHelper);
            accessToEditDialog.show();
        } else {
            WarningDialog warningDialog = new WarningDialog(getActivity());
            warningDialog.setWarningMessage(getString(R.string.do_u_want_edit_order));
            warningDialog.setOnYesClickListener(view1 -> {
                warningDialog.dismiss();
                ((MainPosPageActivity) getActivity()).onEditOrder(getString(R.string.without_reason));
            });
            warningDialog.setOnNoClickListener(view1 -> {
                warningDialog.dismiss();
            });
            warningDialog.setPositiveButtonText(getString(R.string.yes));
            warningDialog.setNegativeButtonText(getString(R.string.cancel));
            warningDialog.show();


        }

    }

    @Override
    public void openCancelAccsessDialog() {
        if (preferencesHelper.isCancelOrderProtected()) {
            AccessToCancelDialog accessToCancelDialog = new AccessToCancelDialog(getContext(), new AccessToCancelDialog.OnAccsessListner() {
                @Override
                public void accsessSuccess(String reason) {
                    ((MainPosPageActivity) getActivity()).onCancelOrder(reason); //TODO !!!!!!!!!!
                }

                @Override
                public void onBruteForce() {
                    presenter.onBruteForce();
                }
            }, preferencesHelper);
            accessToCancelDialog.show();
        } else {
            WarningDialog warningDialog = new WarningDialog(getActivity());
            warningDialog.setWarningMessage(getString(R.string.do_u_want_cancel_order));
            warningDialog.setOnYesClickListener(view1 -> {
                warningDialog.dismiss();
                ((MainPosPageActivity) getActivity()).onCancelOrder(getString(R.string.without_reason));
            });
            warningDialog.setOnNoClickListener(view1 -> {
                warningDialog.dismiss();
            });
            warningDialog.setPositiveButtonText(getString(R.string.yes));
            warningDialog.setNegativeButtonText(getString(R.string.cancel));
            warningDialog.show();
        }

    }


    @Override
    public void setOrderNumberToToolbar(Long orderNumber) {
        ((MainPosPageActivity) getActivity()).updateIndicator(orderNumber);
    }

    @Override
    public void onContinuePressed(Order order) {
        ((MainPosPageActivity) getActivity()).onContinueOrder(order);
    }

    @Override
    public void reprint(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        ((MainPosPageActivity) getActivity()).reprintOrder(order, databaseManager, preferencesHelper);
    }

}
