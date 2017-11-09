package com.jim.multipos.ui.customer_group_new.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;
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

    @Override
    protected int getLayout() {
        return R.layout.add_customer_group_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
