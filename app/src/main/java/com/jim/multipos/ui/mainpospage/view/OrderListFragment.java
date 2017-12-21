package com.jim.multipos.ui.mainpospage.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenter;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OrderListFragment extends BaseFragment implements OrderListView {//BaseFragment {
    @Inject
    OrderListPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.llDiscount)
    LinearLayout llDiscount;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;

    Activity activity;

    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //setClickEffects();

        /*view.findViewById(R.id.lbChoiseCustomer).setOnClickListener(view1 -> {

        });
        view.findViewById(R.id.lbHoldOrder).setOnClickListener(view1 -> {

        });
        view.findViewById(R.id.lbCancelOrder).setOnClickListener(view1 -> {

        });*/

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
                        public void addDiscount(double amount, String description, String amountType) {
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
                        public void addServiceFee(double amount, String description, String amountType) {
                            presenter.addServiceFee(amount, description, amountType);
                        }
                    });
                    serviceFeeDialog.show(getActivity().getSupportFragmentManager(), "discountDialog");
                });
    }

    boolean pressed = false;

    public void setClickEffects() {
        pressed = false;

        llPay.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.performClick();
                    if (!pressed) {
                        VibrateManager.startVibrate(getContext(), 50);
                        pressed = true;
                    }
                    llPay.setBackgroundResource(R.drawable.light_gradient_revice);
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    llPay.setBackgroundResource(R.drawable.light_gradient);

                    return false;
            }
            return false;
        });

        llDiscount.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.performClick();
                    if (!pressed) {
                        VibrateManager.startVibrate(getContext(), 50);

                        pressed = true;
                    }
                    llDiscount.setBackgroundColor(ContextCompat.getColor(activity, R.color.pressedWhite));
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    llDiscount.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));

                    return false;
            }
            return false;
        });

        llServiceFee.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.performClick();
                    if (!pressed) {
                        VibrateManager.startVibrate(getContext(), 50);

                        pressed = true;
                    }
                    llServiceFee.setBackgroundColor(ContextCompat.getColor(activity, R.color.pressedWhite));
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    llServiceFee.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));

                    return false;
            }
            return false;
        });

        llPrintCheck.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.performClick();
                    if (!pressed) {
                        VibrateManager.startVibrate(getContext(), 50);

                        pressed = true;
                    }
                    llPrintCheck.setBackgroundColor(ContextCompat.getColor(activity, R.color.pressedWhite));
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    llPrintCheck.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));

                    return false;
            }
            return false;
        });
    }
}
