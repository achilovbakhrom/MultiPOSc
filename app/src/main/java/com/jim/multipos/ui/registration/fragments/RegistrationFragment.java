package com.jim.multipos.ui.registration.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.R;
import com.jim.multipos.ui.registration.di.LoginActivityComponent;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.registration.adapters.ContactsAdapter;
import com.jim.multipos.ui.registration.presenters.RegistrationPresenter;
import com.jim.multipos.ui.registration.LoginActivity;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jim.multipos.utils.CommonUtils.isEmailValid;

/**
 * Created by DEV on 26.07.2017.
 */

public class RegistrationFragment extends BaseFragment implements RegistrationFragmentView {
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnRegistrationSecond)
    MpButton btnRegistration;
    @BindView(R.id.etOrgName)
    MpEditText etOrgName;
    @BindView(R.id.etOrgAddress)
    MpEditText etOrgAddress;
    @BindView(R.id.etOrgEmail)
    MpEditText etOrgEmail;
    @BindView(R.id.etOrgZipCode)
    MpEditText etOrgZipCode;
    @BindView(R.id.etContacts)
    MpEditText etContacts;
    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;
    @BindView(R.id.ivAddContact)
    ImageView ivAddContact;
    MpSpinner spContacts;
    @Inject
    RegistrationPresenter presenter;
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    LoginActivity activity;
    private ArrayList<String> list;
    private String[] contacts;
    private Unbinder unbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.reg_fragment, container, false);
        this.getComponent(LoginActivityComponent.class).inject(this);
        unbinder = ButterKnife.bind(this, rootView);
        contacts = new String[]{activity.getResources().getString(R.string.phone), activity.getResources().getString(R.string.email)};
        spContacts = (MpSpinner) rootView.findViewById(R.id.spContacts);
        list = new ArrayList<>();
        presenter.init(this);
        presenter.setItems(contacts);
        etContacts.setInputType(InputType.TYPE_CLASS_PHONE);
        spContacts.setOnItemSelectedListener((adapterView, view, position, l) -> {
            switch (position) {
                case 0:
                    etContacts.setInputType(InputType.TYPE_CLASS_PHONE);
                    etContacts.setText("");
                    break;
                case 1:
                    etContacts.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    etContacts.setText("");
                    break;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnClick(R.id.btnBack)
    public void back() {
        presenter.popBackStack();
    }

    @OnClick(R.id.btnRegistrationSecond)
    public void registration() {

        String code = etOrgZipCode.getText().toString();
        String address = etOrgAddress.getText().toString();
        String name = etOrgName.getText().toString();
        String email = etOrgEmail.getText().toString();
        if (!email.equals("")) {
            if (isEmailValid(email))
                presenter.displayFragment(name, address, email, code);
            else etOrgEmail.setError("wrong input type");
        } else etOrgEmail.setError(getResources().getString(R.string.enter_organization_email));
        presenter.wrapData();
    }

    @OnClick(R.id.ivAddContact)
    public void addContact() {
        if (!etContacts.getText().toString().equals("")) {
            if (etContacts.getInputType() == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                if (isEmailValid(etContacts.getText().toString())) {
                    presenter.setRecyclerViewItems(list.get(spContacts.selectedItem()), etContacts.getText().toString());
                    etContacts.setText("");
                } else {
                    etContacts.setError("wrong input type");
                }
            } else {
                presenter.setRecyclerViewItems(list.get(spContacts.selectedItem()), etContacts.getText().toString());
                etContacts.setText("");
            }
        } else etContacts.setError(getResources().getString(R.string.enter_phone_number));
    }

    @Override
    public void setSpinnerItems(ArrayList<String> items) {
        spContacts.setItems(items);
        spContacts.setAdapter();
        list = items;
    }

    @Override
    public void displayFragment(RegistrationConfirmFragment confirmFragment) {
//        posFragmentManager.displayFragment(confirmFragment, R.id.loginFragment);
        activity.openRegistrationConfirm(confirmFragment);
    }

    @Override
    public void popFromBackStack() {
//        posFragmentManager.popBackStack();
        activity.popFromBackStack();
    }

    @Override
    public void setRecyclerView(ArrayList<Contact> contacts) {
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        ContactsAdapter adapter = new ContactsAdapter(contacts, getContext());
        rvContacts.setAdapter(adapter);
    }

    @Override
    public void sendData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
