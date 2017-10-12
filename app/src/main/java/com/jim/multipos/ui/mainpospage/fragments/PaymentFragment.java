package com.jim.multipos.ui.mainpospage.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpPaymentField;
import com.jim.mpviews.MpList;
import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;

import java.util.ArrayList;
import java.util.List;

import static com.jim.mpviews.utils.Constants.PAYMENT_MODE;

/**
 * Created by developer on 22.08.2017.
 */

public class PaymentFragment extends Fragment {
    MpList mpList;
    TextView tvPay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_page_payment_fragment, container, false);
        mpList = (MpList) view.findViewById(R.id.mpLPaymentList);
        tvPay = (TextView) view.findViewById(R.id.tvPay);

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
        return view;
    }
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
}
