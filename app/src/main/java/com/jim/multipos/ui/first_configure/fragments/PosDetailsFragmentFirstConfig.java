package com.jim.multipos.ui.first_configure.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.PosFragmentPresenter;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PosDetailsFragmentFirstConfig extends Fragment { //BaseFragmentFirstConfig implements PosDetailsFragmentView {
    @Inject
    PosFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    @BindView(R.id.etPosId)
    EditText etPosId;
    @BindView(R.id.etAlias)
    EditText etAlias;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    private String posID;
    private String alias;
    private String address;
    private String password;
    private String repeatPassword;

    public PosDetailsFragmentFirstConfig() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pos_details_fragment, container, false);
//        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
//        presenter.init(this);
        ButterKnife.bind(this, view);


        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        if (posID != null) {
            etPosId.setText(posID);
        }

        if (alias != null) {
            etAlias.setText(alias);
        }

        if (address != null) {
            etAddress.setText(address);
        }

        if (password != null) {
            etPassword.setText(password);
        }

        if (repeatPassword != null) {
            etRepeatPassword.setText(repeatPassword);
        }

        RxView.clicks(btnNext).subscribe(aVoid -> {
            presenter.openNextFragment(getData());
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        getDataFromView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        hideKeyboard();
    }

    public void showPosIdError(String error) {
        etPosId.setError(error);
    }

    public void showAliasError(String error) {
        etAlias.setError(error);
    }

    public void showAddressError(String error) {
        etAddress.setError(error);
    }

    public void showPasswordError(String error) {
        etPassword.setError(error);
    }

    public boolean checkData() {
        return presenter.isCompleteData(getData());
    }

    public void showRepeatPasswordError(String error) {
        etRepeatPassword.setError(error);
    }

    public void openNextFragment() {
        activity.openNextFragment();
    }

    public HashMap<String, String> getData() {
        getDataFromView();

        HashMap<String, String> data = new HashMap<>();

        data.put("posId", posID);
        data.put("alias", alias);
        data.put("address", address);
        data.put("password", password);
        data.put("repeatPassword", repeatPassword);

        return data;
    }

    private void getDataFromView() {
        posID = etPosId.getText().toString();
        alias = etAlias.getText().toString();
        address = etAddress.getText().toString();
        password = etPassword.getText().toString();
        repeatPassword = etRepeatPassword.getText().toString();
    }

    public void saveData() {
        presenter.saveData();
    }

    public void clearViews() {
        etPosId.setText("");
        etAlias.setText("");
        etAddress.setText("");
        etPassword.setText("");
        etRepeatPassword.setText("");
    }
}
