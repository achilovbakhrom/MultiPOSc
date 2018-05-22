package com.jim.multipos.ui.vendor.add_edit.adapter;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ItemRemoveListener;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import butterknife.BindView;
import lombok.Setter;

/**
 * Created by bakhrom on 10/22/17.
 */

public class ContactAdapter extends BaseAdapter<Contact, ContactAdapter.ContactViewHolder> {

    @Setter
    private OnContactClickListener listener;
    private Context context;

    public ContactAdapter(List<Contact> items, Context context) {
        super(items);
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        return new ContactViewHolder(view);
    }

    public void addContactItem(Contact contact, int position) {
        items.add(contact);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.contact.setText(items.get(position).getType() == 0 ?context.getString(R.string.phone) : context.getString(R.string.email));
        holder.contactData.setText(items.get(position).getName());
        if (items.get(position).getType() == 0) {
            holder.contactData.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            holder.contactData.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        holder.remove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemove(position, items.get(position));
            }
        });

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
            contactData.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
            contactData.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        if (items.get(getAdapterPosition()).getType() == 1) {
                            if (!Patterns.EMAIL_ADDRESS.matcher(contactData.getText().toString()).matches()) {
                                contactData.setError(context.getString(R.string.email_adress_is_not_valid));
                            }
                        }
                    } else contactData.setError(context.getString(R.string.cannot_be_empty));
                }
            });
        }
    }

    public interface OnContactClickListener {
        void onRemove(int position, Contact contact);

        void onSave(int position, Contact contact);
    }
}
