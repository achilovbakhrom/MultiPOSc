package com.jim.multipos.ui.signing.sign_up;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.operations.ContactOperations;
import com.jim.multipos.ui.signing.SignActivity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.jim.multipos.utils.BundleConstants.ORG_ADDRESS;
import static com.jim.multipos.utils.BundleConstants.ORG_CODE;
import static com.jim.multipos.utils.BundleConstants.ORG_EMAIL;
import static com.jim.multipos.utils.BundleConstants.ORG_NAME;
import static com.jim.multipos.utils.CommonUtils.isEmailValid;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationPresenterImpl extends BasePresenterImpl<RegistrationView> implements RegistrationPresenter{


    private final DatabaseManager databaseManager;

    @Inject
    public RegistrationPresenterImpl(RegistrationView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
    }

    @Override
    public void register() {
        RegistrationFragment fragment = ((RegistrationFragment) view);
        String code = fragment.getOrganizationZipCode().getText().toString();
        String address = fragment.getOrganizationAddress().getText().toString();
        String name = fragment.getOrganizationName().getText().toString();
        String email = fragment.getOrganizationEmail().getText().toString();

        if (name.isEmpty()) {
            fragment.getOrganizationName().setError("Please, enter organization name");
            return;
        }
        if (email.isEmpty()) {
            fragment.getOrganizationEmail().setError("Please, enter your e-mail address");
            return;
        }
        if (isEmailValid(email)) {
            fragment.getOrganizationEmail().setError("Email is not valid...");
            return;
        }


        ((SignActivity)fragment.getContext()).getActivityFragmentManager().popBackStack();

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);

    }
}
