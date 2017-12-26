package com.jim.multipos.ui.mainpospage.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpLightButton;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductAdapter;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenter;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderListFragment extends BaseFragment implements OrderListView {
    @Inject
    OrderListPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    DatabaseManager databaseManager;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.llDiscount)
    LinearLayout llDiscount;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;

    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvDiscountAmount)
    TextView tvDiscountAmount;
    @BindView(R.id.tvServiceAmount)
    TextView tvServiceAmount;
    @BindView(R.id.tvBalanceDue)
    TextView tvBalanceDue;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvOrderProducts)
    RecyclerView rvOrderProducts;
    @Inject
    OrderProductAdapter orderProductAdapter;
    @BindView(R.id.lbChoiseCustomer)
    MpLightButton lbChooseCustomer;
    Activity activity;

    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvOrderProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderProducts.setAdapter(orderProductAdapter);
        ((SimpleItemAnimator) rvOrderProducts.getItemAnimator()).setSupportsChangeAnimations(false);

        lbChooseCustomer.setOnLightButtonClickListener(view1 -> {
            CustomerDialog customerDialog = new CustomerDialog(getContext(), databaseManager);
            customerDialog.show();
        });

        RxView.clicks(llPay)
                .subscribe(view1 -> {

                });
        RxView.clicks(llDiscount)
                .subscribe(view1 -> {
                    DiscountDialog discountDialog = new DiscountDialog();
                    discountDialog.setDiscounts(presenter.getDiscounts(), getResources().getStringArray(R.array.discount_amount_types_abr));
                    discountDialog.setOnDialogListener(new DiscountDialog.OnDialogListener() {
                        @Override
                        public List<Discount> getDiscounts() {
                            return presenter.getDiscounts();
                        }

                        @Override
                        public void addDiscount(double amount, String description, int amountType) {
                            presenter.addDiscount(amount, description, amountType);
                        }
                    });
                    discountDialog.show(getActivity().getSupportFragmentManager(), "discountDialog");
                });
        RxView.clicks(llPrintCheck)
                .subscribe(view1 -> {

                });
        RxView.clicks(llServiceFee)
                .subscribe(view1 -> {
                    ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog();
                    serviceFeeDialog.setServiceFee(presenter.getServiceFees());
                    serviceFeeDialog.setOnDialogListener(new ServiceFeeDialog.OnDialogListener() {
                        @Override
                        public List<ServiceFee> getServiceFees() {
                            return presenter.getServiceFees();
                        }

                        @Override
                        public void addServiceFee(double amount, String description, int amountType) {
                            presenter.addServiceFee(amount, description, amountType);
                        }
                    });
                    serviceFeeDialog.show(getActivity().getSupportFragmentManager(), "discountDialog");
                });
    }

}
