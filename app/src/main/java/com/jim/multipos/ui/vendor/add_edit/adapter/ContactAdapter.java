package com.jim.multipos.ui.vendor.add_edit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ItemRemoveListener;
import com.jim.multipos.data.db.model.Contact;

import java.util.List;

import butterknife.BindView;
import lombok.Setter;

/**
 * Created by bakhrom on 10/22/17.
 */

public class ContactAdapter extends BaseAdapter<Contact, ContactAdapter.ContactViewHolder> {

    @Setter
    private ItemRemoveListener<Contact> listener;

    public ContactAdapter(List<Contact> items) {
        super(items);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.contact.setText(items.get(position).getType() == 0 ? "PHONE" : "EMAIL");
        holder.contactData.setText(items.get(position).getName());
        holder.remove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemove(position, items.get(position));
            }
        });
    }

    class ContactViewHolder extends BaseViewHolder {
        @BindView(R.id.tvContacts)
        TextView contact;
        @BindView(R.id.tvContactsValue)
        TextView contactData;
        @BindView(R.id.ivRemoveContact)
        ImageView remove;
        public ContactViewHolder(View itemView) {
            super(itemView);
        }
    }
}
