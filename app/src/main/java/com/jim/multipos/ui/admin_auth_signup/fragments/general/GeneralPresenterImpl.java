package com.jim.multipos.ui.admin_auth_signup.fragments.general;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.utils.Utils;

import javax.inject.Inject;

public class GeneralPresenterImpl extends BasePresenterImpl<GeneralView> implements GeneralPresenter{

    GeneralView view;

    @Inject
    protected GeneralPresenterImpl(GeneralView generalView) {
        super(generalView);
        view = generalView;
    }

    @Override
    public void onVerifyData(String email, String password, String confirmPassword) {
        int progress = 0;

        if(!Utils.isEmailValid(email))
            view.onError(R.string.incorrect_email, 1);
        else progress++;

        if(!password.equals(confirmPassword))
            view.onError(R.string.password_dont_match, 2);
        else progress++;

        if(password.length()>5)
            progress++;
        else view.onError(R.string.password_length, 3);
        if(progress == 3)
            view.onSuccess(email, password);
    }
}
