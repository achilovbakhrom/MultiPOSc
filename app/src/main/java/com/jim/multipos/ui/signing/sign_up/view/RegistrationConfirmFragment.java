package com.jim.multipos.ui.signing.sign_up.view;

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
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.sign_up.presenter.RegistrationConfirmPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jim.multipos.utils.BundleConstants.ORG_ADDRESS;
import static com.jim.multipos.utils.BundleConstants.ORG_CODE;
import static com.jim.multipos.utils.BundleConstants.ORG_EMAIL;
import static com.jim.multipos.utils.BundleConstants.ORG_NAME;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationConfirmFragment extends BaseFragment { //implements RegistrationConfirmFragmentView {
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
    @BindView(R.id.tvOrgName)
    TextView tvOrgName;
    @BindView(R.id.tvOrgEmail)
    TextView tvOrgEmail;
    @BindView(R.id.tvOrgAddress)
    TextView tvOrgAddress;
    @BindView(R.id.tvOrgZipCode)
    TextView tvOrgZipCode;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reg_second_page_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        this.getComponent(LoginActivityComponent.class).inject(this);
//        presenter.init(this);
        if (getArguments() != null) {
            tvOrgName.setText(getArguments().getString(ORG_NAME));
            tvOrgName.setPaintFlags(tvOrgName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvOrgAddress.setText(getArguments().getString(ORG_ADDRESS));
            tvOrgEmail.setText(getArguments().getString(ORG_EMAIL));
            tvOrgZipCode.setText(getArguments().getString(ORG_CODE));
        }
        return rootView;
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void rxConnections() {

    }

    @OnClick(R.id.btnBack)
    public void back() {
        presenter.back();

    }

    @OnClick(R.id.btnConfirm)
    public void confirm() {
        presenter.confirm();
    }

    @OnClick(R.id.ivEditDetails)
    public void edit() {
        presenter.back();
    }


    public void onConfirm() {
//        activity.openFirstConfigure();
    }


    public void checkToken() {

    }


    public void onBack() {
//        activity.popFromBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
