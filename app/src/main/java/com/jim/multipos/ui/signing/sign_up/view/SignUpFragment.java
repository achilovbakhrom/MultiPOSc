package com.jim.multipos.ui.signing.sign_up.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.signing.sign_up.presenter.SignUpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 26.07.2017.
 */

public class SignUpFragment extends BaseFragment implements SignUpView {

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
    SignUpPresenter presenter;

    private String[] contactTypes;

    @Override
    protected void init(Bundle savedInstanceState) {
        contactTypes = new String[]{ getResources().getString(R.string.phone), getResources().getString(R.string.email)};
        contactText.setInputType(InputType.TYPE_CLASS_PHONE);
        contactsSpinner.setItems(contactTypes);
        contactsSpinner.setAdapter();
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
    protected void rxConnections() {

    }

    @Override
    protected int getLayout() {
        return R.layout.reg_fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
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
                ((SignActivity) getContext()).openSignIn();
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
    public void addContactButtonClicked() {
        int type = -1;
        switch (contactsSpinner.selectedItemPosition()) {
            case 0:
                type = Contact.PHONE;
                break;
            case 1:
                type = Contact.E_MAIL;
                break;
        }
        presenter.addContact(type, contactText.getText().toString());
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


    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
        private ArrayList<Contact> contact;
        private Context context;

        public ContactsAdapter(ArrayList<Contact> contact, Context context) {
            this.contact = contact;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String type = null;
            switch (contact.get(position).getType()) {
                case 0:
                    type = context.getResources().getString(R.string.phone);
                    break;
                case 1:
                    type = context.getResources().getString(R.string.email);
                    break;
            }
            if (type != null) {
                holder.tvContacts.setText(type);
            }
            holder.tvContactsValue.setText(contact.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return contact.size();
        }

        public Contact getItem(int pos) {
            return contact.get(pos);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvContacts)
            TextView tvContacts;
            @BindView(R.id.tvContactsValue)
            TextView tvContactsValue;
            @BindView(R.id.ivRemoveContact)
            ImageView ivRemoveContact;
            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
            @OnClick(R.id.ivRemoveContact)
            public void onRemove() {
                contact.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                presenter.deleteContact(getAdapterPosition());
            }
        }
    }
}
