package com.jim.multipos.ui.customer_group_new.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.validator.MultipleCallback;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerGroupFragment extends BaseFragment {
    @NotEmpty(messageId = R.string.enter_group_name)
    @BindView(R.id.etGroupName)
    EditText etGroupName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnMembers)
    MpButton btnMembers;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.btnDelete)
    MpButton btnDelete;
    private boolean isEditMode = false;
    private CustomerGroup editCustomerGroup;

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.add_customer_group_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        btnSave.setText(R.string.add);
        btnDelete.setVisibility(View.GONE);
        RxView.clicks(btnBack).subscribe(o -> {
            getActivity().onBackPressed();
        });
        RxView.clicks(btnMembers).subscribe(o -> {
            if (isEditMode) {
                ((CustomerGroupActivity) getActivity()).showCustomersFragment(editCustomerGroup.getId());
            } else {
                ((CustomerGroupActivity) getActivity()).showCustomersFragment(-1);
            }
        });
        RxView.clicks(btnSave).subscribe(o -> {
            if (FormValidator.validate(this, new MultipleCallback())) {
                if (isEditMode) {
                    if (!editCustomerGroup.getName().equals(etGroupName.getText().toString()) && !((CustomerGroupActivity) getActivity()).getPresenter().isCustomerGroupExists(etGroupName.getText().toString())) {
                        editCustomerGroup.setName(etGroupName.getText().toString());
                        editCustomerGroup.setIsActive(chbActive.isChecked());
                        ((CustomerGroupActivity) getActivity()).getPresenter().updateCustomerGroup(editCustomerGroup);
                        setDefaultState();
                    } else if (editCustomerGroup.getName().equals(etGroupName.getText().toString())) {
                        editCustomerGroup.setName(etGroupName.getText().toString());
                        editCustomerGroup.setIsActive(chbActive.isChecked());
                        ((CustomerGroupActivity) getActivity()).getPresenter().updateCustomerGroup(editCustomerGroup);
                        setDefaultState();
                    } else {
                        etGroupName.setError(getString(R.string.customer_group_name_exists));
                    }
                } else {
                    if (!((CustomerGroupActivity) getActivity()).getPresenter().isCustomerGroupExists(etGroupName.getText().toString())) {
                        ((CustomerGroupActivity) getActivity()).getPresenter().addCustomerGroup(etGroupName.getText().toString(), chbActive.isChecked());
                        etGroupName.setText("");
                        btnSave.setText(R.string.add);
                        btnDelete.setVisibility(View.GONE);
                    } else {
                        etGroupName.setError(getString(R.string.customer_group_name_exists));
                    }
                }
            }
        });

        RxView.clicks(btnDelete).subscribe(o -> {
            WarningDialog warningDialog = new WarningDialog(getContext());
            warningDialog.setWarningMessage(getString(R.string.do_you_want_delete));
            warningDialog.setOnYesClickListener(view -> {
                ((CustomerGroupActivity) getActivity()).getPresenter().removeCustomerGroup(editCustomerGroup);
                /*btnSave.setText(R.string.add);
                etGroupName.setText("");
                btnDelete.setVisibility(View.GONE);
                isEditMode = false;
                editCustomerGroup = null;*/
                warningDialog.dismiss();
            });
            warningDialog.setOnNoClickListener(v -> {
                warningDialog.dismiss();
            });
            warningDialog.show();
        });
    }

    public void setDefaultState() {
        btnSave.setText(R.string.add);
        etGroupName.setText("");
        btnDelete.setVisibility(View.GONE);
        isEditMode = false;
        editCustomerGroup = null;
    }

    public void clearView() {
        setDefaultState();
    }

    public void showCustomerGroup(CustomerGroup customerGroup) {
        isEditMode = true;
        editCustomerGroup = customerGroup;
        etGroupName.setText(customerGroup.getName());
        chbActive.setChecked(customerGroup.getIsActive());
        btnDelete.setVisibility(View.VISIBLE);
        btnSave.setText(R.string.save);
    }
}
