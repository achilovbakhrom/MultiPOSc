package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpList;
import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.mainpospage.dialogs.AddDebtDialog;
import com.jim.multipos.ui.mainpospage.presenter.PaymentPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 22.08.2017.
 */

public class PaymentFragment extends BaseFragment implements PaymentView {
    @Inject
    PaymentPresenter presenter;
    @BindView(R.id.llDebtBorrow)
    LinearLayout llDebtBorrow;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.mpLPaymentList)
    MpList mpList;

    List<PaymentTypeWithService> paymentTypes;
    public void ititArray(){
        paymentTypes = new ArrayList<>();
        paymentTypes.add(new PaymentTypeWithService("Dollar","+30%"));
        paymentTypes.add(new PaymentTypeWithService("Cash Uzs",""));
        paymentTypes.add(new PaymentTypeWithService("Bank Uzs","+10%"));
        paymentTypes.add(new PaymentTypeWithService("Visa Card",""));
        paymentTypes.add(new PaymentTypeWithService("Master card","+5%"));
        paymentTypes.add(new PaymentTypeWithService("Asia Alians",""));
    }

    @Override
    protected int getLayout() {
        return R.layout.main_page_payment_fragment;
    }
    public void refreshData(){

    }
    @Override
    protected void init(Bundle savedInstanceState) {

        tvPay.setOnTouchListener((vieww, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrateManager.startVibrate(getContext(),50);
                    tvPay.setBackgroundResource(R.drawable.big_btn_pressed);
                    break;
                case MotionEvent.ACTION_UP:
                    tvPay.setBackgroundResource(R.drawable.big_btn);
                    break;
            }
            return false;
        });
        tvPay.setOnClickListener(view1 -> {});
        ititArray();
        mpList.setPayments(paymentTypes);
        mpList.setOnPaymentClickListner(position -> {
            Log.d("paymenttest", "onCreateView: "+position);
        });

        llDebtBorrow.setOnClickListener(view12 -> {
            presenter.onDebtBorrowClicked();
        });
    }

    @Override
    public void openAddDebtDialog(DatabaseManager databaseManager) {
        AddDebtDialog dialog = new AddDebtDialog(getContext(),null, databaseManager, null, debt -> {});
        dialog.show();
    }
}
