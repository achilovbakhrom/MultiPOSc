package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.presenter.CloseTillDialogFragmentPresenter;
import com.jim.multipos.ui.first_configure_last.CompletionMode;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.mpviews.MpCompletedStateView.COMPLETED_STATE;
import static com.jim.mpviews.MpCompletedStateView.WARNING_STATE;
import static com.jim.multipos.ui.cash_management.CashManagementActivity.TILL_ID;
import static com.jim.multipos.ui.cash_management.presenter.CloseTillDialogFragmentPresenterImpl.FIRST_KEY;
import static com.jim.multipos.ui.cash_management.presenter.CloseTillDialogFragmentPresenterImpl.SECOND_KEY;
import static com.jim.multipos.ui.cash_management.presenter.CloseTillDialogFragmentPresenterImpl.THIRD_KEY;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillDialogFragment extends BaseFragment implements CloseTillDialogFragmentView {


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
    MpCompletedStateView csvFirstStep;
    @BindView(R.id.csvCloseAmount)
    MpCompletedStateView csvSecondStep;
    @BindView(R.id.csvToNext)
    MpCompletedStateView csvThirdStep;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    @Inject
    CashManagementConnection connection;
    @Inject
    CloseTillDialogFragmentPresenter presenter;

    private FragmentManager fragmentManager;
    private int currentFragment = FIRST_STEP;
    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.close_till_dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null){
            presenter.setTill(getArguments().getLong(TILL_ID));
        }
        connection.setCloseTillDialogView(this);
        fragmentManager = getChildFragmentManager();
        presenter.initData();
        showFirstStep();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
        tvDateAndTime.setText(simpleDateFormat.format(System.currentTimeMillis()));
        llReconcileOrder.setOnClickListener(view1 -> {
            connection.checkFirstStepCompletion();
            presenter.showFirstStep();
        });

        llCloseAmount.setOnClickListener(view2 -> {
            connection.checkSecondStepCompletion();
            presenter.showSecondStep();
        });

        llToNext.setOnClickListener(view3 -> {
            connection.checkThirdStepCompletion();
            presenter.showThirdStep();
        });

        btnNext.setOnClickListener(view4 -> {
            switch (currentFragment) {
                case FIRST_STEP:
                    connection.checkFirstStepCompletion();
                    break;
                case SECOND_STEP:
                    connection.checkSecondStepCompletion();
                    break;
                case THIRD_STEP:
                    connection.checkThirdStepCompletion();
                    break;
            }

            if (this.mode == CompletionMode.NEXT) {
                switch (currentFragment) {
                    case FIRST_STEP:
                        presenter.showSecondStep();
                        break;
                    case SECOND_STEP:
                        presenter.showThirdStep();
                        break;
                    case THIRD_STEP:
                        presenter.showFirstStep();
                        break;
                }
            } else {
               presenter.checkAllStepsCompletion();
            }
        });

        btnRevert.setOnClickListener(view -> closeTillDialog());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCloseTillDialogView(null);
    }

    @Override
    public void setFirstStepCompletionStatus(boolean status) {
        presenter.putCompletion(FIRST_KEY, status);
        if (status) {
            csvFirstStep.setState(COMPLETED_STATE);
        } else {
            csvFirstStep.setState(WARNING_STATE);
        }
    }

    @Override
    public void setSecondStepCompletionStatus(boolean status) {
        presenter.putCompletion(SECOND_KEY, status);
        if (status) {
            csvSecondStep.setState(COMPLETED_STATE);
        } else {
            csvSecondStep.setState(WARNING_STATE);
        }
    }

    @Override
    public void setThirdStepCompletionStatus(boolean status) {
        presenter.putCompletion(THIRD_KEY, status);
        if (status) {
            csvThirdStep.setState(COMPLETED_STATE);
        } else {
            csvThirdStep.setState(WARNING_STATE);
        }
    }

    @Override
    public void initAllSteps() {
        CloseTillFirstStepFragment firstStepFragment = new CloseTillFirstStepFragment();
        addFragmentWithTag(firstStepFragment, CloseTillFirstStepFragment.class.getName());
        CloseTillSecondStepFragment secondStepFragment = new CloseTillSecondStepFragment();
        addFragmentWithTag(secondStepFragment, CloseTillSecondStepFragment.class.getName());
        CloseTillThirdStepFragment thirdStepFragment = new CloseTillThirdStepFragment();
        addFragmentWithTag(thirdStepFragment, CloseTillThirdStepFragment.class.getName());
        fragmentManager.beginTransaction().hide(firstStepFragment).commit();
        fragmentManager.beginTransaction().hide(secondStepFragment).commit();
        fragmentManager.beginTransaction().hide(thirdStepFragment).commit();
    }

    @Override
    public void setCompletionMode(CompletionMode mode) {
        this.mode = mode;
        if (mode == CompletionMode.FINISH) {
            btnNext.setText(getContext().getResources().getString(R.string.finish));
        } else {
            btnNext.setText(getContext().getResources().getString(R.string.next));
        }
    }

    @Override
    public void showFirstStep() {
        setBackgrounds(FIRST_STEP);
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

    @Override
    public void showSecondStep() {
        setBackgrounds(SECOND_STEP);
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

    @Override
    public void showThirdStep() {
        setBackgrounds(THIRD_STEP);
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

    @Override
    public void setSecondStepData(List<TillManagementOperation> operations) {
        presenter.setSecondStepData(operations);
    }

    @Override
    public void setThirdStepData(List<TillManagementOperation> operations) {
        presenter.setThirdStepData(operations);
    }

    @Override
    public void setCompleteStatus(boolean status) {
        if (status){
            connection.collectData();
            presenter.closeTill();
        }
    }

    @Override
    public void closeTillDialog() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void setTillStatus(int status) {
        connection.setTillStatus(status);
    }

    private void addFragmentWithTag(Fragment fragment, String tag) {
        if (fragment.isAdded()) return;
        fragmentManager.beginTransaction()
                .add(R.id.flStepContainer, fragment, tag)
                .commit();
    }

    private void setBackgrounds(int currentFragment) {
        this.currentFragment = currentFragment;
        switch (currentFragment) {
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
}
