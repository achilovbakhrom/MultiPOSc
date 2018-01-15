package com.jim.multipos.ui.cash_management.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepFragment;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillDialog extends DialogFragment {


    public static final int FIRST_STEP = 0;
    public static final int SECOND_STEP = 1;
    public static final int THIRD_STEP = 2;

    @BindView(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @BindView(R.id.tvDescriptionOfStep)
    TextView tvDescriptionOfStep;
    @BindView(R.id.llReconcileOrder)
    LinearLayout llReconcileOrder;
    @BindView(R.id.llCloseAmount)
    LinearLayout llCloseAmount;
    @BindView(R.id.llToNext)
    LinearLayout llToNext;
    @BindView(R.id.csvReconcileOrder)
    MpCompletedStateView csvReconcileOrder;
    @BindView(R.id.csvCloseAmount)
    MpCompletedStateView csvCloseAmount;
    @BindView(R.id.csvToNext)
    MpCompletedStateView csvToNext;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.close_till_dialog, container, false);
        ButterKnife.bind(this, view);
        fragmentManager = getChildFragmentManager();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showFirstStep();
        setBackgrounds(FIRST_STEP);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
        tvDateAndTime.setText(simpleDateFormat.format(System.currentTimeMillis()));

        llReconcileOrder.setOnClickListener(view1 -> {
            showFirstStep();
            setBackgrounds(FIRST_STEP);
        });

        llCloseAmount.setOnClickListener(view12 -> {
            showSecondStep();
            setBackgrounds(SECOND_STEP);
        });


        llToNext.setOnClickListener(view13 -> {
            showThirdStep();
            setBackgrounds(THIRD_STEP);
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void setBackgrounds(int mode) {
        switch (mode) {
            case FIRST_STEP:
                llReconcileOrder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                llToNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                llCloseAmount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                tvDescriptionOfStep.setText("1 step of 3 steps for closing till");
                break;
            case SECOND_STEP:
                llReconcileOrder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                llToNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                llCloseAmount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                tvDescriptionOfStep.setText("2 step of 3 steps for closing till");
                break;
            case THIRD_STEP:
                llReconcileOrder.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                llToNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                llCloseAmount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
                tvDescriptionOfStep.setText("3 step of 3 steps for closing till");
                break;
        }
    }

    private void showFirstStep() {
        CloseTillFirstStepFragment firstStepFragment = (CloseTillFirstStepFragment) fragmentManager.findFragmentByTag(CloseTillFirstStepFragment.class.getName());
        CloseTillSecondStepFragment secondStepFragment = (CloseTillSecondStepFragment) fragmentManager.findFragmentByTag(CloseTillSecondStepFragment.class.getName());
        CloseTillThirdStepFragment thirdStepFragment = (CloseTillThirdStepFragment) fragmentManager.findFragmentByTag(CloseTillThirdStepFragment.class.getName());
        if (secondStepFragment != null && secondStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(secondStepFragment).commit();
        }
        if (thirdStepFragment != null && thirdStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(thirdStepFragment).commit();
        }
        if (firstStepFragment == null) {
            firstStepFragment = new CloseTillFirstStepFragment();
            addFragmentWithTag(firstStepFragment, CloseTillFirstStepFragment.class.getName());
        } else {
            fragmentManager.beginTransaction().show(firstStepFragment).commit();
        }
    }

    private void showSecondStep() {
        CloseTillFirstStepFragment firstStepFragment = (CloseTillFirstStepFragment) fragmentManager.findFragmentByTag(CloseTillFirstStepFragment.class.getName());
        CloseTillSecondStepFragment secondStepFragment = (CloseTillSecondStepFragment) fragmentManager.findFragmentByTag(CloseTillSecondStepFragment.class.getName());
        CloseTillThirdStepFragment thirdStepFragment = (CloseTillThirdStepFragment) fragmentManager.findFragmentByTag(CloseTillThirdStepFragment.class.getName());
        if (firstStepFragment != null && firstStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(firstStepFragment).commit();
        }
        if (thirdStepFragment != null && thirdStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(thirdStepFragment).commit();
        }
        if (secondStepFragment == null) {
            secondStepFragment = new CloseTillSecondStepFragment();
            addFragmentWithTag(secondStepFragment, CloseTillSecondStepFragment.class.getName());
        } else {
            fragmentManager.beginTransaction().show(secondStepFragment).commit();
        }
    }

    private void showThirdStep() {
        CloseTillFirstStepFragment firstStepFragment = (CloseTillFirstStepFragment) fragmentManager.findFragmentByTag(CloseTillFirstStepFragment.class.getName());
        CloseTillSecondStepFragment secondStepFragment = (CloseTillSecondStepFragment) fragmentManager.findFragmentByTag(CloseTillSecondStepFragment.class.getName());
        CloseTillThirdStepFragment thirdStepFragment = (CloseTillThirdStepFragment) fragmentManager.findFragmentByTag(CloseTillThirdStepFragment.class.getName());
        if (secondStepFragment != null && secondStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(secondStepFragment).commit();
        }
        if (firstStepFragment != null && firstStepFragment.isVisible()) {
            fragmentManager.beginTransaction().hide(firstStepFragment).commit();
        }
        if (thirdStepFragment == null) {
            thirdStepFragment = new CloseTillThirdStepFragment();
            addFragmentWithTag(thirdStepFragment, CloseTillThirdStepFragment.class.getName());
        } else {
            fragmentManager.beginTransaction().show(thirdStepFragment).commit();
        }
    }

    private void addFragmentWithTag(Fragment fragment, String tag) {
        if (fragment.isAdded()) return;
        fragmentManager.beginTransaction()
                .add(R.id.flStepContainer, fragment, tag)
                .commit();
    }
}
