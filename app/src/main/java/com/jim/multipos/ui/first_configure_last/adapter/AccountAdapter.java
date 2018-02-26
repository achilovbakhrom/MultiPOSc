package com.jim.multipos.ui.first_configure_last.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.core.ItemRemoveListener;

import java.util.List;

import butterknife.BindView;
import lombok.Setter;

/**
 * Created by bakhrom on 10/18/17.
 */

public class AccountAdapter extends BaseAdapter<Account, AccountAdapter.AccountViewHolder> {

    @Setter
    private ItemRemoveListener<Account> itemRemoveListener;
    public AccountAdapter(List<Account> items) {
        super(items);
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new AccountAdapter.AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        Account account = items.get(position);
        holder.accountName.setText(account.getName());
        holder.remove.setOnClickListener(v -> {
            if (itemRemoveListener != null) {
                itemRemoveListener.onItemRemove(position, items.get(position));
            }
        });
        if (account.getStaticAccountType() == Account.CASH_ACCOUNT)
            holder.remove.setVisibility(View.GONE);
    }

    class AccountViewHolder extends BaseViewHolder {
        @BindView(R.id.tvAccountName)
        TextView accountName;
        @BindView(R.id.ivRemove)
        ImageView remove;

        public AccountViewHolder(View itemView) {
            super(itemView);
        }
    }


}
