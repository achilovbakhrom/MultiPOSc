package com.jim.multipos.ui.signing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Contact;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 27.07.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private ArrayList<Contact> contact;

    public ContactsAdapter(ArrayList<Contact> contact, Context context) {
        this.contact = contact;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvContacts.setText(contact.get(position).getContactType());
        holder.tvContactsValue.setText(contact.get(position).getContactValue());
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
        }
    }
}
