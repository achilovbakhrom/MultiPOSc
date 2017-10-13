package com.jim.multipos.ui.mainpospage.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.utils.VibrateManager;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListFragment extends Fragment {//BaseFragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
//        this.getComponent(MainPosPageActivityComponent.class).inject(this);

        ButterKnife.bind(this, view);
        setClickEffects();
        activity = getActivity();
        view.findViewById(R.id.lbChoiseCustomer).setOnClickListener(view1->{

        });
        view.findViewById(R.id.lbHoldOrder).setOnClickListener(view1->{

        });
        view.findViewById(R.id.lbCancelOrder).setOnClickListener(view1->{

        });
        RxView.clicks(llPay)
                .subscribe(view1->{

                });
        RxView.clicks(llDiscount)
                .subscribe(view1->{

                });
        RxView.clicks(llPrintCheck)
                .subscribe(view1->{

                });
        RxView.clicks(llServiceFee)
                .subscribe(view1->{

                });
        return view;
    }
    boolean pressed = false;

    public void setClickEffects(){
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
