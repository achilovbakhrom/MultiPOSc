package com.jim.multipos.ui.admin_auth_signup.fragments.confirmation;

import android.os.Bundle;
import android.widget.Toast;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_auth_events.SuccessEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfirmationFragment extends BaseFragment implements ConfirmationView {

    @Inject
    ConfirmationPresenter presenter;

    @Inject
    RxBus bus;

    @BindView(R.id.confirmation)
    MpEditText etConfirm;

    String mail;

    @OnClick(R.id.nextBtn)
    public void onConfirmClicked() {
        presenter.onConfirmation(mail, Integer.parseInt(etConfirm.getText().toString()));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_confirmation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mail = savedInstanceState.getString("mail");
    }

    @Override
    public void onSuccess() {
        bus.send(new SuccessEvent());
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
