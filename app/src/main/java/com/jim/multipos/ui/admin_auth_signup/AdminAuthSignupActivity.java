package com.jim.multipos.ui.admin_auth_signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.ui.admin_auth_signup.fragments.confirmation.ConfirmationFragment;
import com.jim.multipos.ui.admin_auth_signup.fragments.general.GeneralFragment;
import com.jim.multipos.ui.admin_auth_signup.fragments.info.InfoFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_auth_events.GeneralEvent;
import com.jim.multipos.utils.rxevents.admin_auth_events.InfoEvent;
import com.jim.multipos.utils.rxevents.admin_auth_events.OnBackEvent;
import com.jim.multipos.utils.rxevents.admin_auth_events.SuccessEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AdminAuthSignupActivity extends BaseActivity implements AdminAuthSignupActivityView {

    @Inject
    MultiPosApiService service;
    @Inject
    AdminAuthSignupActivityPresenter presenter;
    @Inject
    RxBus bus;

    CompositeDisposable disposable = new CompositeDisposable();

    String mail, password, confirmation_email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_registration_layout);
        addFragment(R.id.fragment_container, new GeneralFragment());

        disposable.add(bus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            if (o instanceof GeneralEvent) {
                mail = ((GeneralEvent) o).getUsername();
                password = ((GeneralEvent) o).getPassword();

                InfoFragment fragment = new InfoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mail", mail);
                bundle.putString("pass", password);
                fragment.setArguments(bundle);
                replaceFragmentWithBackStack(R.id.fragment_container, fragment);
            }
            if (o instanceof InfoEvent) {
                confirmation_email = ((InfoEvent) o).getMail();

                ConfirmationFragment fragment = new ConfirmationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mail", confirmation_email);
                fragment.setArguments(bundle);
                replaceFragmentWithClearTop(R.id.fragment_container, fragment);

            }
            if (o instanceof SuccessEvent) {
                Toast.makeText(this, "Successfully confirmed", Toast.LENGTH_LONG).show();
            }
            if (o instanceof OnBackEvent) {
                onBackPressed();
            }

        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

}
