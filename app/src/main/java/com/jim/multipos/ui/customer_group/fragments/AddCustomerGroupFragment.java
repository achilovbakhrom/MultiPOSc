package com.jim.multipos.ui.customer_group.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customer_group.adapters.ServiceFeeSpinnerAdapter;
import com.jim.multipos.ui.customer_group.di.CustomerGroupActivityComponent;
import com.jim.multipos.ui.customer_group.presenters.AddCustomerGroupFragmentPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerGroupFragment extends BaseFragment implements AddCustomerGroupFragmentView {
    @Inject
    AddCustomerGroupFragmentPresenter presenter;
    @BindView(R.id.etGroupName)
    EditText etGroupName;
    @BindView(R.id.spDiscounts)
    MpSpinner spDiscounts;
    @BindView(R.id.spServiceFee)
    MpSpinner spServiceFee;
    @BindView(R.id.chbTaxFree)
    MpCheckbox chbTaxFree;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.chbDiscountsAutoApply)
    MpCheckbox chbDiscountsAutoApply;
    @BindView(R.id.chbServiceFeeAutoApply)
    MpCheckbox chbServiceFeeAutoApply;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnMembers)
    MpButton btnMembers;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    private Unbinder unbinder;

    public AddCustomerGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_customer_group_fragment, container, false);

        getComponent(CustomerGroupActivityComponent.class).inject(this);
        presenter.init(this);
        unbinder = ButterKnife.bind(this, view);

        presenter.getServiceFees();

        RxView.clicks(btnSave).subscribe(o -> {
            String groupName = etGroupName.getText().toString();
            boolean isTaxFree = chbTaxFree.isCheckboxChecked();
            boolean isActive = chbActive.isCheckboxChecked();
            int serviceFeePosition = spServiceFee.selectedItem();

            presenter.addCustomerGroup(groupName, 1, serviceFeePosition, isTaxFree, isActive);
        });

        RxView.clicks(btnMembers).subscribe(o -> {
            presenter.getCustomerGroup();
        });

        RxView.clicks(btnCancel).subscribe(o -> {
            ((CustomerGroupActivity) getActivity()).closeActivity();
        });

        return view;
    }

    @Override
    public void showServiceFees(List<ServiceFee> serviceFees) {
        spServiceFee.setAdapter(new ServiceFeeSpinnerAdapter(this.getContext(), R.layout.item_spinner, serviceFees));
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void showGroupNameError(String message) {
        etGroupName.setError(message);
    }

    @Override
    public void showCustomerGroup(CustomerGroup customerGroup) {
        //TODO edit -> ADD AUTO APPLY
        etGroupName.setText(customerGroup.getName());
        chbTaxFree.setChecked(customerGroup.getIsTaxFree());
        chbActive.setChecked(customerGroup.getIsActive());
    }

    @Override
    public void clearViews() {
        etGroupName.setText("");
    }

    @Override
    public void requestFocus() {
        etGroupName.requestFocus();
    }

    @Override
    public void showMembers() {
        ((CustomerGroupActivity) getActivity()).openCustomerGroupsFragment();
    }
}
