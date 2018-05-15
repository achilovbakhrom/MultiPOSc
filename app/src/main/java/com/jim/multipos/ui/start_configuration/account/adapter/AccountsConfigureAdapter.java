package com.jim.multipos.ui.start_configuration.account.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsConfigureAdapter extends RecyclerView.Adapter<AccountsConfigureAdapter.SystemAccountViewHolder> {

    private final Context context;
    private final List<Account> accountList;
    private OnSaveClicked callback;

    public AccountsConfigureAdapter(Context context, List<Account> accountList, OnSaveClicked callback) {
        this.context = context;
        this.accountList = accountList;
        this.callback = callback;
    }

    @Override
    public SystemAccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_configure_item, parent, false);
        return new AccountsConfigureAdapter.SystemAccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SystemAccountViewHolder holder, int position) {
        if (accountList.get(position).getStaticAccountType() == Account.CASH_ACCOUNT) {
            holder.ivDelete.setEnabled(false);
            holder.ivDelete.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorGreyDark));
            holder.chbActive.setEnabled(false);
            holder.chbActive.setCheckboxColor(R.color.colorGreyDark);
        }
        holder.chbActive.setEnabled(false);
        holder.etAccountName.setText(accountList.get(position).getName());
        holder.chbActive.setChecked(accountList.get(position).getIsActive());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    class SystemAccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.etAccountName)
        TextView etAccountName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.ivDelete)
        MpMiniActionButton ivDelete;

        public SystemAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivDelete.setOnClickListener(view -> {
                callback.onDelete(accountList.get(getAdapterPosition()), getAdapterPosition());
            });
        }
    }

    public interface OnSaveClicked {
        void onDelete(Account account, int position);
    }
}
