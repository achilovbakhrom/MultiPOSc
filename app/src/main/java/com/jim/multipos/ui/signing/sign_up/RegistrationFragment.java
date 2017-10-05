package com.jim.multipos.ui.signing.sign_up;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.R;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.signing.adapters.ContactsAdapter;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationFragment extends BaseFragment implements RegistrationView, RegistrationPresenter {

    @Getter
    @BindView(R.id.etOrgName)
    MpEditText organizationName;
    @Getter
    @BindView(R.id.etOrgAddress)
    MpEditText organizationAddress;
    @Getter
    @BindView(R.id.etOrgEmail)
    MpEditText organizationEmail;
    @Getter
    @BindView(R.id.etOrgZipCode)
    MpEditText organizationZipCode;
    @Getter
    @BindView(R.id.etContacts)
    MpEditText contactText;
    @Getter
    @BindView(R.id.rvContacts)
    RecyclerView contactsList;
    @Getter
    @BindView(R.id.spContacts)
    MpSpinner contactsSpinner;
    @Inject
    RegistrationPresenter presenter;
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    SignActivity activity;


    private List<Contact> contactDatas;


    private final String CONTACTS_KEY = "CONTACTS_KEY";


    private String[] contactTypes;

    @Override
    protected void init(Bundle savedInstanceState) {
        contactTypes = new String[]{activity.getResources().getString(R.string.phone), activity.getResources().getString(R.string.email)};
        if (savedInstanceState != null) {
            contactDatas = Parcels.unwrap(savedInstanceState.getParcelable(CONTACTS_KEY));
        } else {
            contactDatas = new ArrayList<>();
        }
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
        RecyclerView rv;

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


    @Override
    public void register() {
        presenter.register();
    }

    @OnClick({R.id.btnBack, R.id.btnRegistration, R.id.ivAddContact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistration:
                register();
//                String code = etOrgZipCode.getText().toString();
//                String address = etOrgAddress.getText().toString();
//                String name = etOrgName.getText().toString();
//                String email = etOrgEmail.getText().toString();
//                if (!email.equals("")) {
//                    if (isEmailValid(email))
//                        presenter.displayFragment(name, address, email, code);
//                    else etOrgEmail.setError("wrong input type");
//                } else etOrgEmail.setError(getResources().getString(R.string.enter_organization_email));
                break;
            case R.id.ivAddContact:
//                if (!etContacts.getText().toString().equals("")) {
//                    if (etContacts.getInputType() == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
//                        if (isEmailValid(etContacts.getText().toString())) {
//                            presenter.setRecyclerViewItems(spContacts.getSelectedItem().toString(), etContacts.getText().toString());
//                            etContacts.setText("");
//                        } else {
//                            etContacts.setError("wrong input type");
//                        }
//                    } else {
//                        presenter.setRecyclerViewItems(spContacts.getSelectedItem().toString(), etContacts.getText().toString());
//                        etContacts.setText("");
//                    }
//                } else etContacts.setError(getResources().getString(R.string.enter_phone_number));
                break;
            case R.id.btnBack:
//                presenter.popBackStack();
                break;
        }
    }


    public void setRecyclerView(ArrayList<Contact> contacts) {
        contactsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ContactsAdapter adapter = new ContactsAdapter(contacts, getContext());
        contactsList.setAdapter(adapter);
    }


}
