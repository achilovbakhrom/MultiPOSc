package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.RxBusLocal;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by user on 07.10.17.
 */

public class AccountFragment extends BaseFragment {
    @Inject
    RxBusLocal rxBusLocal;
    @BindView(R.id.etAccountName)
    EditText etAccountName;
    @BindView(R.id.spType)
    MpSpinner spType;
    @BindView(R.id.spCirculation)
    MpSpinner spCirculation;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.rvSystemAccounts)
    RecyclerView rvSystemAccounts;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    @Override
    protected int getLayout() {
        return R.layout.account_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        RxView.clicks(btnNext).subscribe(aVoid -> {
        });

        RxView.clicks(ivAdd).subscribe(aVoid -> {
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
        });
    }

    @Override
    protected void rxConnections() {

    }
}
