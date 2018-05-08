package com.jim.multipos.ui.settings.pos_details;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class PosDetailsFragment extends BaseFragment implements PosDetailsView {

    @Inject
    PosDetailsPresenter presenter;
    @BindView(R.id.etPosId)
    MpEditText etPosId;
    @BindView(R.id.etPosAddress)
    MpEditText etPosAddress;
    @BindView(R.id.etPosAlias)
    MpEditText etPosAlias;
    @BindView(R.id.etPosPhone)
    MpEditText etPosPhone;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.btnSave)
    MpButton btnSave;

    @Override
    protected int getLayout() {
        return R.layout.pos_details_settings;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.initDetails();
        btnRevert.setOnClickListener(view -> {
            presenter.initDetails();
        });
        etPosPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        btnSave.setOnClickListener(view -> {
            presenter.savePosDetails(etPosId.getText().toString(), etPosAlias.getText().toString(), etPosAddress.getText().toString(), etPosPhone.getText().toString());
        });
    }

    @Override
    public void fillPosDetails(String posId, String alias, String address, String phone) {
        etPosId.setText(posId);
        etPosAlias.setText(alias);
        etPosAddress.setText(address);
        etPosPhone.setText(phone);
    }
}
