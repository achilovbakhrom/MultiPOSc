package com.jim.multipos.ui.vendors.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.vendors.model.ContactItem;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/22/17.
 */

public class VendorContactsAdapter extends RecyclerView.Adapter<VendorContactsAdapter.ContactViewHolder> {

    private OnContactClickListener listener;
    private Context context;
    private List<ContactItem> items;

    public VendorContactsAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new ContactViewHolder(view);
    }

    public void addContactItem(ContactItem contact, int position) {
        items.add(contact);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.contact.setText(items.get(position).getType() == 0 ? context.getString(R.string.phone) : context.getString(R.string.email));
        holder.contactData.setText(items.get(position).getContactDetails());
        if (items.get(position).getType() == 0) {
            holder.contactData.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            holder.contactData.setInputType(InputType.TYPE_CLASS_TEXT);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(OnContactClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ContactItem> contactItems) {
        this.items = contactItems;
        notifyDataSetChanged();
    }

    class ContactViewHolder extends BaseViewHolder {
        @BindView(R.id.tvContacts)
        TextView contact;
        @BindView(R.id.tvContactsValue)
        MpEditText contactData;
        @BindView(R.id.ivRemoveContact)
        MpMiniActionButton remove;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactData.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        if (items.get(getAdapterPosition()).getType() == 1) {
                            if (!Patterns.EMAIL_ADDRESS.matcher(contactData.getText().toString()).matches()) {
                                contactData.setError(context.getString(R.string.email_adress_is_not_valid));
                            } else
                                items.get(getAdapterPosition()).setContactDetails(contactData.getText().toString());
                        } else
                            items.get(getAdapterPosition()).setContactDetails(contactData.getText().toString());
                    } else contactData.setError(context.getString(R.string.cannot_be_empty));
                }
            });

            remove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemove(getAdapterPosition(), items.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnContactClickListener {
        void onRemove(int position, ContactItem contact);
    }
}
