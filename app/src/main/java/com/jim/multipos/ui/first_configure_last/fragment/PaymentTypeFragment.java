package com.jim.multipos.ui.first_configure_last.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.first_configure_last.ChangeableContent;
import com.jim.multipos.ui.first_configure_last.CompletionMode;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenter;
import com.jim.multipos.ui.first_configure_last.adapter.PaymentTypeAdapter;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Achilov Bakhrom on 10/18/17.
 */

public class PaymentTypeFragment extends BaseFragment implements ChangeableContent {

    @NotEmpty(messageId = R.string.enter_payment_type_name)
    @BindView(R.id.etPaymentTypeName)
    MpEditText paymentTypeName;
    @BindView(R.id.spAccount)
    MPosSpinner accounts;
    @BindView(R.id.tvCurrency)
    TextView currency;
    @BindView(R.id.btnNext)
    MpButton next;
    private CompletionMode mode = CompletionMode.NEXT;
    @BindView(R.id.rvSystemPaymentType)
    RecyclerView paymentTypes;
    @BindView(R.id.llPaymentTypeWarning)
    LinearLayout warning;

    @Override
    protected int getLayout() {
        return R.layout.payment_type_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            setMode((CompletionMode) getArguments().getSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY));
        }
        fillData();
    }

    private void fillData() {
        accounts.setAdapter(((FirstConfigureActivity) getContext()).getPresenter().getSpinnerAccounts());
        currency.setText(((FirstConfigureActivity) getContext()).getPresenter().getCurrencyName());
        paymentTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        PaymentTypeAdapter adapter = new PaymentTypeAdapter(((FirstConfigureActivity)getContext()).getPresenter().getPaymentTypes());
        adapter.setListener((position, item) -> {
            UIUtils.closeKeyboard(accounts, getContext());
            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no), getString(R.string.warning_deleting_payment_type),
                    getString(R.string.warning_deleting_payment_type_message),
                    new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            ((FirstConfigureActivity) getContext()).getPresenter().deletePaymentType(item);
                            adapter.removeItem(position);
                        }

                        @Override
                        public void onNegativeButtonClicked() {}
                    });
        });
        paymentTypes.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hasAnyPaymentType()) warning.setVisibility(View.GONE);
        else warning.setVisibility(View.VISIBLE);
        if (hidden) {
            ((FirstConfigureActivity) getContext()).getPresenter().checkPaymentTypeCorrection();
        }
        else {
            fillData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY, mode);
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        switch (mode) {
            case NEXT:
                next.setText(R.string.next);
                break;
            case FINISH:
                next.setText(R.string.finish);
                break;
        }
    }

    @OnClick(value = {R.id.ivAdd, R.id.btnRevert, R.id.btnNext})
    public void buttonClick(View view ) {
        switch (view.getId()) {
            case R.id.ivAdd:
                UIUtils.closeKeyboard(view, getContext());
                if (isValid()) {
                    FirstConfigurePresenter presenter = ((FirstConfigureActivity) getContext()).getPresenter();
                    if (!presenter.isPayemntTypeNameExists(paymentTypeName.getText().toString())) {
                        PaymentType paymentType = ((FirstConfigureActivity) getContext())
                                .getPresenter()
                                .addPaymentType(paymentTypeName.getText().toString(),
                                        accounts.getSelectedPosition());
                        if (paymentType != null) {
                            ((PaymentTypeAdapter) paymentTypes.getAdapter()).addItem(paymentType);
                        }
                    } else {
                        paymentTypeName.setError(getString(R.string.payment_type_name_exists));
                    }
                    paymentTypeName.setText("");
                }
                break;
            case R.id.btnRevert:
                ((FirstConfigureActivity) getContext()).getPresenter().openCurrency();
                break;
            case R.id.btnNext:
                if (hasAnyPaymentType()) {
                    if (mode == CompletionMode.NEXT) {
                        warning.setVisibility(View.GONE);
                        FirstConfigureActivity activity = (FirstConfigureActivity) getContext();
                        activity.getPresenter().setCompletedForFragment(getClass().getName(), true);
                        activity.getPresenter().openPOSDetails();
                        activity.changeState(FirstConfigurePresenter.PAYMENT_TYPE_POSITION, MpCompletedStateView.COMPLETED_STATE);
                    } else {
                        ((FirstConfigureActivity) getActivity()).openLockScreen();
                    }
                } else {
                    warning.setVisibility(View.VISIBLE);
                    ((FirstConfigureActivity) getContext()).changeState(FirstConfigurePresenter.PAYMENT_TYPE_POSITION, MpCompletedStateView.WARNING_STATE);
                }
                break;
        }
    }

    public boolean hasAnyPaymentType() {
        return !((FirstConfigureActivity) getContext()).getPresenter().getPaymentTypes().isEmpty();
    }
}
