package com.jim.multipos.ui.settings.payment_type.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.settings.payment_type.model.PaymentTypeItem;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentTypeSettingsAdapter extends RecyclerView.Adapter<PaymentTypeSettingsAdapter.SystemPaymentTypeViewHolder> {

    private final Context context;
    private final List<PaymentTypeItem> items;
    private OnSaveClicked callback;
    private List<Account> accountList;

    public PaymentTypeSettingsAdapter(Context context, List<PaymentTypeItem> items, OnSaveClicked callback, List<Account> accountList) {
        this.context = context;
        this.items = items;
        this.callback = callback;
        this.accountList = accountList;
    }

    @Override
    public SystemPaymentTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_settings_item, parent, false);
        return new PaymentTypeSettingsAdapter.SystemPaymentTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SystemPaymentTypeViewHolder holder, int position) {
        String[] accounts = new String[accountList.size()];
        for (int i = 0; i < accountList.size(); i++) {
            accounts[i] = accountList.get(i).getName();
        }
        holder.spAccount.setAdapter(accounts);
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).getId().equals(items.get(position).getAccount().getId()))
                holder.spAccount.setSelection(i);
        }
        if (items.get(position).getPaymentType().getTypeStaticPaymentType() == PaymentType.CASH_PAYMENT_TYPE) {
            holder.etPaymentTypeName.setEnabled(false);
            holder.chbActive.setEnabled(false);
            holder.ivSave.setEnabled(false);
            holder.spAccount.disableSpinner();
            holder.ivSave.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorGreyDark));
            holder.chbActive.setCheckboxColor(R.color.colorGreyDark);
        }
        holder.etPaymentTypeName.setText(items.get(position).getName());
        holder.chbActive.setChecked(items.get(position).isActive());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SystemPaymentTypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etPaymentTypeName)
        MpEditText etPaymentTypeName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.ivSave)
        MpMiniActionButton ivSave;
        @BindView(R.id.spAccount)
        MPosSpinner spAccount;

        public SystemPaymentTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chbActive.setCheckedChangeListener(isChecked -> items.get(getAdapterPosition()).setActive(isChecked));
            etPaymentTypeName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        items.get(getAdapterPosition()).setName(etPaymentTypeName.getText().toString());
                    } else {
                        etPaymentTypeName.setError(context.getString(R.string.enter_payment_method));
                    }
                }
            });
            ivSave.setOnClickListener(view -> {
                if (!items.get(getAdapterPosition()).getAccount().getIsActive() && items.get(getAdapterPosition()).isActive()) {
                    Toast.makeText(context, context.getString(R.string.payment_type_cant_be_active), Toast.LENGTH_SHORT).show();
                    chbActive.setChecked(false);
                } else callback.onSave(items.get(getAdapterPosition()), getAdapterPosition());
            });
            spAccount.setItemSelectionListener((view, position) -> items.get(getAdapterPosition()).setAccount(accountList.get(position)));
        }
    }

    public interface OnSaveClicked {
        void onSave(PaymentTypeItem paymentType, int position);
    }
}
