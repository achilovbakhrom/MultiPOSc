package com.jim.multipos.ui.signing.sign_up.view;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.R;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.sign_up.presenter.RegistrationConfirmPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;

import static com.jim.multipos.utils.BundleConstants.ORG_ADDRESS;
import static com.jim.multipos.utils.BundleConstants.ORG_CODE;
import static com.jim.multipos.utils.BundleConstants.ORG_EMAIL;
import static com.jim.multipos.utils.BundleConstants.ORG_NAME;
import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationConfirmFragment extends BaseFragment implements RegistrationConfirmView {
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    RegistrationConfirmPresenter presenter;
    @Inject
    SignActivity activity;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnConfirm)
    MpButton btnConfirm;
    @BindView(R.id.ivEditDetails)
    ImageView ivEditDetails;
    @NotEmpty(messageId = R.string.enter_name)
    @BindView(R.id.tvOrgName)
    TextView tvOrgName;
    @NotEmpty(messageId = R.string.enter_organization_email)
    @RegExp(value = EMAIL, messageId = R.string.invalid)
    @BindView(R.id.tvOrgEmail)
    TextView tvOrgEmail;
    @NotEmpty(messageId = R.string.enter_address)
    @BindView(R.id.tvOrgAddress)
    TextView tvOrgAddress;
    @NotEmpty(messageId = R.string.enter_zip_code)
    @BindView(R.id.tvOrgZipCode)
    TextView tvOrgZipCode;

    @Override
    protected int getLayout() {
        return R.layout.reg_second_page_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        if (getArguments() != null) {
//            tvOrgName.setText(getArguments().getString(ORG_NAME));
//            tvOrgName.setPaintFlags(tvOrgName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//            tvOrgAddress.setText(getArguments().getString(ORG_ADDRESS));
//            tvOrgEmail.setText(getArguments().getString(ORG_EMAIL));
//            tvOrgZipCode.setText(getArguments().getString(ORG_CODE));
//        }
    }

    @Override
    protected void rxConnections() {

    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        ((SignActivity) getContext()).openSignUp();
    }

    @OnClick(R.id.btnConfirm)
    public void onConfirm() {
        Intent intent = new Intent(getContext(), FirstConfigureActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivEditDetails)
    public void onEdit() {
    }
}
