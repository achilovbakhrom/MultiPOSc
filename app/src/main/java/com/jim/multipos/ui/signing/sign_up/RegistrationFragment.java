package com.jim.multipos.ui.signing.sign_up;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.adapters.ContactsAdapter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationFragment extends BaseFragment implements RegistrationView {

    @BindView(R.id.etOrgName)
    MpEditText organizationName;
    @BindView(R.id.etOrgAddress)
    MpEditText organizationAddress;
    @BindView(R.id.etOrgEmail)
    MpEditText organizationEmail;
    @BindView(R.id.etOrgZipCode)
    MpEditText organizationZipCode;
    @BindView(R.id.etContacts)
    MpEditText contactText;
    @BindView(R.id.rvContacts)
    RecyclerView contactsList;
    @BindView(R.id.spContacts)
    MpSpinner contactsSpinner;
    @Inject
    RegistrationPresenter presenter;

    private String[] contactTypes;

    @Override
    protected void init(Bundle savedInstanceState) {
        contactTypes = new String[]{getResources().getString(R.string.phone), getResources().getString(R.string.email)};
        contactText.setInputType(InputType.TYPE_CLASS_PHONE);
        contactsSpinner.setOnItemSelectedListener((adapterView, view, position, l) -> {
            switch (position) {
                case 0:
                    contactText.setInputType(InputType.TYPE_CLASS_PHONE);
                    contactText.setText("");
                    break;
                case 1:
                    contactText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    contactText.setText("");
                    break;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayout() {
        return R.layout.reg_fragment;
    }




    @OnClick({R.id.btnBack, R.id.btnRegistration, R.id.ivAddContact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistration:
                registerButtonClicked();
                break;
            case R.id.ivAddContact:
                //TODO add item to contact list
                break;
            case R.id.btnBack:
                ((SignActivity) getContext()).openSignUp();
                break;
        }
    }


    public void setRecyclerView(ArrayList<Contact> contacts) {
        contactsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ContactsAdapter adapter = new ContactsAdapter(contacts, getContext());
        contactsList.setAdapter(adapter);
    }


    @Override
    public String getOrganizationName() {
        return organizationName.getText().toString();
    }

    @Override
    public String getAddress() {
        return organizationAddress.getText().toString();
    }

    @Override
    public String getZipCode() {
        return organizationZipCode.getText().toString();
    }

    @Override
    public String getEMail() {
        return organizationEmail.getText().toString();
    }

    @Override
    public void registerButtonClicked() {
        presenter.register();
    }

    @Override
    public void setErrorToOrganizationName(String error) {
        organizationName.setError(error);
    }

    @Override
    public void setErrorToEmail(String error) {
        organizationEmail.setError(error);
    }

    @Override
    public void openConfirmationFragment() {
        ((SignActivity) getContext()).openConfirmation();
    }

}
