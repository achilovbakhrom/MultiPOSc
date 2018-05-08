package com.jim.multipos.ui.settings.accounts.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsSettingsAdapter extends RecyclerView.Adapter<AccountsSettingsAdapter.SystemAccountViewHolder> {

    private final Context context;
    private final List<Account> accountList;
    private OnSaveClicked callback;

    public AccountsSettingsAdapter(Context context, List<Account> accountList, OnSaveClicked callback) {
        this.context = context;
        this.accountList = accountList;
        this.callback = callback;
    }

    @Override
    public SystemAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_item, parent, false);
        return new AccountsSettingsAdapter.SystemAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SystemAccountViewHolder holder, int position) {
        if (accountList.get(position).getStaticAccountType() == Account.CASH_ACCOUNT) {
            holder.etAccountName.setEnabled(false);
            holder.chbActive.setEnabled(false);
            holder.ivSave.setEnabled(false);
            holder.ivSave.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorGreyDark));
            holder.chbActive.setCheckboxColor(R.color.colorGreyDark);
        }
        holder.etAccountName.setText(accountList.get(position).getName());
        holder.chbActive.setChecked(accountList.get(position).getIsActive());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class SystemAccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etAccountName)
        MpEditText etAccountName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.ivSave)
        MpMiniActionButton ivSave;

        public SystemAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chbActive.setCheckedChangeListener(isChecked -> accountList.get(getAdapterPosition()).setIsActive(isChecked));
            etAccountName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        accountList.get(getAdapterPosition()).setName(etAccountName.getText().toString());
                    } else {
                        etAccountName.setError(context.getString(R.string.enter_account_name));
                    }
                }
            });
            ivSave.setOnClickListener(view -> {
                callback.onSave(accountList.get(getAdapterPosition()), getAdapterPosition());
            });


        }
    }

    public interface OnSaveClicked {
        void onSave(Account account, int position);
    }
}
