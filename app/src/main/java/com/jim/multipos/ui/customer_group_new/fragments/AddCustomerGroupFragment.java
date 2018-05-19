package com.jim.multipos.ui.customer_group_new.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.UIUtils;
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
    MpEditText etGroupName;
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
            if (((CustomerGroupActivity) getActivity()).getPresenter().hasChanges()) {
                UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no), getString(R.string.discard_changes),
                        getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                getActivity().onBackPressed();
                            }

                            @Override
                            public void onNegativeButtonClicked() {

                            }
                        });
            } else getActivity().onBackPressed();

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
                    if (((CustomerGroupActivity) getActivity()).getPresenter().hasChanges()) {
                        if (!((CustomerGroupActivity) getActivity()).getPresenter().isCustomerGroupExists(etGroupName.getText().toString())) {
                            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                                    getString(R.string.update), getString(R.string.do_you_want_update_customer_group),
                                    new UIUtils.AlertListener() {
                                        @Override
                                        public void onPositiveButtonClicked() {
                                            editCustomerGroup.setName(etGroupName.getText().toString());
                                            editCustomerGroup.setIsActive(chbActive.isChecked());
                                            ((CustomerGroupActivity) getActivity()).getPresenter().updateCustomerGroup(editCustomerGroup);
                                            setDefaultState();
                                        }

                                        @Override
                                        public void onNegativeButtonClicked() {

                                        }
                                    });

                        } else if (editCustomerGroup.getName().equals(etGroupName.getText().toString())) {
                            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                                    getString(R.string.update), getString(R.string.do_you_want_update_customer_group),
                                    new UIUtils.AlertListener() {
                                        @Override
                                        public void onPositiveButtonClicked() {
                                            editCustomerGroup.setName(etGroupName.getText().toString());
                                            editCustomerGroup.setIsActive(chbActive.isChecked());
                                            ((CustomerGroupActivity) getActivity()).getPresenter().updateCustomerGroup(editCustomerGroup);
                                            setDefaultState();
                                        }

                                        @Override
                                        public void onNegativeButtonClicked() {

                                        }
                                    });
                        } else etGroupName.setError(getString(R.string.customer_group_name_exists));
                    } else {
                        UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                                getString(R.string.update), getString(R.string.do_you_want_update_customer_group),
                                new UIUtils.AlertListener() {
                                    @Override
                                    public void onPositiveButtonClicked() {
                                        editCustomerGroup.setName(etGroupName.getText().toString());
                                        editCustomerGroup.setIsActive(chbActive.isChecked());
                                        ((CustomerGroupActivity) getActivity()).getPresenter().updateCustomerGroup(editCustomerGroup);
                                        setDefaultState();
                                    }

                                    @Override
                                    public void onNegativeButtonClicked() {

                                    }
                                });
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
        if (editCustomerGroup != null) {
            if (customerGroup.getId() != editCustomerGroup.getId()) {
                isEditMode = true;
                editCustomerGroup = customerGroup;
                etGroupName.setText(customerGroup.getName());
                chbActive.setChecked(customerGroup.getIsActive());
                btnDelete.setVisibility(View.VISIBLE);
                btnSave.setText(R.string.save);
            }
        } else {
            isEditMode = true;
            editCustomerGroup = customerGroup;
            etGroupName.setText(customerGroup.getName());
            chbActive.setChecked(customerGroup.getIsActive());
            btnDelete.setVisibility(View.VISIBLE);
            btnSave.setText(R.string.save);
        }

        etGroupName.setSelection(etGroupName.getText().length());
    }

    public String getCustomerGroupName() {
        return etGroupName.getText().toString();
    }

    public boolean getCustomerGroupIsActive() {
        return chbActive.isChecked();
    }
}
